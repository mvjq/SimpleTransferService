package com.example.simpletransferservice;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
public abstract class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // db cleanup
    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM transactions");
        jdbcTemplate.execute("DELETE FROM wallet");
        jdbcTemplate.execute("DELETE FROM users");

        jdbcTemplate.execute("ALTER SEQUENCE users_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE wallet_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE transactions_id_seq RESTART WITH 1");
    }
}