package com.example.simpletransferservice.infrastructure.external;

import com.example.simpletransferservice.domain.event.TransactionCompletedEvent;
import com.example.simpletransferservice.domain.port.out.NotificationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceAdapter implements NotificationPort {
    @Override
    public void notifyTransferToClient(TransactionCompletedEvent transactionCompletedEvent) {

    }
}
