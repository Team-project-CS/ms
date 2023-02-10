package com.gp.api.tests;

import com.gp.api.BaseTest;
import com.gp.api.BaseTestConfig;
import com.gp.api.exception.throwables.*;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.Param;
import com.gp.api.model.ParamDto;
import com.gp.api.model.types.BodyParamType;
import com.gp.api.model.types.ResponseParamType;
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
    void happyCreateEndpointTestCase() {
        EndpointDto actualEndpoint = getNormalEndpointDto();

        Endpoint endpoint = endpointService.createEndpoint(actualEndpoint);

        assertEquals(endpoint.getTitle(), actualEndpoint.getTitle());
        assertEquals(endpoint.getDescription(), actualEndpoint.getDescription());
        assertEquals(
                endpoint.getBodyTemplate(),
                Map.of(
                        "bodyStrField", Param.<BodyParamType>builder()
                                .type(BodyParamType.STRING)
                                .value("")
                                .build(),
                        "bodyIntField", Param.<BodyParamType>builder()
                                .type(BodyParamType.INTEGER)
                                .value("")
                                .build(),
                        "bodyRegexField", Param.<BodyParamType>builder()
                                .type(BodyParamType.REGEX)
                                .value(".{10}")
                                .build()
                )
        );
        assertEquals(
                endpoint.getResponseTemplate(),
                Map.of(
                        "responseStrField", Param.<ResponseParamType>builder()
                                .type(ResponseParamType.STRING)
                                .value("")
                                .build(),
                        "responseIntField", Param.<ResponseParamType>builder()
                                .type(ResponseParamType.INTEGER)
                                .value("")
                                .build(),
                        "responseRegexField", Param.<ResponseParamType>builder()
                                .type(ResponseParamType.REGEX)
                                .value(".{7}")
                                .build(),
                        "responseFixedField", Param.<ResponseParamType>builder()
                                .type(ResponseParamType.FIXED)
                                .value("fixed string")
                                .build()
                )
        );
    }

    @Test
    void InvalidBodyParameterCreateEndpointTestCase() {
        EndpointDto endpointDto = EndpointDto.builder()
                .bodyTemplate(
                        Map.of(
                                "bodyStrField", ParamDto.builder()
                                        .type("invalid type")
                                        .build()
                        )
                )
                .build();

        assertThrows(InvalidBodyTemplateException.class, () -> endpointService.createEndpoint(endpointDto));
    }

    @Test
    void InvalidResponseParameterCreateEndpointTestCase() {
        EndpointDto endpointDto = EndpointDto.builder()
                .responseTemplate(
                        Map.of(
                                "responseStrField", ParamDto.builder()
                                        .type("invalid type")
                                        .build()
                        )
                )
                .build();

        assertThrows(InvalidResponseTemplateException.class, () -> endpointService.createEndpoint(endpointDto));
    }

    @Test
    void happyUseEndpointTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        UUID id = endpointService.createEndpoint(normalEndpointDto).getId();

        Map<String, ?> response = endpointService.useEndpoint(
                id,
                Map.of(
                        "bodyStrField", "some string",
                        "bodyIntField", 1024,
                        "bodyRegexField", "ab1?%",
                        "bodyFixedString", "fixed string"
                )
        );

        assertTrue(response.get("responseStrField") instanceof String);
        assertDoesNotThrow(() -> Integer.parseInt(response.get("responseIntField").toString()));
        assertTrue(response.get("responseRegexField").toString().matches(".{10}"));
        assertEquals(response.get("responseFixedField"), "fixed string");
    }

    @Test
    void endpointNotFoundUseEndpointCase() {
        endpointService.createEndpoint(getNormalEndpointDto());

        assertThrows(EndpointNotFoundException.class, () -> endpointService.useEndpoint(UUID.randomUUID(), Map.of()));
    }

    @Test
    void mandatoryParamIsNotPresentUseEndpointTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        UUID id = endpointService.createEndpoint(normalEndpointDto).getId();

        MandatoryParameterNotSpecifiedException e = assertThrows(MandatoryParameterNotSpecifiedException.class, () ->
                endpointService.useEndpoint(
                        id,
                        Map.of(
                                "bodyIntField", 1024,
                                "bodyRegexField", "ab1?%",
                                "bodyFixedString", "fixed string"
                        )
                ));
        assertEquals(e.getMessage(), "Mandatory parameter bodyStrField not found");
    }

    @Test
    void intInvalidParameterTypeUseEndpointTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        UUID id = endpointService.createEndpoint(normalEndpointDto).getId();

        ParameterTypeMismatchException e = assertThrows(ParameterTypeMismatchException.class, () ->
                endpointService.useEndpoint(
                        id,
                        Map.of(
                                "bodyStrField", "some string",
                                "bodyIntField", "it must be int",
                                "bodyRegexField", "ab1?%",
                                "bodyFixedString", "fixed string"
                        )
                ));
        assertEquals(e.getMessage(), "Body parameter bodyIntField value is invalid");
    }

    @Test
    void regexInvalidParameterTypeUseEndpointTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        UUID id = endpointService.createEndpoint(normalEndpointDto).getId();

        ParameterTypeMismatchException e = assertThrows(ParameterTypeMismatchException.class, () ->
                endpointService.useEndpoint(
                        id,
                        Map.of(
                                "bodyStrField", "some string",
                                "bodyIntField", 1024,
                                "bodyRegexField", "it must have only 10 symbols",
                                "bodyFixedString", "fixed string"
                        )
                ));
        assertEquals(e.getMessage(), "Body parameter bodyRegexField value does not match regex [.{10}]");
    }

    @Test
    void fixedParameterTypeUseEndpointTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        UUID id = endpointService.createEndpoint(normalEndpointDto).getId();

        ParameterTypeMismatchException e = assertThrows(ParameterTypeMismatchException.class, () ->
                endpointService.useEndpoint(
                        id,
                        Map.of(
                                "bodyStrField", "some string",
                                "bodyIntField", 1024,
                                "bodyRegexField", "ab1?%",
                                "bodyFixedString", "this is wrong fixed string"
                        )
                ));
        assertEquals(e.getMessage(), "Body parameter bodyFixedString value does not match fixed value [fixed string]");
    }

    @Test
    void happyDeleteEndpointCate() {
        EndpointDto endpointDto = getNormalEndpointDto();
        UUID id = endpointService.createEndpoint(endpointDto).getId();

        Endpoint deletedEndpoint = endpointService.deleteEndpoint(id);

        assertThrows(EndpointNotFoundException.class, () -> endpointService.deleteEndpoint(id));
        assertEquals(deletedEndpoint.getTitle(), endpointDto.getTitle());
        assertEquals(deletedEndpoint.getDescription(), endpointDto.getDescription());
        assertEquals(deletedEndpoint.getId(), id);
    }

    @Test
    void endpointNotFoundDeleteEndpointCate() {
        endpointService.createEndpoint(getNormalEndpointDto());

        assertThrows(EndpointNotFoundException.class, () -> endpointService.deleteEndpoint(UUID.randomUUID()));
    }

    private static EndpointDto getNormalEndpointDto() {
        return EndpointDto.builder()
                .title("test endpoint")
                .description("endpoint for tests")
                .bodyTemplate(
                        Map.of(
                                "bodyStrField", ParamDto.builder()
                                        .type("str")
                                        .build(),
                                "bodyIntField", ParamDto.builder()
                                        .type("int")
                                        .build(),
                                "bodyRegexField", ParamDto.builder()
                                        .type("regex")
                                        .value(".{10}")
                                        .build(),
                                "bodyFixedField", ParamDto.builder()
                                        .type("fixed")
                                        .value("fixed string")
                                        .build()
                        )
                )
                .responseTemplate(
                        Map.of(
                                "responseStrField", ParamDto.builder()
                                        .type("str")
                                        .build(),
                                "responseIntField", ParamDto.builder()
                                        .type("int")
                                        .build(),
                                "responseRegexField", ParamDto.builder()
                                        .type("regex")
                                        .value(".{7}")
                                        .build(),
                                "responseFixedField", ParamDto.builder()
                                        .type("fixed")
                                        .value("fixed string")
                                        .build()
                        )
                )
                .build();
    }
}
