package com.gp.q.repository;

import com.gp.q.model.entity.QueueMessageEntity;

/**
 * Интерфейс для взаимодействия с MQ
 */
public interface MQBroker {

    QueueMessageEntity push(QueueMessageEntity entity);

    QueueMessageEntity pop(String queueName);

}
