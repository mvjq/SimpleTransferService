package com.example.simpletransferservice.domain.port.out;

import com.example.simpletransferservice.application.command.TransferCommand;

public interface AuthorizationPort {
    boolean authorize(TransferCommand command);
}
