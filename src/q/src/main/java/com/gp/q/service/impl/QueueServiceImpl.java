package com.gp.q.service.impl;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.repository.QueueCrudRepository;
import com.gp.q.repository.QueueRepository;
import com.gp.q.service.QueueService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private QueueCrudRepository crudRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QueueMessageDto pushInQueue(QueueMessageDto dto) {
        QueueMessageEntity entity = modelMapper.map(dto, QueueMessageEntity.class);
        QueueMessageEntity saved = queueRepository.push(entity);
        crudRepository.save(entity);
        return modelMapper.map(saved, QueueMessageDto.class);
    }

    @Override
    public QueueMessageDto popFromQueue(String queueName) {
        QueueMessageEntity entity = queueRepository.pop(queueName);
        return modelMapper.map(entity, QueueMessageDto.class);
    }

    @Override
    public List<QueueMessageDto> getAllMessages(String queueName) {
        return crudRepository.findAllByName(queueName).stream()
                .map(t -> modelMapper.map(t, QueueMessageDto.class)).toList();
    }

    @Override
    public List<QueueMessageDto> getAllMessages(LocalDateTime begin, LocalDateTime end) {
        return crudRepository.findByCreationDateBetween(begin, end).stream()
                .map(t -> modelMapper.map(t, QueueMessageDto.class)).toList();
    }

    @Override
    public List<QueueMessageDto> getAllMessages(String queueName, LocalDateTime begin, LocalDateTime end) {
        return crudRepository.findByCreationDateBetweenAndName(begin, end, queueName).stream()
                .map(t -> modelMapper.map(t, QueueMessageDto.class)).toList();
    }
}
