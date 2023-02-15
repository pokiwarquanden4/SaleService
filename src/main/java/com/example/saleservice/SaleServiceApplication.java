package com.example.saleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
public class SaleServiceApplication {
    private static final Logger logger = LogManager.getLogger(SaleServiceApplication.class);

    public static void main(String[] args) {
        logger.debug("hello");
        SpringApplication.run(SaleServiceApplication.class, args);
    }

}
