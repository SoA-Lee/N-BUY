package com.dalgorithm.nbuy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class NBuyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NBuyApplication.class, args);
    }

}
