package com.example.simpletransferservice.integration.controller;

import com.example.simpletransferservice.IntegrationTestBase;
import com.example.simpletransferservice.domain.enums.UserType;
import com.example.simpletransferservice.domain.port.out.UserRepositoryPort;
import com.example.simpletransferservice.domain.port.out.WalletRepositoryPort;
import com.example.simpletransferservice.presentation.dto.TransferRequest;
import com.example.simpletransferservice.presentation.dto.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransferControllerIntegrationTest extends IntegrationTestBase {

    public static final String PAYER_EMAIL = "payer@gmail.com";
    public static final String PAYEE_EMAIL = "payee@gmail.com";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepositoryPort userRepository;

    @Autowired
    private WalletRepositoryPort walletRepository;

    private static final String TRANSFER_PATH = "/api/v1/transfers";
    private static final String USER_PATH = "/api/v1/users";

    @Test
    @DisplayName("Should transfer money successfully between two customers")
    void shouldTransferMoneySuccessfully() throws Exception {
        Long payerId = createIntegrationuser(PAYER_EMAIL, "11111111111",
                UserType.CUSTOMER, new BigDecimal("1000.00"));
        Long payeeId = createIntegrationuser(PAYEE_EMAIL, "22222222222",
                UserType.CUSTOMER, new BigDecimal("500.00"));

        TransferRequest request = TransferRequest.builder()
                .payer(payerId)
                .payee(payeeId)
                .value(new BigDecimal("150.00"))
                .build();

        mockMvc.perform(post(TRANSFER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").isNumber())
                .andExpect(jsonPath("$.payerId").value(payerId))
                .andExpect(jsonPath("$.payeeId").value(payeeId))
                .andExpect(jsonPath("$.value").value(150.00))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        var payerWallet = walletRepository.findByUserId(payerId).orElseThrow();
        var payeeWallet = walletRepository.findByUserId(payeeId).orElseThrow();

        assertThat(payerWallet.getBalance()).isEqualByComparingTo("850.00"); // 1000 - 150
        assertThat(payeeWallet.getBalance()).isEqualByComparingTo("650.00"); // 500 + 150
    }

    @Test
    @DisplayName("Should return 400 when payer has insufficient balance")
    void shouldReturn500WhenInsufficientBalance() throws Exception {

        Long payerId = createIntegrationuser(PAYER_EMAIL, "33333333333",
                UserType.CUSTOMER, new BigDecimal("50.00"));
        Long payeeId = createIntegrationuser(PAYEE_EMAIL, "44444444444",
                UserType.CUSTOMER, new BigDecimal("1000.00"));

        TransferRequest request = TransferRequest.builder()
                .payer(payerId)
                .payee(payeeId)
                .value(new BigDecimal("100.00"))
                .build();

        mockMvc.perform(post(TRANSFER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        var payerWallet = walletRepository.findByUserId(payerId).orElseThrow();
        var payeeWallet = walletRepository.findByUserId(payeeId).orElseThrow();

        assertThat(payerWallet.getBalance()).isEqualByComparingTo("50.00");
        assertThat(payeeWallet.getBalance()).isEqualByComparingTo("1000.00");
    }

    @Test
    @DisplayName("Should return error when merchant tries to transfer money")
    void shouldReturn500WhenMerchantTriesToTransfer() throws Exception {
        Long merchantId = createIntegrationuser("merchant@example.com", "55555555555",
                UserType.MERCHANT, new BigDecimal("5000.00"));
        Long customerId = createIntegrationuser("customer@example.com", "66666666666",
                UserType.CUSTOMER, new BigDecimal("100.00"));

        TransferRequest request = TransferRequest.builder()
                .payer(merchantId)
                .payee(customerId)
                .value(new BigDecimal("50.00"))
                .build();

        mockMvc.perform(post(TRANSFER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Should return 500 when payer equals payee (self-transfer)")
    void shouldReturn500WhenSelfTransfer() throws Exception {
        Long userId = createIntegrationuser("user@example.com", "77777777777",
                UserType.CUSTOMER, new BigDecimal("1000.00"));


        TransferRequest request = TransferRequest.builder()
                .payer(userId)
                .payee(userId) // Same user!
                .value(new BigDecimal("100.00"))
                .build();

        mockMvc.perform(post(TRANSFER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Should return NOT_FOUND(404) when payer dont exist")
    void shouldReturn404WhenPayerNotFound() throws Exception {
        // Given - Only payee exists
        Long payeeId = createIntegrationuser(PAYEE_EMAIL, "88888888888",
                UserType.CUSTOMER, new BigDecimal("500.00"));

        // Given - Transfer with non-existent payer
        TransferRequest request = TransferRequest.builder()
                .payer(999L) // Non-existent
                .payee(payeeId)
                .value(new BigDecimal("100.00"))
                .build();

        // When/Then
        mockMvc.perform(post(TRANSFER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return NOTFOUND(404) when payee dont exist")
    void shouldReturn404WhenPayeeNotFound() throws Exception {
        Long payerId = createIntegrationuser(PAYER_EMAIL, "99999999999",
                UserType.CUSTOMER, new BigDecimal("1000.00"));


        TransferRequest request = TransferRequest.builder()
                .payer(payerId)
                .payee(999L) // Non-existent
                .value(new BigDecimal("100.00"))
                .build();

        mockMvc.perform(post(TRANSFER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when value is zero or negative")
    void shouldReturn400WhenInvalidValue() throws Exception {
        // Given
        Long payerId = createIntegrationuser(PAYER_EMAIL, "10101010101",
                UserType.CUSTOMER, new BigDecimal("1000.00"));
        Long payeeId = createIntegrationuser(PAYEE_EMAIL, "20202020202",
                UserType.CUSTOMER, new BigDecimal("500.00"));

        TransferRequest zeroRequest = TransferRequest.builder()
                .payer(payerId)
                .payee(payeeId)
                .value(BigDecimal.ZERO)
                .build();

        mockMvc.perform(post(TRANSFER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zeroRequest)))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        TransferRequest negativeRequest = TransferRequest.builder()
                .payer(payerId)
                .payee(payeeId)
                .value(new BigDecimal("-50.00"))
                .build();

        mockMvc.perform(post(TRANSFER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(negativeRequest)))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    private Long createIntegrationuser(String email, String document, UserType userType, BigDecimal initialBalance) throws Exception {
        UserRequest request = UserRequest.builder()
                .fullName("User " + email)
                .documentNumber(document)
                .email(email)
                .password("password123")
                .userType(userType.toString())
                .initialBalance(initialBalance)
                .build();

        MvcResult result = mockMvc.perform(post(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("id").asLong();
    }
}
