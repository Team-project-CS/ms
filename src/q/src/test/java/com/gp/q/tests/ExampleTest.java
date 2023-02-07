package com.gp.q.tests;

import com.gp.q.BaseTest;
import com.gp.q.BaseTestConfig;
import com.gp.q.repository.QueueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({BaseTestConfig.class})
public class ExampleTest extends BaseTest {

    @Autowired
    private QueueRepository queueRepository;
    @Test
    void testExample() {
        queueRepository.findAll();
        Assertions.assertEquals(1, 1);
    }
}
