package com.example.simpletransferservice;

import org.springframework.boot.SpringApplication;

public class TestSimpleTransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(SimpleTransferServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
