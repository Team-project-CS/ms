package com.gp.api.tests;

import com.gp.api.exception.throwables.InvalidDateRangeException;
import com.gp.api.log.EndpointLogService;
import com.gp.api.model.pojo.EndpointLog;
import com.gp.api.repository.EndpointLogRepository;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EndpointLogServiceTest {

    @Autowired
    private EndpointLogService endpointLogService;
    @Autowired
    private EndpointLogRepository endpointLogRepository;

    @AfterEach
    void clearTable() {
        endpointLogRepository.deleteAll();
    }

    @Test
    @DisplayName("Valid log creation")
    void happyLogCreateTestCase() {
        UUID id = UUID.randomUUID();
        endpointLogService.createLogEvent(id,
                Map.of("input1", "input_value1"),
                Map.of("output1", "output_value1"));

        EndpointLog log = endpointLogService.getLogsInDateRange(id, null, null).get(0);

        assertEquals(Map.of("input1", "input_value1"), log.getInput());
        assertEquals(Map.of("output1", "output_value1"), log.getOutput());
        assertEquals(id, log.getEndpointId());
        assertNotNull(log.getCreationDate());
        assertTrue(log.getCreationDate().isBefore(LocalDateTime.now()));
        assertEquals(1, IterableUtils.toList(endpointLogRepository.findAll()).size());
    }

    @Test
    @DisplayName("Valid all logs retrieve")
    void getAllLogsTestCase() {
        UUID id = UUID.randomUUID();
        endpointLogService.createLogEvent(id,
                Map.of("input1", "input_value1"),
                Map.of("output1", "output_value1"));
        endpointLogService.createLogEvent(id,
                Map.of("input2", "input_value2"),
                Map.of("output2", "output_value2"));
        endpointLogService.createLogEvent(id,
                Map.of("input3", "input_value3"),
                Map.of("output3", "output_value3"));

        List<EndpointLog> logs = endpointLogService.getLogsInDateRange(id, null, null);

        assertEquals(3, logs.size());
    }

    @Test
    @DisplayName("Valid logs retrieve after the start date")
    void happyGetLogsAfterStartDateTestCase() {
        UUID id = UUID.randomUUID();
        endpointLogService.createLogEvent(id,
                Map.of("input1", "input_value1"),
                Map.of("output1", "output_value1"));
        endpointLogService.createLogEvent(id,
                Map.of("input2", "input_value2"),
                Map.of("output2", "output_value2"));
        endpointLogService.createLogEvent(id,
                Map.of("input3", "input_value3"),
                Map.of("output3", "output_value3"));

        List<EndpointLog> logs = endpointLogService.getLogsInDateRange(id, LocalDateTime.now().minusSeconds(10), null);

        assertEquals(3, logs.size());
    }

    @Test
    @DisplayName("Valid logs retrieve before the end date")
    void happyGetLogsBeforeEndDateTestCase() {
        UUID id = UUID.randomUUID();
        endpointLogService.createLogEvent(id,
                Map.of("input1", "input_value1"),
                Map.of("output1", "output_value1"));
        endpointLogService.createLogEvent(id,
                Map.of("input2", "input_value2"),
                Map.of("output2", "output_value2"));
        endpointLogService.createLogEvent(id,
                Map.of("input3", "input_value3"),
                Map.of("output3", "output_value3"));

        List<EndpointLog> logs = endpointLogService.getLogsInDateRange(id, null, LocalDateTime.now().plusSeconds(10));

        assertEquals(3, logs.size());
    }

    @Test
    @DisplayName("Valid logs retrieve between the valid date range")
    void happyGetLogsBetweenDateRangeTestCase() {
        UUID id = UUID.randomUUID();
        endpointLogService.createLogEvent(id,
                Map.of("input1", "input_value1"),
                Map.of("output1", "output_value1"));
        endpointLogService.createLogEvent(id,
                Map.of("input2", "input_value2"),
                Map.of("output2", "output_value2"));
        endpointLogService.createLogEvent(id,
                Map.of("input3", "input_value3"),
                Map.of("output3", "output_value3"));

        List<EndpointLog> logs = endpointLogService.getLogsInDateRange(id,
                LocalDateTime.now().minusSeconds(10),
                LocalDateTime.now().plusSeconds(10));

        assertEquals(3, logs.size());
    }

    @Test
    @DisplayName("Invalid date range")
    void invalidDateRangeTestCase() {
        assertThrows(InvalidDateRangeException.class,
                () -> endpointLogService.getLogsInDateRange(UUID.randomUUID(),
                        LocalDateTime.now().plusSeconds(10),
                        LocalDateTime.now().minusSeconds(10))
        );
    }
}
