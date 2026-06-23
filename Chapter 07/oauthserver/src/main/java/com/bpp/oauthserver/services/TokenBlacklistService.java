package com.bpp.oauthserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

// TokenBlacklistService.java
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Called on logout — adds jti to Redis with TTL matching token expiry.
     * Token is structurally valid but blacklisted — filter rejects it.
     */
    public void blacklist(String jti, Date expiration) {
        long ttlSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;
        if (ttlSeconds > 0) {
            redisTemplate.opsForValue()
                    .set(BLACKLIST_PREFIX + jti, "revoked",
                            Duration.ofSeconds(ttlSeconds));
            //  log.debug("Blacklisted token jti={} ttl={}s", jti, ttlSeconds);
        }
    }

    public boolean isBlacklisted(String jti) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + jti);
    }
}