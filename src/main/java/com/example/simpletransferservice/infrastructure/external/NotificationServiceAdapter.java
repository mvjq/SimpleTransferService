package com.example.simpletransferservice.infrastructure.external;

import com.example.simpletransferservice.domain.event.TransactionCompletedEvent;
import com.example.simpletransferservice.domain.port.out.NotificationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class NotificationServiceAdapter implements NotificationPort {

    private final RestClient restClient;
    public NotificationServiceAdapter(
            RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://util.devi.tools/api/v1/notify")
                .build();
    }


    // TODO needs to implement circuit break && retry???
    @Override
    public void notifyTransferToClient(TransactionCompletedEvent transactionCompletedEvent) {
        log.info("Sending notification for transaction: {}", transactionCompletedEvent);

        try {
            postNotification(transactionCompletedEvent.getPayeeEmail(), "Your transfer of " + transactionCompletedEvent.getValue() + " has been completed.");
            postNotification(transactionCompletedEvent.getPayerEmail(), "Your transfer of " + transactionCompletedEvent.getValue() + " has been completed.");
        } catch (Exception e) {
            log.error("Failed to send notification for transaction: {}", transactionCompletedEvent, e);
        }
    }

    private void postNotification(String email, String message) {
        var response = restClient.post()
                .retrieve();
    }
}
