package com.bpp.oauthserver.services;

import com.bpp.oauthserver.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
@Service
@Slf4j
public class JwtService {

    private final SecretKey signingKey;
    /**
     * -- GETTER --
     *  Returns configured access token expiry time.
     *
     * @return access token expiry in milliseconds
     */
    @Getter
    private final long accessTokenExpiryMs;
    private final long refreshTokenExpiryMs;

    /**
     * Initializes JWT service with signing key and token expiry configurations.
     *
     * @param secret Base64-encoded secret key used for signing JWT tokens
     * @param accessExpiryMs access token expiration time in milliseconds
     * @param refreshExpiryMs refresh token expiration time in milliseconds
     */
    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiry-ms}") long accessExpiryMs,
            @Value("${jwt.refresh-token-expiry-ms}") long refreshExpiryMs) {

        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
        this.accessTokenExpiryMs = accessExpiryMs;
        this.refreshTokenExpiryMs = refreshExpiryMs;
    }

    // ── Access Token ──────────────────────────────────────────────

    /**
     * Generates a signed JWT access token for the given user.
     * <p>
     * Includes user ID (subject), email, role, token type, issue time,
     * expiration, and a unique token ID (jti) for tracking/blacklisting.
     *
     * @param user the authenticated user
     * @return signed JWT access token
     */
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .claim("type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiryMs))
                .setId(UUID.randomUUID().toString())
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Refresh Token ─────────────────────────────────────────────

    /**
     * Generates a secure random refresh token.
     * <p>
     * This token is NOT a JWT. It is a random 256-bit value,
     * returned to the client and stored as a hashed value in DB.
     *
     * @return raw refresh token string
     */
    public String generateRefreshToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Hashes a raw token using SHA-256.
     * <p>
     * Used to securely store refresh tokens in the database
     * without persisting the raw token value.
     *
     * @param rawToken the raw token string
     * @return Base64-encoded SHA-256 hash of the token
     */
    public String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    // ── Validation ────────────────────────────────────────────────

    /**
     * Validates the given access token.
     * <p>
     * Ensures token signature, structure, and expiration are valid.
     *
     * @param token JWT access token
     * @return true if valid, false otherwise
     */
    public boolean isAccessTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
           log.error(e.getMessage());
           return false;
        }
    }

    /**
     * Extracts user ID (subject) from the token.
     *
     * @param token JWT access token
     * @return user ID as UUID
     */
    public UUID extractUserId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }

    /**
     * Extracts JWT ID (jti) from the token.
     * <p>
     * Used for token tracking and blacklisting.
     *
     * @param token JWT access token
     * @return token unique identifier (jti)
     */
    public String extractJti(String token) {
        return parseClaims(token).getId();
    }

    /**
     * Extracts expiration date from the token.
     *
     * @param token JWT access token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    // ── Internal Helper ───────────────────────────────────────────

    /**
     * Parses and validates JWT claims using the signing key.
     *
     * @param token JWT token
     * @return Claims object containing token data
     * @throws JwtException if token is invalid or tampered
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}