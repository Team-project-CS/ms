package com.gp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
class ApiBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApiBootApplication.class, args);
    }

}