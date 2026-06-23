package com.bpp.oauthserver.dto;

import lombok.Builder;
import lombok.Data;

// AuthResponse.java
@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;       // "Bearer"
    private long expiresIn;       // seconds
    private UserInfo user;

    @Data
    @Builder
    public static class UserInfo {
        private String id;
        private String email;
        private String fullName;
        private String role;
    }
}
