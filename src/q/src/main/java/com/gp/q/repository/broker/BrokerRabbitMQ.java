package com.gp.q.repository.broker;

import com.gp.q.exception.throwables.QueueServiceException;
import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.repository.MQBroker;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class BrokerRabbitMQ implements MQBroker {

    private final RabbitTemplate rabbitTemplate;

    public BrokerRabbitMQ(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public QueueMessageEntity push(QueueMessageEntity entity) {
        // Если применяется обменник по умолчанию,
        // то сообщение будет маршрутизироваться в очередь с именем равным ключу маршрутизации сообщения.
        rabbitTemplate.convertAndSend(entity.getName(), entity.getMessage());
        return entity;
    }

    @Override
    @SneakyThrows
    public QueueMessageEntity pop(String queueName) {

        String message = (String) rabbitTemplate.receiveAndConvert(queueName);
        if (message == null) {
            throw new QueueServiceException("queue with name " + queueName + " is empty or not exist", HttpStatus.NOT_FOUND);
        }
        return new QueueMessageEntity(queueName, message);
    }
}