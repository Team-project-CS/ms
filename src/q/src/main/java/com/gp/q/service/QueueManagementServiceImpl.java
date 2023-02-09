package com.gp.q.service;

import com.gp.q.model.dto.QueuePropertyDto;
import com.gp.q.model.entity.QueuePropertyEntity;
import com.gp.q.repository.QueuePropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueueManagementServiceImpl implements QueueManagementService {

    @Autowired
    private QueuePropertyRepository db;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AmqpAdmin admin;


    @Override
    public List<QueuePropertyDto> createQueues(List<QueuePropertyDto> queues) {
        for (var i : queues) {
            if (admin.getQueueInfo(i.getQueueName()) == null) {
                admin.declareQueue(new Queue(i.getQueueName(), false));
                db.save(modelMapper.map(i, QueuePropertyEntity.class));
            }
        }
        return queues;
    }

    @Override
    public List<QueuePropertyDto> getQueues() {
        List<QueuePropertyDto> list = new ArrayList<>();
        db.findAll().forEach(q -> list.add(new QueuePropertyDto(q.getQueueName(), q.getCreator())));
        return list;
    }

    @Override
    public List<QueuePropertyDto> deleteQueue(String queueName) {
        admin.deleteQueue(queueName);
        db.deleteById(queueName);
        return getQueues();
    }
}
