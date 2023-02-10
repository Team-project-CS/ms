package com.gp.q.service;

import com.gp.q.model.dto.QueueMessageDto;

public interface QueueService {
    QueueMessageDto pushInQueue(QueueMessageDto queueMessageDto);

    QueueMessageDto popFromQueue(String id);
}
