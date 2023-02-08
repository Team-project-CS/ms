package com.gp.q.testcontainers.service;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.service.QueueService;
import com.gp.q.testcontainers.BaseTest;
import com.gp.q.testcontainers.BaseTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({BaseTestConfig.class})
class ServiceTest extends BaseTest {

    @Autowired
    private QueueService service;

    @Test
    void saveAndGet() {
        QueueMessageDto dto = new QueueMessageDto("queue_1", "message from test");
        service.createQueue(dto);

        QueueMessageDto receivedDto = service.getQueue(dto.getName());
        Assertions.assertEquals(dto, receivedDto);
    }

    @Test
    void pushAndPops() {
        String queueName = "queue_2";
        QueueMessageDto dto1 = new QueueMessageDto(queueName, "message 1");
        QueueMessageDto dto2 = new QueueMessageDto(queueName, "message 2");
        QueueMessageDto dto3 = new QueueMessageDto(queueName, "message 3");

        service.createQueue(dto1);
        service.createQueue(dto2);
        service.createQueue(dto3);

        QueueMessageDto receivedDto1 = service.getQueue(queueName);
        QueueMessageDto receivedDto2 = service.getQueue(queueName);
        QueueMessageDto receivedDto3 = service.getQueue(queueName);
        Assertions.assertEquals(dto1, receivedDto1);
        Assertions.assertEquals(dto2, receivedDto2);
        Assertions.assertEquals(dto3, receivedDto3);
    }

    @Test
    void pushInDifferentQueues() {
        QueueMessageDto dto1 = new QueueMessageDto("queue_1", "message 1");
        QueueMessageDto dto2 = new QueueMessageDto("queue_2", "message 1");
        QueueMessageDto dto3 = new QueueMessageDto("queue_3", "message 1");

        service.createQueue(dto1);
        service.createQueue(dto2);
        service.createQueue(dto3);

        QueueMessageDto receivedDto1 = service.getQueue(dto1.getName());
        QueueMessageDto receivedDto2 = service.getQueue(dto2.getName());
        QueueMessageDto receivedDto3 = service.getQueue(dto3.getName());

        Assertions.assertEquals(dto1, receivedDto1);
        Assertions.assertEquals(dto2, receivedDto2);
        Assertions.assertEquals(dto3, receivedDto3);
    }


}
