package com.example.simpletransferservice.application.service;

import com.example.simpletransferservice.application.port.in.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.application.port.in.TransferUseCase;

public class TransferService implements TransferUseCase {
    @Override
    public TransferResult transfer(TransferCommand command) {
        return null;
    }
}
