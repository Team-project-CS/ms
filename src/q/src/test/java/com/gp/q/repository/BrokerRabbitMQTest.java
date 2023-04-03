package com.gp.q.repository;

import com.gp.q.model.entity.QueueMessageEntity;
import com.gp.q.repository.broker.BrokerRabbitMQ;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = BrokerRabbitMQTest.Initializer.class)
@Testcontainers
class BrokerRabbitMQTest {

    @SuppressWarnings("rawtypes")
    @Container
    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);
    @Autowired
    private BrokerRabbitMQ repository;

    @Autowired
    private AmqpAdmin admin;

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

    @Test
    void test() {
        String queueName = "test_1_test_queue_1";
        admin.declareQueue(new Queue(queueName, false));

        QueueMessageEntity entity = new QueueMessageEntity(queueName, "message");

        repository.push(entity);

        await().atLeast(100, TimeUnit.MILLISECONDS);

        QueueMessageEntity message = repository.pop(queueName);
        Assertions.assertEquals(entity, message);

        admin.deleteQueue(queueName);
    }


    @Test
    void pushAndPopsSigleQueue() {
        String queueName = "test_2_queue_2";
        admin.declareQueue(new Queue(queueName, false));

        QueueMessageEntity entity1 = new QueueMessageEntity(queueName, "message 1");
        QueueMessageEntity entity2 = new QueueMessageEntity(queueName, "message 2");
        QueueMessageEntity entity3 = new QueueMessageEntity(queueName, "message 3");

        repository.push(entity1);
        repository.push(entity2);
        repository.push(entity3);

        await().atLeast(100, TimeUnit.MILLISECONDS);

        QueueMessageEntity receivedEntity1 = repository.pop(queueName);
        QueueMessageEntity receivedEntity2 = repository.pop(queueName);
        QueueMessageEntity receivedEntity3 = repository.pop(queueName);

        Assertions.assertEquals(entity1, receivedEntity1);
        Assertions.assertEquals(entity2, receivedEntity2);
        Assertions.assertEquals(entity3, receivedEntity3);

        admin.deleteQueue(queueName);
    }


    @Test
    void singlePushInDifferentQueues() {

        String queue1 = "test_3_queue_1";
        String queue2 = "test_3_queue_2";
        String queue3 = "test_3_queue_3";
        admin.declareQueue(new Queue(queue1, false));
        admin.declareQueue(new Queue(queue2, false));
        admin.declareQueue(new Queue(queue3, false));

        QueueMessageEntity entity1 = new QueueMessageEntity(queue1, "message 1");
        QueueMessageEntity entity2 = new QueueMessageEntity(queue2, "message 2");
        QueueMessageEntity entity3 = new QueueMessageEntity(queue3, "message 3");

        repository.push(entity1);
        repository.push(entity2);
        repository.push(entity3);

        await().atLeast(100, TimeUnit.MILLISECONDS);

        QueueMessageEntity received1 = repository.pop(queue1);
        QueueMessageEntity received2 = repository.pop(queue2);
        QueueMessageEntity received3 = repository.pop(queue3);

        Assertions.assertEquals(entity1, received1);
        Assertions.assertEquals(entity2, received2);
        Assertions.assertEquals(entity3, received3);

        admin.deleteQueue(queue1);
        admin.deleteQueue(queue2);
        admin.deleteQueue(queue3);
    }

    @Test
    void multiPushInDifferentQueues() {
        String queue1 = "test_4_queue_1";
        String queue2 = "test_4_queue_2";
        String queue3 = "test_4_queue_3";
        admin.declareQueue(new Queue(queue1, false));
        admin.declareQueue(new Queue(queue2, false));
        admin.declareQueue(new Queue(queue3, false));

        QueueMessageEntity entity1 = new QueueMessageEntity(queue1, "message 1");
        QueueMessageEntity entity2 = new QueueMessageEntity(queue2, "message 2");
        QueueMessageEntity entity3 = new QueueMessageEntity(queue3, "message 3");
        QueueMessageEntity entity4 = new QueueMessageEntity(queue1, "message 4");
        QueueMessageEntity entity5 = new QueueMessageEntity(queue2, "message 5");
        QueueMessageEntity entity6 = new QueueMessageEntity(queue3, "message 6");

        for (var i : List.of(entity1, entity2, entity3, entity4, entity5, entity6)) {
            repository.push(i);
        }

        await().atLeast(100, TimeUnit.MILLISECONDS);

        Assertions.assertEquals(entity1, repository.pop(queue1));
        Assertions.assertEquals(entity4, repository.pop(queue1));

        Assertions.assertEquals(entity2, repository.pop(queue2));
        Assertions.assertEquals(entity5, repository.pop(queue2));

        Assertions.assertEquals(entity3, repository.pop(queue3));
        Assertions.assertEquals(entity6, repository.pop(queue3));

        admin.deleteQueue(queue1);
        admin.deleteQueue(queue2);
        admin.deleteQueue(queue3);
    }
}