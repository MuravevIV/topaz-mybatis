package com.ilyamur.topaz.mybatis.service.exception;

public class EmailExistsException extends Exception {

    public static final String MESSAGE = "Email '%s' already exists in database.";

    private String message;

    public EmailExistsException(String email) {
        this.message = String.format(MESSAGE, email);
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
