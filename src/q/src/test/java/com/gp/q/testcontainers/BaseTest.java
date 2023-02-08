package com.gp.q.testcontainers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Import(PostgreTestConfig.class)
@ContextConfiguration(classes = PostgreTestConfig.class)
public abstract class BaseTest {
}
