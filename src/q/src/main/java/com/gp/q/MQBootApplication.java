package com.gp.q;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
class MQBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MQBootApplication.class, args);
    }

}
