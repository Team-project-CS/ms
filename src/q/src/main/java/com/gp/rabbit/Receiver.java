package com.gp.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Receiver {

    @RabbitListener(queues = Config.queueName)
    public void receive(String message) {
        log.info("receive: '" + message + "'");
    }
}