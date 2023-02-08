package com.gp.q.service;

import com.gp.q.model.dto.QueueMessageDto;

import java.util.UUID;

public interface QueueService {
    QueueMessageDto createQueue(QueueMessageDto queueMessageDto);

    QueueMessageDto getQueue(UUID id);
}
