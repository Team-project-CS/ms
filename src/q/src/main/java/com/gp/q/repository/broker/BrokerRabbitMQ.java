package com.gp.q.repository.broker;

import com.gp.q.exception.throwables.QueueServiceException;
import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.repository.MQBroker;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public class BrokerRabbitMQ implements MQBroker {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin admin;

    public BrokerRabbitMQ(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void checkExistence(String queueName) {
        if (admin.getQueueInfo(queueName) == null) {
            throw new QueueServiceException("queue with name '" + queueName + "' not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public QueueMessageEntity push(@NotNull QueueMessageEntity entity) {
        checkExistence(entity.getName());
        // Если применяется обменник по умолчанию,
        // то сообщение будет маршрутизироваться в очередь с именем равным ключу маршрутизации сообщения.
        rabbitTemplate.convertAndSend(entity.getName(), entity.getMessage());
        return entity;
    }

    @Override
    public QueueMessageEntity pop(String queueName) {
        checkExistence(queueName);
        String message = (String) rabbitTemplate.receiveAndConvert(queueName);
        if (message == null) {
            throw new QueueServiceException("queue with name " + queueName + " is empty", HttpStatus.NOT_FOUND);
        }
        return new QueueMessageEntity(queueName, message);
    }
}
