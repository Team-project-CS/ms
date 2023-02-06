package com.gp.q.service;

import com.gp.q.model.dto.QueueCreateParams;
import com.gp.q.model.dto.QueueDto;

import java.util.UUID;

public interface QueueService {
    void createQueue(QueueCreateParams queueCreateParams);
    QueueDto getQueue(UUID id);
}
