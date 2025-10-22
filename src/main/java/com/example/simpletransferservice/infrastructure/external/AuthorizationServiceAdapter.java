package com.example.simpletransferservice.infrastructure.external;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.out.AuthorizationPort;
import com.example.simpletransferservice.infrastructure.external.dto.AuthorizationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class AuthorizationServiceAdapter implements AuthorizationPort {

    private final RestClient restClient;

    //TODO defines this url in a value
    public AuthorizationServiceAdapter(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://util.devi.tools/")
                .build();
    }

    @Override
    public boolean authorize(TransferCommand command) {
        var response = restClient.get()
                .uri("/api/v2/authorize")
                .retrieve()
                .body(AuthorizationResponse.class);

        log.info("Authorization response: {} for command {}", response, command);
        return response.getData().isAuthorization();
    }
}
