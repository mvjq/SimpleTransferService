package com.example.simpletransferservice.domain.exception;

public class WalleNotFoundException extends RuntimeException {
    public WalleNotFoundException(String message) {
        super(message);
    }
}
