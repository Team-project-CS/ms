package com.gp.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    static final String queueName = "com.gp.rabbit";

    @Bean
    Queue single_queue() {
        return new Queue(queueName, false);
    }
}
