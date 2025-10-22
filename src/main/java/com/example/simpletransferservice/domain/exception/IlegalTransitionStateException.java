package com.example.simpletransferservice.domain.exception;

public class IlegalTransitionStateException extends RuntimeException {
    public IlegalTransitionStateException(String message) {
        super(message);
    }
}
