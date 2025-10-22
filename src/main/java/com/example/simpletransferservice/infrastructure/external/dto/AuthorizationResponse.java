package com.example.simpletransferservice.infrastructure.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationResponse {

    private String status;
    private AuthorizationData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorizationData {
        private boolean authorization;
    }
}
