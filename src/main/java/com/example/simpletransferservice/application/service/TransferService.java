package com.example.simpletransferservice.application.service;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.application.port.in.TransferUseCase;
import com.example.simpletransferservice.domain.exception.InsufficientBalanceException;
import com.example.simpletransferservice.domain.port.out.AuthorizationPort;
import com.example.simpletransferservice.domain.port.out.TransactionRepositoryPort;
import com.example.simpletransferservice.domain.port.out.UserRepositoryPort;
import com.example.simpletransferservice.domain.port.out.WalletRepositoryPort;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.exception.UserNotFoundException;
import com.example.simpletransferservice.domain.model.Transaction;
import com.example.simpletransferservice.domain.model.User;
import com.example.simpletransferservice.domain.model.Wallet;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class TransferService implements TransferUseCase {

    private final UserRepositoryPort userRepository;
    private final WalletRepositoryPort walletRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final AuthorizationPort authorizationPort;
    private final DomainMapper domainMapper;

    public TransferService(UserRepositoryPort userRepository, WalletRepositoryPort walletRepository, TransactionRepositoryPort transactionRepository, AuthorizationPort authorizationPort, DomainMapper domainMapper) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.authorizationPort = authorizationPort;
        this.domainMapper = domainMapper;
    }

    @Override
    @Transactional
    public TransferResult transfer(TransferCommand command) {

        log.info("Starting transfer from user {} to user {} amount {}",
                command.getPayerId(), command.getPayeeId(), command.getAmount());

        if (command.getPayeeId().equals(command.getPayerId())) {
            throw new IllegalArgumentException("Payee IDs must be different from payee IDs");
        }

        Transaction transaction = domainMapper.toDomain(command);

        User payer = userRepository.findById(command.getPayerId()).orElseThrow(
                () -> new UserNotFoundException("Payer not found"));
        User payee = userRepository.findById(command.getPayeeId()).orElseThrow(
                () -> new UserNotFoundException("Payee not found"));

        if (!payer.canTransfer()) {
            throw new IllegalArgumentException("Payer is not allowed to make transfers (type MERCHANT)");
        }

        Wallet payerWallet = walletRepository.findByUserIdWithPessimisticLocking(payer.getId())
                .orElseThrow(() -> new UserNotFoundException("Payer wallet not found"));
        Wallet payeeWallet = walletRepository.findByUserIdWithPessimisticLocking(payee.getId())
                .orElseThrow(() -> new UserNotFoundException("Payee wallet not found"));

        if (payerWallet.getBalance().compareTo(command.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Available: %s, Required: %s",
                            payerWallet.getBalance(), command.getAmount())
            );
        }

        try {
            boolean authorized = authorizationPort.authorize(command);
            if (!authorized) {
                transaction.failedAuthorization("Failed authorization");
                return getTransferResult(command, "Transfer not authorized", transaction, payee, payer, false);
            }
            transaction.process();
            transactionRepository.save(transaction);

            var newPayerWallet = payerWallet.debit(command.getAmount());
            var newPayeeWallet = payeeWallet.credit(command.getAmount());

            walletRepository.save(newPayerWallet);
            walletRepository.save(newPayeeWallet);

            transaction.complete();
            transactionRepository.save(transaction);
            log.info("Transfer authorized for transaction id {}", transaction.getId());

            return getTransferResult(command, "Transfer completed successfully", transaction, payee, payer, true);

        } catch (Exception e) {
            log.error("Transfer failed {}", e.getMessage(), e);

            if (transaction.getStatus().isCompleted()) {
                transaction.reverse(e.getMessage());
                revertWallet(payerWallet, payeeWallet, command.getAmount());
            } else {
                transaction.failProcessing(e.getMessage());
            }

            transactionRepository.save(transaction);

            return getTransferResult(command, e.getMessage(), transaction, payee, payer, false);
        }
    }

    private static TransferResult getTransferResult(TransferCommand command, String message,
                                                    Transaction transaction, User payee, User payer, Boolean success) {
        return TransferResult.builder()
                .transactionId(transaction.getId())
                .payeeId(payee.getId())
                .payerId(payer.getId())
                .message(message)
                .status(transaction.getStatus().toString())
                .amount(command.getAmount())
                .success(success)
                .build();
    }

    private void revertWallet(Wallet payerWallet, Wallet payeeWallet, BigDecimal amount) {
        var newPayerWallet = payerWallet.credit(amount);
        var newPayeeWallet = payeeWallet.debit(amount);

        walletRepository.save(newPayerWallet);
        walletRepository.save(newPayeeWallet);
    }
}
