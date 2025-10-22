package com.example.simpletransferservice.application.service;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.application.port.in.TransferUseCase;
import com.example.simpletransferservice.application.port.out.AuthorizationPort;
import com.example.simpletransferservice.application.port.out.TransactionRepositoryPort;
import com.example.simpletransferservice.application.port.out.UserRepositoryPort;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.exception.UserNotFoundException;
import com.example.simpletransferservice.domain.model.Transaction;
import com.example.simpletransferservice.domain.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class TransferService implements TransferUseCase {

    private final UserRepositoryPort userRepository;
    private final UserRepositoryPort walletRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final AuthorizationPort authorizationPort;
    private final DomainMapper domainMapper;

    public TransferService(UserRepositoryPort userRepository, UserRepositoryPort walletRepository, UserRepositoryPort transactionRepository, TransactionRepositoryPort transactionRepository1, AuthorizationPort authorizationPort, DomainMapper domainMapper) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository1;
        this.authorizationPort = authorizationPort;
        this.domainMapper = domainMapper;
    }

    @Override
    @Transactional
    public TransferResult transfer(TransferCommand command) {

        // create transaction
        // convert TransferCommand to Transaction Domain
        // created with pending
        Transaction transaction = domainMapper.toDomain(command);
        // save transaction as pending

        boolean authorized = authorizationPort.authorize(command);
        if (!authorized) {
            // move transaction to failed authorization
            // add failure reason
            // end flow
            transaction.failedAuthorization("Failed authorization");
            return new TransferResult();
        }

        transaction.authorize();
        transactionRepository.save(transaction);

        try {
            User payer = userRepository.findById(command.getPayerId())
                    .orElseThrow(() -> new UserNotFoundException("Payer not found"));
            User payee = userRepository.findById(command.getPayeeId())
                    .orElseThrow(() -> new UserNotFoundException("Payer not found"));


            // validation
//             if (payer.canTransfer() && payer.)
            if (payer.canTransfer() && )

            // debit payer wallet
            // credit payee wallet
        } catch (Exception e) {
            // move transaction to failed processing
            // add failure reason
            if (transaction.getStatus().isFinal()) {
                transaction.reverse();
            } else {
                transaction.failedValidation("Failed processing: " + e.getMessage());
            }

            // TODO: implement transferResult
            return new TransferResult();
        } finally {
            transactionRepository.save(transaction);
        }

        // now i state validating
        // apply all the business rules
        // if any rule fails
        // move transaction to failed validation
        // add failure reason
        // end flow


        // the checking for the authorization code needs to be "blocked"
        // i cannot continue a transaction without authorization


        // needs to checck the autheitcation service
        // needs to get users rom the repositroy
        // check if the wallt has enough balance
        // debit the payer wallet
        // credit the payee wallet
        // save the transaction
        // return the result
        // notify event (fire and forget (using listenner/observer)


        return null;
    }
}
