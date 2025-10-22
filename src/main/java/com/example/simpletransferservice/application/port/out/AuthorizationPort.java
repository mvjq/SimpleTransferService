package com.example.simpletransferservice.application.port.out;

import com.example.simpletransferservice.application.command.TransferCommand;

public interface AuthorizationPort {
    boolean authorize(TransferCommand command);
}
