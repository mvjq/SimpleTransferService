package com.example.simpletransferservice.application.service;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.application.port.in.TransferUseCase;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class TransferService implements TransferUseCase {

    @Override
    @Transactional
    public TransferResult transfer(TransferCommand command) {




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
