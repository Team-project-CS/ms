package com.gp.q.service.impl;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.repository.QueueRepository;
import com.gp.q.service.QueueService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QueueMessageDto createQueue(QueueMessageDto dto) {
        QueueMessageEntity entity = modelMapper.map(dto, QueueMessageEntity.class);
        QueueMessageEntity saved = queueRepository.push(entity);
        return modelMapper.map(saved, QueueMessageDto.class);
    }

    @Override
    public QueueMessageDto getQueue(String queueName) {
        QueueMessageEntity entity = queueRepository.pop(queueName);
        return modelMapper.map(entity, QueueMessageDto.class);
    }
}
