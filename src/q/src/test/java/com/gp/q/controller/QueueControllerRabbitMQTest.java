package com.gp.q.controller;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.dto.QueuePropertyDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueInformation;
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
@ContextConfiguration(initializers = QueueControllerRabbitMQTest.Initializer.class)
@Testcontainers
class QueueControllerRabbitMQTest {

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
    private AmqpAdmin admin;
    @Autowired
    private QueueController controller;

    @SuppressWarnings("ConstantConditions")
    @Test
    void queueCreation() {
        String queueName = "test_queue_creation_1";

        // Проверяем, что очереди нет
        QueueInformation queueInfo = admin.getQueueInfo(queueName);
        Assertions.assertNull(queueInfo);

        // Создаем очередь через контроллер
        QueuePropertyDto queuePropertyDto = new QueuePropertyDto(queueName, "test");
        ResponseEntity<List<QueuePropertyDto>> createResponse = controller.createQueues(List.of(queuePropertyDto));
        Assertions.assertTrue(createResponse.getStatusCode().is2xxSuccessful());

        // Проверяем, что очередь создалась и имена совпадают
        Assertions.assertNotNull(admin.getQueueInfo(queueName));
        Assertions.assertEquals(queueName, admin.getQueueInfo(queueName).getName());

        // Удаляем очередь
        Assertions.assertTrue(admin.deleteQueue(queueName));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void queueCreation3() {
        String queueName2 = "test_queue_creation_3_1";
        String queueName3 = "test_queue_creation_3_2";
        String queueName4 = "test_queue_creation_3_3";

        // Проверяем, что очередей нет
        Assertions.assertNull(admin.getQueueInfo(queueName2));
        Assertions.assertNull(admin.getQueueInfo(queueName3));
        Assertions.assertNull(admin.getQueueInfo(queueName4));

        // Создаем 3 очереди через контроллер
        ResponseEntity<List<QueuePropertyDto>> createResponse = controller.createQueues(
                List.of(new QueuePropertyDto(queueName2, "test"),
                        new QueuePropertyDto(queueName3, "test"),
                        new QueuePropertyDto(queueName4, "test")));
        Assertions.assertTrue(createResponse.getStatusCode().is2xxSuccessful());

        // Проверяем, что очереди создались и имена совпадают
        Assertions.assertNotNull(admin.getQueueInfo(queueName2));
        Assertions.assertNotNull(admin.getQueueInfo(queueName3));
        Assertions.assertNotNull(admin.getQueueInfo(queueName4));
        Assertions.assertEquals(queueName2, admin.getQueueInfo(queueName2).getName());
        Assertions.assertEquals(queueName3, admin.getQueueInfo(queueName3).getName());
        Assertions.assertEquals(queueName4, admin.getQueueInfo(queueName4).getName());

        // Удаляем очереди
        Assertions.assertTrue(admin.deleteQueue(queueName2));
        Assertions.assertTrue(admin.deleteQueue(queueName3));
        Assertions.assertTrue(admin.deleteQueue(queueName4));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void queueDeletion3() {
        String queueName2 = "test_queue_deletion_3_1";
        String queueName3 = "test_queue_creation_3_2";
        String queueName4 = "test_queue_creation_3_3";

        admin.declareQueue(new Queue(queueName2, false));
        admin.declareQueue(new Queue(queueName3, false));
        admin.declareQueue(new Queue(queueName4, false));

        // Проверяем, что очереди создались и имена совпадают
        Assertions.assertNotNull(admin.getQueueInfo(queueName2));
        Assertions.assertNotNull(admin.getQueueInfo(queueName3));
        Assertions.assertNotNull(admin.getQueueInfo(queueName4));

        Assertions.assertEquals(queueName2, admin.getQueueInfo(queueName2).getName());
        Assertions.assertEquals(queueName3, admin.getQueueInfo(queueName3).getName());
        Assertions.assertEquals(queueName4, admin.getQueueInfo(queueName4).getName());

        // Удаляем очереди
        Assertions.assertTrue(controller.deleteSingleQueue(queueName2).getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(controller.deleteSingleQueue(queueName3).getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(controller.deleteSingleQueue(queueName4).getStatusCode().is2xxSuccessful());

        // Проверяем, что очередей нет
        Assertions.assertNull(admin.getQueueInfo(queueName2));
        Assertions.assertNull(admin.getQueueInfo(queueName3));
        Assertions.assertNull(admin.getQueueInfo(queueName4));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void queueDeletionReturn3() {
        String queueName2 = "test_queue_deletion_return_3_1";
        String queueName3 = "test_queue_deletion_return_3_2";
        String queueName4 = "test_queue_deletion_return_3_3";

        // Создаем 3 очереди через контроллер
        ResponseEntity<List<QueuePropertyDto>> createResponse = controller.createQueues(
                List.of(new QueuePropertyDto(queueName2, "test"),
                        new QueuePropertyDto(queueName3, "test"),
                        new QueuePropertyDto(queueName4, "test")));
        Assertions.assertTrue(createResponse.getStatusCode().is2xxSuccessful());

        // Проверяем, что очереди создались и имена совпадают
        Assertions.assertNotNull(admin.getQueueInfo(queueName2));
        Assertions.assertNotNull(admin.getQueueInfo(queueName3));
        Assertions.assertNotNull(admin.getQueueInfo(queueName4));
        Assertions.assertEquals(queueName2, admin.getQueueInfo(queueName2).getName());
        Assertions.assertEquals(queueName3, admin.getQueueInfo(queueName3).getName());
        Assertions.assertEquals(queueName4, admin.getQueueInfo(queueName4).getName());

        // Удаляем очереди
        Assertions.assertEquals(2, controller.deleteSingleQueue(queueName2).getBody().size());
        Assertions.assertEquals(1, controller.deleteSingleQueue(queueName3).getBody().size());
        Assertions.assertEquals(0, controller.deleteSingleQueue(queueName4).getBody().size());
    }

    @Test
    void pushAndPop() {
        // create queue
        String queueName = "test_queue_push_and_pop_1";
        admin.declareQueue(new Queue(queueName, false));

        // push in queue
        QueueMessageDto messageDto = new QueueMessageDto(queueName, "message from test");
        ResponseEntity<QueueMessageDto> postResponse = controller.postMessageInQueue(messageDto);
        Assertions.assertTrue(postResponse.getStatusCode().is2xxSuccessful());

        // read from queue
        ResponseEntity<QueueMessageDto> getResponse = controller.getMessageFromQueue(queueName);
        Assertions.assertTrue(getResponse.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(messageDto, getResponse.getBody());

        // delete queue
        admin.deleteQueue(queueName);
    }

    @Test
    void createAndDeleteQueue() {
        // create queue
        String queueName = "test_queue_create_push_pop_delete_1";
        QueuePropertyDto queuePropertyDto = new QueuePropertyDto(queueName, "test");
        ResponseEntity<List<QueuePropertyDto>> createResponse = controller.createQueues(List.of(queuePropertyDto));
        Assertions.assertTrue(createResponse.getStatusCode().is2xxSuccessful());

        // delete queue
        ResponseEntity<List<QueuePropertyDto>> deleteResponse = controller.deleteSingleQueue(queueName);
        Assertions.assertTrue(deleteResponse.getStatusCode().is2xxSuccessful());
    }

    @Test
    void createPushPopDeleteQueue() {
        // create queue
        String queueName = "test_queue_create_push_pop_delete_1";
        QueuePropertyDto queuePropertyDto = new QueuePropertyDto(queueName, "test");
        ResponseEntity<List<QueuePropertyDto>> createResponse = controller.createQueues(List.of(queuePropertyDto));
        Assertions.assertTrue(createResponse.getStatusCode().is2xxSuccessful());

        // push in queue
        QueueMessageDto messageDto = new QueueMessageDto(queueName, "message from test");
        ResponseEntity<QueueMessageDto> postResponse = controller.postMessageInQueue(messageDto);
        Assertions.assertTrue(postResponse.getStatusCode().is2xxSuccessful());

        // read from queue
        ResponseEntity<QueueMessageDto> getResponse = controller.getMessageFromQueue(queueName);
        Assertions.assertTrue(getResponse.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(messageDto, getResponse.getBody());

        // delete queue
        ResponseEntity<List<QueuePropertyDto>> deleteResponse = controller.deleteSingleQueue(queueName);
        Assertions.assertTrue(deleteResponse.getStatusCode().is2xxSuccessful());
    }
}