package com.bpp.oauthserver.filter;

import com.bpp.oauthserver.entity.User;
import com.bpp.oauthserver.entity.repo.UserRepository;
import com.bpp.oauthserver.services.JwtService;
import com.bpp.oauthserver.services.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// JwtAuthenticationFilter.java
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepository userRepo;
    @Autowired
    TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.isAccessTokenValid(token)) {
                chain.doFilter(req, res);
                return;
            }

            // Check Redis blacklist (logout/rotation)
            String jti = jwtService.extractJti(token);
            if (blacklistService.isBlacklisted(jti)) {
                log.debug("Rejected blacklisted token jti={}", jti);
                chain.doFilter(req, res);
                return;
            }

            UUID userId = jwtService.extractUserId(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepo.findById(String.valueOf(userId)).orElse(null);

                if (user != null && user.isEnabled() && user.isAccountNonLocked()) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user, null, user.getAuthorities()
                            );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(req)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.debug("JWT filter error: {}", e.getMessage());
            // Let the request continue — Spring Security handles 401 downstream
        }

        chain.doFilter(req, res);
    }
}