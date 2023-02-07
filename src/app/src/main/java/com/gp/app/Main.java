package com.gp.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.gp")
@EnableJpaRepositories("com.gp")
@EntityScan(basePackages = "com.gp")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}