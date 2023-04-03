package com.gp.q.service;

import com.gp.q.model.dto.QueueLogDto;
import com.gp.q.model.dto.QueueMessageDto;
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
@ContextConfiguration(initializers = QueueServiceImplTest.Initializer.class)
@Testcontainers
class QueueServiceImplTest {
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
    private QueueService service;

    @Autowired
    private QueueManagementService managementService;

    @Test
    void getAllMessagesEmpty() {

        LocalDateTime begin = LocalDateTime.of(LocalDate.MIN, LocalTime.MAX);
        LocalDateTime end = LocalDateTime.now();
        List<QueueLogDto> remaining = service.getAllMessages(begin, end);

        Assertions.assertTrue(remaining.isEmpty());
    }

    @Test
    void getAllMessages1() {
        String queueName = "getAllMessages1";
        managementService.createQueues(List.of(new QueuePropertyDto(queueName, "test")));
        QueueMessageDto messageDto = new QueueMessageDto(queueName, "message");
        service.pushInQueue(messageDto);

        Assertions.assertFalse(service.getAllMessages(queueName).isEmpty());
    }

}