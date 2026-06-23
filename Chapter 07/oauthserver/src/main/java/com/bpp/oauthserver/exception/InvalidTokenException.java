package com.bpp.oauthserver.exception;

public class InvalidTokenException extends Throwable {
    public InvalidTokenException(String missingOrInvalidAuthorizationHeader) {
        super(missingOrInvalidAuthorizationHeader);
    }
}
