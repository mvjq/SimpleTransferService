package com.example.simpletransferservice.presentation.controller;


import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.application.port.in.TransferUseCase;
import com.example.simpletransferservice.presentation.CommandMapper;
import com.example.simpletransferservice.presentation.dto.TransferRequest;
import com.example.simpletransferservice.presentation.dto.TransferResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private TransferUseCase transferUseCase;
    private CommandMapper commandMapper;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest transferRequest) {

        log.info("Received transfer request: {}", transferRequest);

        TransferCommand transferCommand = commandMapper.toCommand(transferRequest);
        TransferResult result = transferUseCase.transfer(transferCommand);

        log.info("Transfer result: {}", result);

        TransferResponse transferResponse = commandMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(transferResponse);
    }
}
