package com.example.simpletransferservice.infrastructure.external;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.domain.port.out.AuthorizationPort;
import com.example.simpletransferservice.infrastructure.external.dto.AuthorizationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class AuthorizationServiceAdapter implements AuthorizationPort {

    private final RestClient restClient;

    public AuthorizationServiceAdapter(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://util.devi.tools/")
                .build();
    }

    @Override
    @CircuitBreaker(name = "authorizationServiceCircuitBreaker", fallbackMethod = "authorizeFallback")
    @Retry(name ="AuthorizationServiceRetry")
    public boolean authorize(TransferCommand command) {
        var response = restClient.get()
                .uri("/api/v2/authorize")
                .retrieve()
                .body(AuthorizationResponse.class);

        log.info("Authorization response: {} for command {}", response, command);
        if (response == null) {
            log.error("Authorization response for command {} is null", command);
            return false;
        }

        return response.getData().isAuthorization();
    }
}
