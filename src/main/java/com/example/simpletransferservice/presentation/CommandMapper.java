package com.example.simpletransferservice.presentation;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.presentation.dto.TransferRequest;
import com.example.simpletransferservice.presentation.dto.TransferResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CommandMapper {

    public CommandMapper() {}

    public TransferCommand toCommand(TransferRequest request) {
        return TransferCommand.builder()
                .payerId(request.getPayerId())
                .payeeId(request.getPayeeId())
                .amount(BigDecimal.valueOf(request.getAmount()))
                .build();
    }

    public TransferRequest toRequest(TransferCommand command) {
        return TransferRequest.builder()
                .payerId(command.getPayerId())
                .payeeId(command.getPayeeId())
                .amount(command.getAmount().doubleValue())
                .build();
    }

// TODO implement result
    public TransferResponse toResponse(TransferResult result) {
        return TransferResponse.builder().build();
    }
}
