package com.gp.q.service.impl;

import com.gp.q.exception.throwables.QueueServiceException;
import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.repository.QueueRepository;
import com.gp.q.service.QueueService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QueueMessageDto createQueue(QueueMessageDto queueMessageDto) {
        QueueMessageEntity queueMessageEntity = modelMapper.map(queueMessageDto, QueueMessageEntity.class);
        return modelMapper.map(queueRepository.save(queueMessageEntity), QueueMessageDto.class);
    }

    @Override
    @SneakyThrows
    public QueueMessageDto getQueue(UUID id) {
        Optional<QueueMessageEntity> queueById = queueRepository.findById(id);
        if (queueById.isEmpty()) {
            throw new QueueServiceException("Queue not found.", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(queueById.get(), QueueMessageDto.class);
    }
}
