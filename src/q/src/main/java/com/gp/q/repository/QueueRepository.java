package com.gp.q.repository;

import com.gp.q.model.entity.QueueMessageEntity;

public interface QueueRepository /*extends CrudRepository<QueueMessageEntity, UUID>*/ {

    QueueMessageEntity push(QueueMessageEntity entity);

    QueueMessageEntity pop(String queueName);

}
