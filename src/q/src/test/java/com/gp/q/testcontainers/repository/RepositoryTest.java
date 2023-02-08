package com.gp.q.testcontainers.repository;

import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.repository.QueueRepository;
import com.gp.q.testcontainers.BaseTest;
import com.gp.q.testcontainers.BaseTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringJUnitConfig({BaseTestConfig.class})
class RepositoryTest extends BaseTest {

    @Autowired
    private QueueRepository queueRepository;

    @Test
    void saveAndGet() {
        QueueMessageEntity queueMessageEntity = new QueueMessageEntity();
        queueMessageEntity.setName("new name");
        queueRepository.save(queueMessageEntity);

        Optional<QueueMessageEntity> byId = queueRepository.findById(queueMessageEntity.getId());
        Assertions.assertEquals(queueMessageEntity, byId.get());
    }
}
