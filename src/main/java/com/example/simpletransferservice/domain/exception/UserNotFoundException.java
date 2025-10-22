package com.example.simpletransferservice.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public
    UserNotFoundException(String message) {
        super(message);
    }
}
