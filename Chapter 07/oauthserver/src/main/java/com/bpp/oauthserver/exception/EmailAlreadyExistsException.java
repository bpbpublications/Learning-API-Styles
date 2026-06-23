package com.bpp.oauthserver.exception;

public class EmailAlreadyExistsException extends Throwable {
    public EmailAlreadyExistsException(String email) {
        super(email);
    }
}
