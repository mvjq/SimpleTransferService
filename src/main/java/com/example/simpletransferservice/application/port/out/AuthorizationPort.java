package com.example.simpletransferservice.application.port.out;

import com.example.simpletransferservice.application.command.TransferCommand;

import java.util.concurrent.CompletableFuture;

public interface AuthorizationPort {
    boolean authorize(TransferCommand command);
}
