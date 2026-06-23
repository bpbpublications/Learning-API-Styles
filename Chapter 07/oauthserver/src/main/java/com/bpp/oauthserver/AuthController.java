package com.bpp.oauthserver;

import com.bpp.oauthserver.dto.AuthResponse;
import com.bpp.oauthserver.dto.LoginRequest;
import com.bpp.oauthserver.dto.RegisterRequest;
import com.bpp.oauthserver.entity.User;
import com.bpp.oauthserver.exception.EmailAlreadyExistsException;
import com.bpp.oauthserver.exception.InvalidCredentialsException;
import com.bpp.oauthserver.exception.InvalidTokenException;
import com.bpp.oauthserver.services.AuthService;
import com.bpp.oauthserver.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest req) throws EmailAlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest req,
            HttpServletRequest httpReq) throws AccountLockedException, InvalidCredentialsException {
        return ResponseEntity.ok(authService.login(req, httpReq));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestHeader("X-Refresh-Token") String refreshToken) throws InvalidTokenException {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader(value = "X-Refresh-Token", required = false)
            String refreshToken) throws InvalidTokenException {

        String accessToken = extractBearerToken(authHeader);
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(
            @RequestHeader("Authorization") String authHeader) throws InvalidTokenException {

        String token = extractBearerToken(authHeader);
        UUID userId = jwtService.extractUserId(token);
        authService.logoutAllDevices(userId.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserInfo> me(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole().name())
                        .build()
        );
    }

    private String extractBearerToken(String header) throws InvalidTokenException {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new InvalidTokenException("Missing or invalid Authorization header");
        }
        return header.substring(7);
    }
}