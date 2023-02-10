package com.gp.q.repository;

import com.gp.q.exception.throwables.QueueServiceException;
import com.gp.q.model.entity.QueueMessageEntity;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class QueueRepositoryRabbitMQ implements QueueRepository {

    private final RabbitTemplate rabbitTemplate;

    public QueueRepositoryRabbitMQ(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public QueueMessageEntity push(QueueMessageEntity entity) {
//        rabbitTemplate.convertAndSend(entity.getName(), "", entity.getMessage());
        rabbitTemplate.convertAndSend(entity.getName(), entity.getMessage());
        return entity;
    }

    @Override
    @SneakyThrows
    public QueueMessageEntity pop(String queueName) {

        String message = (String) rabbitTemplate.receiveAndConvert(queueName);
        if (message == null) {
            throw new QueueServiceException("queue with name " + queueName + " is empty or not exist");
        }
        return new QueueMessageEntity(queueName, message);
    }
}
