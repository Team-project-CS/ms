package com.gp.q.service;

import com.gp.q.model.dto.QueueMessageDto;

public interface QueueService {
    QueueMessageDto createQueue(QueueMessageDto queueMessageDto);

    QueueMessageDto getQueue(String id);
}
