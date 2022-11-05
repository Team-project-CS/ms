package com.gp.rabbit;

import com.gp.QueueInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping()
public class Controller {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @GetMapping()
//    public void send() {
//        String message = "hello world";
//        rabbitTemplate.convertAndSend(Config.queueName, message);
//        log.info("send: '" + message + "'");
//    }


    @PutMapping("api/rabbit")
    public void send(@Valid QueueInfo queueInfo, @RequestParam String message) {
//        String message = "hello world";
        rabbitTemplate.convertAndSend(queueInfo.getQueueName(), message);
        log.info("send: '{}' in '{}' queue", message, queueInfo.getQueueName());
    }

    @GetMapping("api/rabbit")
    public void receive(@Valid QueueInfo queueInfo) {
        Object o = rabbitTemplate.receiveAndConvert(queueInfo.getQueueName());
        log.info("receive: '{}'", o);
    }
}
