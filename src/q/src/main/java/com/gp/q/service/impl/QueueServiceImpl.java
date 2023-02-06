package com.gp.q.service.impl;

import com.gp.q.exception.throwables.QueueServiceException;
import com.gp.q.model.dto.QueueCreateParams;
import com.gp.q.model.dto.QueueDto;
import com.gp.q.model.entity.QueueEntity;
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
public class QueueServiceImpl extends ModelMapper implements QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @Override
    public void createQueue(QueueCreateParams queueCreateParams) {
        QueueEntity queueEntity = this.map(queueCreateParams, QueueEntity.class);
        queueRepository.save(queueEntity);
    }

    @Override
    @SneakyThrows
    public QueueDto getQueue(UUID id) {
        Optional<QueueEntity> queueById = queueRepository.findById(id);
        if (queueById.isEmpty()) {
            throw new QueueServiceException("Queue not found.", HttpStatus.NOT_FOUND);
        }
        return this.map(queueById.get(), QueueDto.class);
    }
}
