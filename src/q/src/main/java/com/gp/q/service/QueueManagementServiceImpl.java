package com.gp.q.service;

import com.gp.q.model.dto.QueuePropertyDto;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueueManagementServiceImpl implements QueueManagementService {

    // Можно в бд хранить....
    private final List<QueuePropertyDto> queues = new ArrayList<>();

    @Autowired
    private AmqpAdmin admin;


    @Override
    public List<QueuePropertyDto> createQueues(List<QueuePropertyDto> queues) {
        for (var i : queues) {
            if (admin.getQueueInfo(i.getQueueName()) == null) {
                admin.declareQueue(new Queue(i.getQueueName(), false));
                this.queues.add(i);
            }
        }
        return queues;
    }

    @Override
    public List<QueuePropertyDto> getQueues() {
        return queues;
    }
}
