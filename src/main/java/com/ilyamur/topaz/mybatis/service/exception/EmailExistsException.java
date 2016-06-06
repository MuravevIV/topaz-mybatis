package com.ilyamur.topaz.mybatis.service.exception;

public class EmailExistsException extends Exception {

    private String message;

    public EmailExistsException(String email) {
        this.message = String.format("Email '%s' already exists in database.", email);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
