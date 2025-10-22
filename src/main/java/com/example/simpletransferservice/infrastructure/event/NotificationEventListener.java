package com.example.simpletransferservice.infrastructure.event;

import com.example.simpletransferservice.domain.event.TransactionCompletedEvent;
import com.example.simpletransferservice.domain.port.out.NotificationPort;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationPort notificationPort;

    @Async
    @EventListener
    public void handleTransactionCompletedEvent(TransactionCompletedEvent event) {
        log.info("Received transaction completed event: {}", event);
        try {
            notificationPort.notifyTransferToClient(event);
            log.info("Successfully notified transaction completed event: {}", event);
        } catch (Exception exception){
            log.error("Error occurred while processing transaction event {} completed event", event.toString(), exception);
        }

    }
}
