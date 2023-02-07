package com.gp.api.tests;

import com.gp.api.BaseTest;
import com.gp.api.BaseTestConfig;
import com.gp.api.exception.throwables.InvalidBodyTemplateException;
import com.gp.api.exception.throwables.InvalidResponseTemplateException;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.ParamType;
import com.gp.api.service.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({BaseTestConfig.class})
@Slf4j
public class EndpointServiceTest extends BaseTest {

    @Autowired
    private EndpointService endpointService;

    @Test
    void createEndpointHappyTestCase() {
        EndpointDto endpointDto = EndpointDto.builder()
                .title("Some test endpoint")
                .description("Some test description")
                .bodyTemplate(Map.of(
                        "field1", "str",
                        "field2", "int"
                ))
                .responseTemplate(Map.of(
                        "responseField1", "str",
                        "responseField2", "int"
                ))
                .build();

        Endpoint actualEndpoint = endpointService.createEndpoint(endpointDto);

        assertEquals(
                Map.of(
                        "field1", ParamType.STRING,
                        "field2", ParamType.INTEGER
                ),
                actualEndpoint.getBodyTemplate());
        assertEquals(endpointDto.getTitle(), actualEndpoint.getTitle());
        assertEquals(endpointDto.getDescription(), actualEndpoint.getDescription());
        assertTrue(actualEndpoint.getResponseTemplate().containsKey("responseField1"));
        assertTrue(actualEndpoint.getResponseTemplate().containsKey("responseField2"));
        assertEquals(ParamType.STRING, actualEndpoint.getResponseTemplate().get("responseField1"));
        assertEquals(ParamType.INTEGER, actualEndpoint.getResponseTemplate().get("responseField2"));
    }

    @Test
    void createEndpointInvalidBodyTemplateTestCase() {
        EndpointDto endpointDto = EndpointDto.builder()
                .title("Some test endpoint")
                .description("Some test description")
                .bodyTemplate(Map.of(
                        "field1", "abracadabra",
                        "field2", "what?"
                ))
                .responseTemplate(Map.of(
                        "responseField1", "str",
                        "responseField2", "int"
                ))
                .build();

        assertThrows(InvalidBodyTemplateException.class, () -> endpointService.createEndpoint(endpointDto));
    }

    @Test
    void createEndpointInvalidResponseTemplateTestCase() {
        EndpointDto endpointDto = EndpointDto.builder()
                .title("Some test endpoint")
                .description("Some test description")
                .bodyTemplate(Map.of(
                        "field1", "str",
                        "field2", "int"
                ))
                .responseTemplate(Map.of(
                        "responseField1", "abracadabra",
                        "responseField2", "what?"
                ))
                .build();

        assertThrows(InvalidResponseTemplateException.class, () -> endpointService.createEndpoint(endpointDto));
    }
}
