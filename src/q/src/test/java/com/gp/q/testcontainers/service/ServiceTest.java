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
}
