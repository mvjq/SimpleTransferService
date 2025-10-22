package com.example.simpletransferservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SimpleTransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleTransferServiceApplication.class, args);
    }

}
