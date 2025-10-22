package com.example.simpletransferservice.domain.port.out;

import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.domain.event.TransactionCompletedEvent;

public interface NotificationPort {
    void notifyTransferToClient(TransactionCompletedEvent transactionCompletedEvent);
}
