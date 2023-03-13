package com.gp.q.controller;

import com.gp.q.model.QueueMessageDirection;
import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.dto.QueueMessageLogDto;
import com.gp.q.model.dto.QueueMessagePeriodDto;
import com.gp.q.model.dto.QueuePropertyDto;
import com.gp.q.model.entity.QueueMessageLogEntity;
import com.gp.q.repository.QueueCrudRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = QueueHistoryControllerTest.Initializer.class)
@Testcontainers
class QueueHistoryControllerTest {

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
    private QueueController controller;

    @Autowired
    private QueueHistoryController historyController;

    @SuppressWarnings("ConstantConditions")
    @Test
    void getMessagesByQueueName() {
        String queueName = "getMessagesByQueueName";

        // Создаем очередь через контроллер
        QueuePropertyDto queuePropertyDto = new QueuePropertyDto(queueName, "test");
        var createResponse = controller.createQueues(List.of(queuePropertyDto));
        Assertions.assertTrue(createResponse.getStatusCode().is2xxSuccessful());
        QueueMessageDto messageDto = new QueueMessageDto(queueName, "message from test");
        QueueMessageLogDto logDto = new QueueMessageLogDto(messageDto.getName(), messageDto.getMessage(), QueueMessageDirection.IN);
        Assertions.assertTrue(controller.postMessageInQueue(messageDto).getStatusCode().is2xxSuccessful());

        ResponseEntity<List<QueueMessageLogDto>> messagesByQueueName = historyController.getMessagesByQueueName(queueName);
        Assertions.assertTrue(messagesByQueueName.getStatusCode().is2xxSuccessful());

        Assertions.assertEquals(1, messagesByQueueName.getBody().size());
        Assertions.assertEquals(logDto, messagesByQueueName.getBody().get(0));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void getMessagesByQueueName3() {
        String queueName = "getMessagesByQueueName3";

        // Создаем очередь через контроллер
        QueuePropertyDto queuePropertyDto = new QueuePropertyDto(queueName, "test");
        var createResponse = controller.createQueues(List.of(queuePropertyDto));
        Assertions.assertTrue(createResponse.getStatusCode().is2xxSuccessful());

        QueueMessageDto messageDto1 = new QueueMessageDto(queueName, "message from test 1");
        QueueMessageDto messageDto2 = new QueueMessageDto(queueName, "message from test 2");
        QueueMessageDto messageDto3 = new QueueMessageDto(queueName, "message from test 3");

        QueueMessageLogDto logDto1 = new QueueMessageLogDto(messageDto1.getName(), messageDto1.getMessage(), QueueMessageDirection.IN);
        QueueMessageLogDto logDto2 = new QueueMessageLogDto(messageDto2.getName(), messageDto2.getMessage(), QueueMessageDirection.IN);
        QueueMessageLogDto logDto3 = new QueueMessageLogDto(messageDto3.getName(), messageDto3.getMessage(), QueueMessageDirection.IN);


        Assertions.assertTrue(controller.postMessageInQueue(messageDto1).getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(controller.postMessageInQueue(messageDto2).getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(controller.postMessageInQueue(messageDto3).getStatusCode().is2xxSuccessful());

        ResponseEntity<List<QueueMessageLogDto>> messagesByQueueName = historyController.getMessagesByQueueName(queueName);
        Assertions.assertTrue(messagesByQueueName.getStatusCode().is2xxSuccessful());

        Assertions.assertEquals(3, messagesByQueueName.getBody().size());
        Assertions.assertEquals(logDto1, messagesByQueueName.getBody().get(0));
        Assertions.assertEquals(logDto2, messagesByQueueName.getBody().get(1));
        Assertions.assertEquals(logDto3, messagesByQueueName.getBody().get(2));
    }

    @Autowired
    private QueueCrudRepository repository;

    @SuppressWarnings("ConstantConditions")
    @Test
    void getMessagesByDateAll() {
        String queueName = "getMessagesByDateAll";
        LocalDateTime current = LocalDateTime.now();

        List<QueueMessageLogEntity> list = List.of(
                new QueueMessageLogEntity(queueName, "message 1", current.minusSeconds(10), QueueMessageDirection.IN),
                new QueueMessageLogEntity(queueName, "message 2", current.minusSeconds(40), QueueMessageDirection.IN),
                new QueueMessageLogEntity(queueName, "message 3", current.minusSeconds(50), QueueMessageDirection.IN),
                new QueueMessageLogEntity(queueName, "message 4", current.minusSeconds(62), QueueMessageDirection.IN));

        repository.saveAll(list);

        QueueMessagePeriodDto periodDto = new QueueMessagePeriodDto(queueName, current.minusMinutes(1), current);

        ResponseEntity<List<QueueMessageLogDto>> messagesByQueueName = historyController.getMessagesByDate(periodDto);
        Assertions.assertTrue(messagesByQueueName.getStatusCode().is2xxSuccessful());

        Assertions.assertEquals(3, messagesByQueueName.getBody().size());
    }

}