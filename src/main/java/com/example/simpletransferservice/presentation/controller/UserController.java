package com.example.simpletransferservice.presentation.controller;

import com.example.simpletransferservice.application.command.UserCommand;
import com.example.simpletransferservice.application.port.in.UserResult;
import com.example.simpletransferservice.application.port.in.UserUseCase;
import com.example.simpletransferservice.presentation.CommandMapper;
import com.example.simpletransferservice.presentation.dto.UserRequest;
import com.example.simpletransferservice.presentation.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserUseCase userUsecase;
    private final CommandMapper commandMapper;

    public UserController(UserUseCase userUsecase, CommandMapper commandMapper) {
        this.userUsecase = userUsecase;
        this.commandMapper = commandMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Creating user: {}", userRequest);

        UserCommand userCommand = commandMapper.toCommand(userRequest);
        UserResult userResult = userUsecase.createUserWithWallet(userCommand);

        log.error("Successfully created user: {}", userCommand);
        UserResponse response = commandMapper.toResponse(userResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        log.info("Fetching user by email: {}", email);

        UserResult userResult = userUsecase.getUserByEmail(email);
        UserResponse response = commandMapper.toResponse(userResult);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/document")
    public ResponseEntity<UserResponse> getUserByDocumentNumber(@RequestParam String documentNumber) {
        log.info("Fetching user by document number: {}", documentNumber);

        UserResult userResult = userUsecase.getUserByDocumentNumber(documentNumber);
        UserResponse response = commandMapper.toResponse(userResult);

        return ResponseEntity.ok(response);
    }

}

