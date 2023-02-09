package com.gp.q.service;

import com.gp.q.model.dto.QueuePropertyDto;

import java.util.List;

public interface QueueManagementService {
    List<QueuePropertyDto> createQueues(List<QueuePropertyDto> queues);

    List<QueuePropertyDto> getQueues();
}
