package com.gp.api.tests;

import com.gp.api.BaseTest;
import com.gp.api.BaseTestConfig;
import com.gp.api.log.EndpointLogService;
import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.dto.ParamDto;
import com.gp.api.model.pojo.EndpointLog;
import com.gp.api.model.types.Method;
import com.gp.api.repository.EndpointLogRepository;
import com.gp.api.repository.EndpointRepository;
import com.gp.api.service.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({BaseTestConfig.class})
@Slf4j
public class IntegrationTest extends BaseTest {

    @Autowired
    private EndpointService endpointService;
    @Autowired
    private EndpointLogService endpointLogService;
    @Autowired
    private EndpointRepository endpointRepository;
    @Autowired
    private EndpointLogRepository endpointLogRepository;

    private static EndpointDto getNormalEndpointDto() {
        return EndpointDto.builder()
                .title("test endpoint")
                .description("endpoint for tests")
                .bodyTemplate(
                        Set.of(
                                ParamDto.builder()
                                        .key("bodyStrField")
                                        .type("str")
                                        .build(),
                                ParamDto.builder()
                                        .key("bodyIntField")
                                        .type("int")
                                        .build(),
                                ParamDto.builder()
                                        .key("bodyRegexField")
                                        .type("regex")
                                        .value(".{1,10}")
                                        .build(),
                                ParamDto.builder()
                                        .key("bodyFixedField")
                                        .type("fixed")
                                        .value("fixed string")
                                        .build()
                        )
                )
                .responseTemplate(
                        Set.of(
                                ParamDto.builder()
                                        .key("responseStrField")
                                        .type("str")
                                        .build(),
                                ParamDto.builder()
                                        .key("responseIntField")
                                        .type("int")
                                        .build(),
                                ParamDto.builder()
                                        .key("responseRegexField")
                                        .type("regex")
                                        .value(".{1,10}")
                                        .build(),
                                ParamDto.builder()
                                        .key("responseFixedField")
                                        .type("fixed")
                                        .value("fixed string")
                                        .build()
                        )
                )
                .method("post")
                .build();
    }

    @AfterEach
    void clearTable() {
        endpointRepository.deleteAll();
        endpointLogRepository.deleteAll();
    }

    private Map<String, String> mapValuesToString(Map<String, ?> input) {
        return input.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Test
    @DisplayName("Valid use of pre-created endpoint")
    void happyUseEndpointTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        UUID id = endpointService.createEndpoint(normalEndpointDto).getId();

        Map<String, ?> response = endpointService.useEndpoint(
                id,
                Map.of(
                        "bodyStrField", "some string",
                        "bodyIntField", 1024,
                        "bodyRegexField", "ab1?%",
                        "bodyFixedField", "fixed string"
                ),
                Method.POST);

        EndpointLog log = endpointLogService.getLogsInDateRange(id, null, null).get(0);

        assertEquals(1, IterableUtils.toList(endpointRepository.findAll()).size());
        assertTrue(response.get("responseStrField") instanceof String);
        assertDoesNotThrow(() -> Integer.parseInt(response.get("responseIntField").toString()));
        assertEquals(response.get("responseFixedField"), "fixed string");

        assertEquals(1, IterableUtils.toList(endpointLogRepository.findAll()).size());
        assertEquals(log.getEndpointId(), id);
        assertEquals(log.getInput(),
                Map.of(
                        "bodyStrField", "some string",
                        "bodyIntField", "1024",
                        "bodyRegexField", "ab1?%",
                        "bodyFixedField", "fixed string"
                )
        );
        assertEquals(log.getOutput(), mapValuesToString(response));
        assertNotNull(log.getCreationDate());
        assertTrue(log.getCreationDate().isBefore(LocalDateTime.now()));
    }
}
