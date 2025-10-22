package com.example.simpletransferservice.presentation;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.command.UserCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.application.port.in.UserResult;
import com.example.simpletransferservice.domain.enums.UserType;
import com.example.simpletransferservice.presentation.dto.TransferRequest;
import com.example.simpletransferservice.presentation.dto.TransferResponse;
import com.example.simpletransferservice.presentation.dto.UserRequest;
import com.example.simpletransferservice.presentation.dto.UserResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CommandMapper {

    public CommandMapper() {}

    public TransferCommand toCommand(TransferRequest request) {
        return TransferCommand.builder()
                .payerId(request.getPayerId())
                .payeeId(request.getPayeeId())
                .amount(request.getAmount())
                .build();
    }

    public TransferRequest toRequest(TransferCommand command) {
        return TransferRequest.builder()
                .payerId(command.getPayerId())
                .payeeId(command.getPayeeId())
                .amount(BigDecimal.valueOf(command.getAmount().doubleValue()))
                .build();
    }

    public TransferResponse toResponse(TransferResult result) {
        return TransferResponse.builder()
                .payerId(result.getPayerId())
                .payeeId(result.getPayeeId())
                .amount(result.getAmount())
                .status(result.getStatus())
                .message(result.getMessage())
                .build();
    }

    public UserCommand toCommand(UserRequest userRequest) {
        return UserCommand.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .documentNumber(userRequest.getDocumentNumber())
                .initialBalance(userRequest.getInitialBalance())
                .userType(UserType.valueOf(userRequest.getUserType()))
                .build();
    }

    public UserResponse toResponse(UserResult userResult) {
        return UserResponse.builder()
                .fullName(userResult.getFullName())
                .email(userResult.getEmail())
                .password(userResult.getPassword())
                .documentNumber(userResult.getDocumentNumber())
                .initialBalance(userResult.getBalance())
                .userType(userResult.getUserType().name())
                .build();
    }
}
