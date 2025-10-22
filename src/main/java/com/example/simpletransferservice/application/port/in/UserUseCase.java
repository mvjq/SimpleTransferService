package com.example.simpletransferservice.application.port.in;

import com.example.simpletransferservice.application.command.UserCommand;

public interface UserUseCase {
    UserResult createUserWithWallet(UserCommand userCommand);
    UserResult getUserByEmail(String email);;
    UserResult getUserByDocumentNumber(String documentNumber);
}
