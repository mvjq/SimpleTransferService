package com.example.simpletransferservice.application.service;

import com.example.simpletransferservice.application.command.UserCommand;
import com.example.simpletransferservice.application.port.in.UserResult;
import com.example.simpletransferservice.application.port.in.UserUseCase;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.exception.UserAlreadyExistsException;
import com.example.simpletransferservice.domain.model.User;
import com.example.simpletransferservice.domain.model.Wallet;
import com.example.simpletransferservice.domain.port.out.UserRepositoryPort;
import com.example.simpletransferservice.domain.port.out.WalletRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepository;
    private final WalletRepositoryPort walletRepository;
    private final DomainMapper domainMapper;

    public UserService(UserRepositoryPort userRepository, WalletRepositoryPort walletRepository, DomainMapper domainMapper) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.domainMapper = domainMapper;
    }

    @Override
    @Transactional
    public UserResult createUserWithWallet(UserCommand userCommand) {

        validateCommand(userCommand);

        try {
            User user = User.builder()
                    .fullName(userCommand.getFullName())
                    .email(userCommand.getEmail())
                    .document(userCommand.getDocumentNumber())
                    .password(userCommand.getPassword())
                    .userType(userCommand.getUserType())
                    .build();

            User savedUser = userRepository.save(user);

            Wallet wallet = Wallet.builder()
                    .userId(savedUser.getId())
                    .balance(userCommand.getInitialBalance())
                    .build();

            Wallet savedWallet = walletRepository.save(wallet);
            log.info("Wallet created with ID: {} for user: {}", savedWallet.getId(), savedUser.getId());

            savedUser.setWalletId(savedWallet.getId());
            User updatedUser = userRepository.save(savedUser);

            log.info("User creation completed successfully. UserID: {}, WalletID: {}",
                    updatedUser.getId(), savedWallet.getId());

            return UserResult.builder()
                    .id(updatedUser.getId())
                    .email(updatedUser.getEmail())
                    .fullName(updatedUser.getFullName())
                    .documentNumber(updatedUser.getDocument())
                    .balance(wallet.getBalance())
                    .userType(updatedUser.getUserType())
                    .build();


        } catch (Exception ex) {
            log.error("Error occurred while creating user: {}", userCommand);
            throw ex;
        }
    }

    private void validateCommand(UserCommand command) {
        if (command.getInitialBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        if (userRepository.existsByEmailOrDocument(command.getEmail(), command.getDocumentNumber())) {
            throw new UserAlreadyExistsException("User with email already exists");
        }

    }

    @Override
    public UserResult getUserByEmail(String email) {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        return findWalletAndReturnResult(foundUser);
    }

    @Override
    public UserResult getUserByDocumentNumber(String documentNumber) {
        User foundUser = userRepository.findByDocument(documentNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + documentNumber));

        return findWalletAndReturnResult(foundUser);

    }

    private UserResult findWalletAndReturnResult(User foundUser) {
        Wallet wallet = walletRepository.findByUserId(foundUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user ID: " + foundUser.getId()));

        return UserResult.builder()
                .id(foundUser.getId())
                .email(foundUser.getEmail())
                .fullName(foundUser.getFullName())
                .balance(wallet.getBalance())
                .documentNumber(foundUser.getDocument())
                .userType(foundUser.getUserType())
                .build();
    }
}
