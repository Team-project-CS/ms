package com.gp;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
public class QueueBuilderController {

    @PostMapping("api/rabbit")
    public ResponseEntity<Object> build(@Valid QueueInfo queueInfo, Errors errors) {
        if (errors.hasErrors()) {
            log.error(queueInfo.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        ConnectionFactory factory = new CachingConnectionFactory();
        try (Connection connection = factory.createConnection(); Channel channel = connection.createChannel(false)) {
            channel.queueDeclare(queueInfo.getQueueName(), false, false, false, null);
            log.info("create '{}' queue", queueInfo.getQueueName());
        } catch (IOException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(queueInfo);
    }
}
