package com.example.simpletransferservice.application.port.in;

import com.example.simpletransferservice.application.command.TransferCommand;

public interface TransferUseCase {
    TransferResult transfer(TransferCommand command);
}
