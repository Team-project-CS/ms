package com.gp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
class AppSpringBoot extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AppSpringBoot.class, args);
    }

}