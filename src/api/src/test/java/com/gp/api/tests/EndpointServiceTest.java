package com.gp.api.tests;

import com.gp.api.BaseTest;
import com.gp.api.BaseTestConfig;
import com.gp.api.exception.throwables.*;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.ParamType;
import com.gp.api.repository.EndpointRepository;
import com.gp.api.service.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({BaseTestConfig.class})
@Slf4j
public class EndpointServiceTest extends BaseTest {

    @Autowired
    private EndpointService endpointService;
    @Autowired
    private EndpointRepository endpointRepository;

    @AfterEach
    void clearDatabase() {
        endpointRepository.deleteAll();
    }

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

    @Test
    void useEndpointHappyTestCase() {
        Endpoint endpoint = endpointService.createEndpoint(getTestEndpointDto());

        Map<String, ?> body = Map.of(
                "field1", "some string",
                "field2", 1024
        );
        Map<String, ?> response = endpointService.useEndpoint(endpoint.getId(), body);

        assertTrue(response.containsKey("responseField1"));
        assertTrue(response.containsKey("responseField2"));
        assertDoesNotThrow(() -> Integer.parseInt(response.get("responseField2").toString()));
    }

    @Test
    void useEndpointNotFoundTestCase() {
        endpointService.createEndpoint(getTestEndpointDto());

        assertThrows(
                EndpointNotFoundException.class,
                () -> endpointService.useEndpoint(UUID.randomUUID(), Map.of())
        );
    }

    @Test
    void useEndpointMandatoryParameterNotSpecifiedTestCase() {
        Endpoint endpoint = endpointService.createEndpoint(getTestEndpointDto());

        Map<String, ?> bodyWithoutMandatoryParameter = Map.of(
                "field1", "some string"
        );

        assertThrows(
                MandatoryParameterNotSpecifiedException.class,
                () -> endpointService.useEndpoint(endpoint.getId(), bodyWithoutMandatoryParameter)
        );
    }

    @Test
    void useEndpointParameterTypeMismatchTestCase() {
        Endpoint endpoint = endpointService.createEndpoint(getTestEndpointDto());

        Map<String, ?> bodyWithMismatchedTypeOfParameter = Map.of(
                "field1", "some string",
                "field2", "here must be int"
        );

        assertThrows(
                ParameterTypeMismatchException.class,
                () -> endpointService.useEndpoint(endpoint.getId(), bodyWithMismatchedTypeOfParameter)
        );

    }

    @Test
    void deleteEndpointHappyTestCase(){
        EndpointDto testEndpointDto = getTestEndpointDto();

        endpointService.createEndpoint(testEndpointDto);

        assertThrows(EndpointNotFoundException.class, () -> endpointService.deleteEndpoint(UUID.randomUUID()));
    }

    @Test
    void deleteEndpointNotFoundTestCase(){
        EndpointDto testEndpointDto = getTestEndpointDto();

        Endpoint actualEndpoint = endpointService.createEndpoint(testEndpointDto);

        Endpoint deletedEndpoint = endpointService.deleteEndpoint(actualEndpoint.getId());

        assertEquals(
                Map.of(
                        "field1", ParamType.STRING,
                        "field2", ParamType.INTEGER
                ),
                deletedEndpoint.getBodyTemplate());
        assertEquals(deletedEndpoint.getTitle(), actualEndpoint.getTitle());
        assertEquals(deletedEndpoint.getDescription(), actualEndpoint.getDescription());
        assertTrue(deletedEndpoint.getResponseTemplate().containsKey("responseField1"));
        assertTrue(deletedEndpoint.getResponseTemplate().containsKey("responseField2"));
        assertEquals(ParamType.STRING, deletedEndpoint.getResponseTemplate().get("responseField1"));
        assertEquals(ParamType.INTEGER, deletedEndpoint.getResponseTemplate().get("responseField2"));
    }

    private EndpointDto getTestEndpointDto() {
        return EndpointDto.builder()
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
    }
}
