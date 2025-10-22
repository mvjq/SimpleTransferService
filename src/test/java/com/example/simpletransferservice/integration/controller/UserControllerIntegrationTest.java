package com.example.simpletransferservice.integration.controller;

import com.example.simpletransferservice.IntegrationTestBase;
import com.example.simpletransferservice.domain.enums.UserType;
import com.example.simpletransferservice.presentation.dto.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerIntegrationTest extends IntegrationTestBase {

    public static final String DOCUMENT_NUMBER = "39293399865";
    public static final String MAIL = "mviniciusjunqueira@gmail.com";
    public static final String NAME = "Marcos Vinicius Junqueira";
    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/users";

    @Test
    @DisplayName("Should create user type CUSTOMER")
    void shouldCreateCustomerUserSuccessfully() throws Exception {
        UserRequest request = UserRequest.builder()
                .fullName(NAME)
                .documentNumber(DOCUMENT_NUMBER)
                .email(MAIL)
                .password("1234")
                .userType(UserType.CUSTOMER.toString())
                .initialBalance(new BigDecimal("1000.00"))
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.fullName").value(NAME))
                    .andExpect(jsonPath("$.email").value(MAIL))
                    .andExpect(jsonPath("$.documentNumber").value(DOCUMENT_NUMBER))
                    .andExpect(jsonPath("$.userType").value("CUSTOMER"))
                    .andExpect(jsonPath("$.initialBalance").value(1000.00));
    }

    @Test
    @DisplayName("Should return 400 when email already exists")
    void shouldReturn400WhenEmailExists() throws Exception {
        UserRequest firstUser = createMockRequest(MAIL, "39293399865");
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isCreated());

        UserRequest duplicateEmail = createMockRequest(MAIL, DOCUMENT_NUMBER);

        // Then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateEmail)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    @DisplayName("Should return NOT FOUND when email dont exist in database")
    void shouldReturn404WhenUserNotFoundByEmail() throws Exception {
        mockMvc.perform(get(BASE_URL + "/email")
                        .param("email", "qualquer@email.com"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("not found")));
    }


    private UserRequest createMockRequest(String email, String document) {
        return UserRequest.builder()
                .fullName(NAME)
                .documentNumber(document)
                .email(email)
                .password("1234")
                .userType(UserType.CUSTOMER.toString())
                .initialBalance(new BigDecimal("1000.00"))
                .build();
    }

}