package com.example.simpletransferservice.application.service;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.application.port.in.TransferUseCase;
import org.springframework.stereotype.Component;

@Component
public class TransferService implements TransferUseCase {
    @Override
    public TransferResult transfer(TransferCommand command) {
        return null;
    }
}
