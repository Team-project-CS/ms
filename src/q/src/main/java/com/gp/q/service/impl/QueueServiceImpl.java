package com.gp.q.service.impl;

import com.gp.q.model.QueueMessageDirection;
import com.gp.q.model.dto.QueueLogDto;
import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.dto.QueueMessagePeriodDto;
import com.gp.q.model.entity.QueueLogEntity;
import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.repository.MQBroker;
import com.gp.q.repository.QueueLogRepository;
import com.gp.q.service.QueueService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private MQBroker broker;

    @Autowired
    private QueueLogRepository logRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QueueMessageDto pushInQueue(QueueMessageDto dto) {
        QueueMessageEntity entity = modelMapper.map(dto, QueueMessageEntity.class);
        QueueMessageEntity saved = broker.push(entity);
        // Сохраняет в лог реальное время вставки, а не переданное извне
        logRepository.save(new QueueLogEntity(entity.getName(), entity.getMessage(), LocalDateTime.now(), QueueMessageDirection.IN));
        return modelMapper.map(saved, QueueMessageDto.class);
    }

    @Override
    public QueueMessageDto popFromQueue(String queueName) {
        QueueMessageEntity entity = broker.pop(queueName);
        // Сохраняет в лог реальное время чтения, а не переданное извне
        logRepository.save(new QueueLogEntity(entity.getName(), entity.getMessage(), LocalDateTime.now(), QueueMessageDirection.OUT));
        return modelMapper.map(entity, QueueMessageDto.class);
    }

    @Override
    public List<QueueLogDto> getAllMessages(String queueName) {
        return logRepository.findAllByName(queueName).stream()
                .map(t -> modelMapper.map(t, QueueLogDto.class)).toList();
    }

    @Override
    public List<QueueLogDto> getAllMessages(LocalDateTime begin, LocalDateTime end) {
        return logRepository.findByCreationDateBetween(begin, end).stream()
                .map(t -> modelMapper.map(t, QueueLogDto.class)).toList();
    }

    @Override
    public List<QueueLogDto> getAllMessages(QueueMessagePeriodDto dto) {
        return logRepository.findByCreationDateBetweenAndName(dto.getStartDate(), dto.getEndDate(), dto.getQueueName()).stream()
                .map(t -> modelMapper.map(t, QueueLogDto.class)).toList();
    }
}
