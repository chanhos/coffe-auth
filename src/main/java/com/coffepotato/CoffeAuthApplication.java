package com.coffepotato;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;


@Slf4j
@SpringBootApplication
@EnableAsync
@EnableGlobalAuthentication
public class CoffeAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeAuthApplication.class, args);
    }

}
