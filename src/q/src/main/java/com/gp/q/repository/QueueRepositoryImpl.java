package com.gp.q.repository;

import com.gp.q.exception.throwables.QueueServiceException;
import com.gp.q.model.entity.QueueMessageEntity;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@Repository
public class QueueRepositoryImpl implements QueueRepository {

    private final Map<String, Deque<QueueMessageEntity>> queues = new HashMap<>();

    @Override
    public QueueMessageEntity push(QueueMessageEntity entity) {
        if (queues.get(entity.getName()) == null) {
            queues.put(entity.getName(), new ArrayDeque<>());
        }
        queues.get(entity.getName()).addLast(entity);
        return entity;
    }

    @Override
    @SneakyThrows
    public QueueMessageEntity pop(String queueName) {
        if (queues.get(queueName) == null || queues.get(queueName).isEmpty()) {
            throw new QueueServiceException("queue with name " + queueName + " is empty or not exist");
        }
        return queues.get(queueName).pollFirst();
    }
}
