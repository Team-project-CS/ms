package com.gp.q.repository;

import com.gp.q.model.entity.QueueMessageEntity;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = QueueCrudRepositoryTest.Initializer.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.MethodName.class)
class QueueCrudRepositoryTest {

    @SuppressWarnings("rawtypes")
    @Container
    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

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

    @Autowired
    private QueueCrudRepository repository;

    @Test
    @Order(1)
    void repositoryFindAll() {
        String queueName = "repositoryFindAll";
        QueueMessageEntity entity = new QueueMessageEntity(queueName, "message");

        repository.save(entity);

        List<QueueMessageEntity> messages = repository.findAll();
        Assertions.assertFalse(messages.isEmpty());
    }


    @Test
    void repositoryFindAllByQueueName1() {
        String queueName = "repositoryFindAllByQueueName1";

        QueueMessageEntity entity = new QueueMessageEntity(queueName, "message");

        repository.save(entity);

        List<QueueMessageEntity> messages = repository.findAllByName(queueName);
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(entity, messages.get(0));

    }

    @Test
    void repositoryFindAllByQueueName3() {
        String queueName = "repositoryFindAllByQueueName3";

        repository.save(new QueueMessageEntity(queueName, "message 1"));
        repository.save(new QueueMessageEntity(queueName, "message 2"));
        repository.save(new QueueMessageEntity(queueName, "message 3"));

        List<QueueMessageEntity> messages = repository.findAllByName(queueName);
        Assertions.assertEquals(3, messages.size());

    }

    @Test
    void repositoryFindAllByQueueName2Queue() {
        String queueName1 = "repositoryFindAllByQueueName2Queue_1";
        String queueName2 = "repositoryFindAllByQueueName2Queue_2";

        repository.save(new QueueMessageEntity(queueName1, "message 1"));
        repository.save(new QueueMessageEntity(queueName2, "message 2"));
        repository.save(new QueueMessageEntity(queueName2, "message 3"));

        Assertions.assertEquals(1, repository.findAllByName(queueName1).size());
        Assertions.assertEquals(2, repository.findAllByName(queueName2).size());
    }


    @Test
    void findByCreatedAtBetweenEmpty() {
        LocalDateTime date1 = LocalDateTime.of(LocalDate.MIN, LocalTime.MIN);
        LocalDateTime date2 = LocalDateTime.of(LocalDate.MAX, LocalTime.MAX);

        Assertions.assertEquals(0, repository.findByCreationDateBetween(date1, date2).size());
    }

    @Test
    void findByCreatedAtBetween() {
        String queueName = "findByCreatedAtBetween1";

        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2022, 1, 1), LocalTime.of(23, 1, 1));
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.of(23, 1, 1));

        LocalDateTime current = LocalDateTime.of(LocalDate.of(2023, 1, 1), LocalTime.of(23, 1, 1));

        repository.save(new QueueMessageEntity(queueName, "message findByCreatedAtBetween 1", current));
        repository.save(new QueueMessageEntity(queueName, "message findByCreatedAtBetween 2", current.plusMinutes(1)));
        repository.save(new QueueMessageEntity(queueName, "message findByCreatedAtBetween 3", current.plusMinutes(2)));

        Assertions.assertEquals(3, repository.findByCreationDateBetween(startDate, endDate).size());
    }

    @Test
    void findByCreatedAtBetweenAndName() {
        String queueName = "findByCreatedAtBetweenAndName";
        String queueName2 = "findByCreatedAtBetweenAndName 2";

        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2022, 1, 1), LocalTime.of(23, 1, 1));
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.of(23, 1, 1));
        LocalDateTime current = LocalDateTime.of(LocalDate.of(2023, 1, 1), LocalTime.of(23, 1, 1));

        repository.save(new QueueMessageEntity(queueName, "message findByCreatedAtBetween 1", current));
        repository.save(new QueueMessageEntity(queueName, "message findByCreatedAtBetween 2", current.plusMinutes(1)));

        repository.save(new QueueMessageEntity(queueName2, "message findByCreatedAtBetween 1", current));
        repository.save(new QueueMessageEntity(queueName2, "message findByCreatedAtBetween 2", current.plusMinutes(10)));
        repository.save(new QueueMessageEntity(queueName2, "message findByCreatedAtBetween 3", current.plusMinutes(11)));
        repository.save(new QueueMessageEntity(queueName2, "message findByCreatedAtBetween 4", current.plusMinutes(12)));

        Assertions.assertEquals(2, repository.findByCreationDateBetweenAndName(startDate, endDate, queueName).size());
        Assertions.assertEquals(4, repository.findByCreationDateBetweenAndName(startDate, endDate, queueName2).size());
    }

}