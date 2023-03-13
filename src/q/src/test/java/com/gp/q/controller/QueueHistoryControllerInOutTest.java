package com.gp.q.controller;

import com.gp.q.model.QueueMessageDirection;
import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.dto.QueueMessageLogDto;
import com.gp.q.model.dto.QueuePropertyDto;
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

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = QueueHistoryControllerInOutTest.Initializer.class)
@Testcontainers
class QueueHistoryControllerInOutTest {

    @SuppressWarnings("rawtypes")
    @Container
    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);
    @Autowired
    private QueueController controller;
    @Autowired
    private QueueHistoryController historyController;

    @SuppressWarnings("ConstantConditions")
    @Test
    void pushIn() {
        String queueName = "pushIn";

        QueueMessageDto messageDto = new QueueMessageDto(queueName, "message from test");
        Assertions.assertTrue(controller.postMessageInQueue(messageDto).getStatusCode().is2xxSuccessful());

        ResponseEntity<List<QueueMessageLogDto>> messagesByQueueName = historyController.getMessagesByQueueName(queueName);
        Assertions.assertTrue(messagesByQueueName.getStatusCode().is2xxSuccessful());


        QueueMessageLogDto inDto = new QueueMessageLogDto(messageDto.getName(), messageDto.getMessage(), QueueMessageDirection.IN);

        Assertions.assertEquals(1, messagesByQueueName.getBody().size());
        Assertions.assertEquals(inDto, messagesByQueueName.getBody().get(0));

    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void pushInPopOut() {
        String queueName = "pushInPopOut";

        // Создаем очередь через контроллер
        Assertions.assertTrue(controller.createQueues(
                        List.of(new QueuePropertyDto(queueName, "test")))
                .getStatusCode().is2xxSuccessful());

        // Записываем и считываем из очереди
        QueueMessageDto messageDto = new QueueMessageDto(queueName, "message from test");
        Assertions.assertTrue(controller.postMessageInQueue(messageDto).getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(controller.getMessageFromQueue(queueName).getStatusCode().is2xxSuccessful());

        // Собираем логи очереди
        ResponseEntity<List<QueueMessageLogDto>> messagesByQueueName = historyController.getMessagesByQueueName(queueName);
        Assertions.assertTrue(messagesByQueueName.getStatusCode().is2xxSuccessful());

        QueueMessageLogDto inDto = new QueueMessageLogDto(messageDto.getName(), messageDto.getMessage(), QueueMessageDirection.IN);
        QueueMessageLogDto outDto = new QueueMessageLogDto(messageDto.getName(), messageDto.getMessage(), QueueMessageDirection.OUT);

        // Проверяем 2 лога - чтение и запись
        Assertions.assertEquals(2, messagesByQueueName.getBody().size());
        Assertions.assertEquals(inDto, messagesByQueueName.getBody().get(0));
        Assertions.assertEquals(outDto, messagesByQueueName.getBody().get(1));
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