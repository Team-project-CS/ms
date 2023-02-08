package com.gp.q.service.impl;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.service.QueueService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;

@Service
public class QueueServiceImpl implements QueueService {

//    @Autowired
//    private QueueRepository queueRepository;

    @Autowired
    private ModelMapper modelMapper;

    Deque<QueueMessageEntity> queue = new ArrayDeque<>();

    @Override
    public QueueMessageDto createQueue(QueueMessageDto dto) {
        QueueMessageEntity queueMessageEntity = modelMapper.map(dto, QueueMessageEntity.class);
        queue.addLast(queueMessageEntity);
//        QueueMessageEntity saved = queueRepository.save(queueMessageEntity);
        return modelMapper.map(queueMessageEntity, QueueMessageDto.class);
    }

    @Override
    @SneakyThrows
    public QueueMessageDto getQueue(String queueName) {
        QueueMessageEntity entity = queue.pollFirst();
        return modelMapper.map(entity, QueueMessageDto.class);
    }
}
