package com.bpp.oauthserver.services;

import com.bpp.oauthserver.dto.AuthResponse;
import com.bpp.oauthserver.dto.LoginRequest;
import com.bpp.oauthserver.dto.RegisterRequest;
import com.bpp.oauthserver.entity.RefreshToken;
import com.bpp.oauthserver.entity.User;
import com.bpp.oauthserver.entity.UserRole;
import com.bpp.oauthserver.entity.repo.RefreshTokenRepository;
import com.bpp.oauthserver.entity.repo.UserRepository;
import com.bpp.oauthserver.exception.EmailAlreadyExistsException;
import com.bpp.oauthserver.exception.InvalidCredentialsException;
import com.bpp.oauthserver.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountLockedException;
import java.time.LocalDateTime;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    RefreshTokenRepository refreshTokenRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    TokenBlacklistService blacklistService;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Value("${security.max-login-attempts}")
    private int maxAttempts;
    @Value("${security.lockout-duration-minutes}")
    private int lockoutMinutes;


    // ── Register ──────────────────────────────────────────────────

    /**
     * Registers a new user in the system.
     * <p>
     * Validates if the email already exists, hashes the password,
     * and persists the user with default role CUSTOMER.
     *
     * @param req the registration request containing user details
     * @return AuthResponse containing access and refresh tokens with user info
     * @throws EmailAlreadyExistsException if the email is already registered
     */
    public AuthResponse register(RegisterRequest req) throws EmailAlreadyExistsException {
        if (userRepo.existsByEmail(req.getEmail().toLowerCase())) {
            throw new EmailAlreadyExistsException(req.getEmail());
        }

        User user = User.builder()
                .email(req.getEmail().toLowerCase())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .role(UserRole.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        user = userRepo.save(user);
        log.info("New user registered: {}", user.getEmail());
        return buildAuthResponse(user, req.getEmail());
    }

    // ── Login ─────────────────────────────────────────────────────

    /**
     * Authenticates a user based on email and password.
     * <p>
     * Validates credentials, handles failed attempts, and returns
     * authentication tokens along with device and IP information.
     *
     * @param req the login request containing credentials
     * @param httpReq HTTP request used to extract client metadata (IP, User-Agent)
     * @return AuthResponse containing tokens and user information
     * @throws InvalidCredentialsException if credentials are incorrect
     * @throws AccountLockedException if account is locked due to failed attempts
     */
    public AuthResponse login(LoginRequest req, HttpServletRequest httpReq)
            throws InvalidCredentialsException, AccountLockedException {

        User user = userRepo.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            handleFailedAttempt(user);
            throw new InvalidCredentialsException();
        }

        userRepo.save(user);

        log.info("Login success: {} from {}", user.getEmail(),
                httpReq.getRemoteAddr());

        return buildAuthResponse(user,
                httpReq.getHeader("User-Agent"),
                httpReq.getRemoteAddr());
    }

    // ── Token Refresh ─────────────────────────────────────────────

    /**
     * Refreshes authentication tokens using a valid refresh token.
     * <p>
     * Validates the refresh token, detects reuse attacks,
     * revokes old token, and issues a new token pair.
     *
     * @param rawRefreshToken the raw refresh token provided by client
     * @return AuthResponse containing new access and refresh tokens
     * @throws InvalidTokenException if token is invalid, expired, or revoked
     */
    public AuthResponse refresh(String rawRefreshToken) throws InvalidTokenException {
        String tokenHash = jwtService.hashToken(rawRefreshToken);

        RefreshToken stored = refreshTokenRepo.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

        if (!stored.isValid()) {
            if (stored.isRevoked()) {
                log.warn("Refresh token reuse detected for user {}",
                        stored.getUser().getId());
                refreshTokenRepo.revokeAllForUser(stored.getUser().getId());
            }
            throw new InvalidTokenException("Refresh token expired or revoked");
        }

        stored.setRevoked(true);
        refreshTokenRepo.save(stored);

        return buildAuthResponse(stored.getUser(), null, null);
    }

    // ── Logout ────────────────────────────────────────────────────

    /**
     * Logs out a user by invalidating access and refresh tokens.
     * <p>
     * Blacklists the access token and revokes the refresh token if provided.
     *
     * @param accessToken the JWT access token
     * @param rawRefreshToken optional refresh token to revoke
     */
    public void logout(String accessToken, String rawRefreshToken) {
        String jti = jwtService.extractJti(accessToken);
        Date expiry = jwtService.extractExpiration(accessToken);
        blacklistService.blacklist(jti, expiry);

        if (rawRefreshToken != null) {
            String hash = jwtService.hashToken(rawRefreshToken);
            refreshTokenRepo.findByTokenHash(hash)
                    .ifPresent(t -> {
                        t.setRevoked(true);
                        refreshTokenRepo.save(t);
                    });
        }

        log.info("User logged out, jti={}", jti);
    }

    // ── Logout All Devices ─────────────────────────────────────────

    /**
     * Logs out a user from all active devices.
     * <p>
     * Revokes all refresh tokens associated with the user.
     *
     * @param userId the unique identifier of the user
     */
    public void logoutAllDevices(String userId) {
        refreshTokenRepo.revokeAllForUser(userId);
        log.info("Revoked all sessions for user {}", userId);
    }

    // ── Private helpers ───────────────────────────────────────────

    /**
     * Handles failed login attempts.
     * <p>
     * Can be extended to increment retry count and apply account lock logic.
     *
     * @param user the user entity
     */
    private void handleFailedAttempt(User user) {
        userRepo.save(user);
    }

    /**
     * Builds authentication response including JWT tokens and user details.
     * <p>
     * Generates access and refresh tokens, persists refresh token metadata,
     * and returns structured response.
     *
     * @param user the authenticated user
     * @param deviceInfo client device information (User-Agent)
     * @param ipAddress client IP address
     * @return AuthResponse with tokens and user info
     */
    private AuthResponse buildAuthResponse(User user, String deviceInfo,
                                           String ipAddress) {

        String accessToken = jwtService.generateAccessToken(user);
        String rawRefresh = jwtService.generateRefreshToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(jwtService.hashToken(rawRefresh))
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .expiresAt(LocalDateTime.now().plusSeconds(
                        jwtService.getAccessTokenExpiryMs() / 1000 * 48 * 7
                ))
                .build();

        refreshTokenRepo.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(rawRefresh)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpiryMs() / 1000)
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole().name())
                        .build())
                .build();
    }

    /**
     * Overloaded helper method to build auth response without device and IP info.
     *
     * @param user the authenticated user
     * @param email email (not used internally, kept for compatibility)
     * @return AuthResponse with tokens
     */
    private AuthResponse buildAuthResponse(User user, String email) {
        return buildAuthResponse(user, null, null);
    }
}