package com.gp.q.repository;

import com.gp.q.model.QueueMessageDirection;
import com.gp.q.model.entity.QueueLogEntity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = QueueLogRepositoryLogTest.Initializer.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.MethodName.class)
class QueueLogRepositoryLogTest {

    @SuppressWarnings("rawtypes")
    @Container
    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);
    @Autowired
    private QueueLogRepository repository;

    @Test
    @Order(1)
    void repositoryFindAllByNameSingleInOut() {
        String queueName = "repositoryFindAllSingleInOut";
        QueueLogEntity entity = new QueueLogEntity(queueName, "message", LocalDateTime.now(), QueueMessageDirection.IN);
        QueueLogEntity entity2 = new QueueLogEntity(queueName, "message", LocalDateTime.now(), QueueMessageDirection.OUT);

        repository.save(entity);
        repository.save(entity2);

        List<QueueLogEntity> messages = repository.findAllByName(queueName);
        Assertions.assertEquals(2, messages.size());
    }

    @Test
    @Order(2)
    void repositoryFindAllByName() {
        String queueName = "repositoryFindAllByName";
        List<QueueLogEntity> list = List.of(new QueueLogEntity(queueName, "message 1", LocalDateTime.now(), QueueMessageDirection.IN),
                new QueueLogEntity(queueName, "message 2", LocalDateTime.now(), QueueMessageDirection.OUT),
                new QueueLogEntity(queueName, "message 3", LocalDateTime.now(), QueueMessageDirection.IN));

        repository.saveAll(list);

        List<QueueLogEntity> messages = repository.findAllByName(queueName);
        Assertions.assertEquals(3, messages.size());
        Assertions.assertEquals(list, messages);
    }

    public static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.rabbitmq.host=" + rabbit.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbit.getMappedPort(5672)
            );
            values.applyTo(configurableApplicationContext);
        }
    }

}