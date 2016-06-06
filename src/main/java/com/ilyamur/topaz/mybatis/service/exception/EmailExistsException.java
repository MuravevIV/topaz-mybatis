package com.ilyamur.topaz.mybatis.service.exception;

public class EmailExistsException extends Exception {

    private String message;

    public EmailExistsException(String email) {
        this.message = String.format("Email '%s' already exists in database.", email);
    }

    public EmailExistsException(String email, Throwable cause) {
        this(email);
        initCause(cause);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
