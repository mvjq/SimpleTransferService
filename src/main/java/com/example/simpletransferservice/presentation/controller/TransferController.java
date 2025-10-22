package com.example.simpletransferservice.presentation.controller;


import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.application.port.in.TransferUseCase;
import com.example.simpletransferservice.presentation.CommandMapper;
import com.example.simpletransferservice.presentation.dto.TransferRequest;
import com.example.simpletransferservice.presentation.dto.TransferResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferUseCase transferUseCase;
    private final CommandMapper commandMapper;

    public TransferController(TransferUseCase transferUseCase, CommandMapper commandMapper) {
        this.transferUseCase = transferUseCase;
        this.commandMapper = commandMapper;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest transferRequest) {

        log.info("Received transfer request: {}", transferRequest);

        TransferCommand transferCommand = commandMapper.toCommand(transferRequest);
        TransferResult result = transferUseCase.transfer(transferCommand);

        log.info("Transfer result: {}", result);

        TransferResponse transferResponse = commandMapper.toResponse(result);

        HttpStatus status = result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(transferResponse);
    }
}
