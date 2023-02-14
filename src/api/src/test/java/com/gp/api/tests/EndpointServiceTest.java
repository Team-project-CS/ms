package com.gp.api.tests;

import com.gp.api.BaseTest;
import com.gp.api.BaseTestConfig;
import com.gp.api.exception.throwables.*;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.Param;
import com.gp.api.model.ParamDto;
import com.gp.api.model.types.ParamType;
import com.gp.api.service.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({BaseTestConfig.class})
@Slf4j
public class EndpointServiceTest extends BaseTest {

    @Autowired
    private EndpointService endpointService;

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
                .build();
    }

    @Test
    void happyCreateEndpointTestCase() {
        EndpointDto actualEndpoint = getNormalEndpointDto();

        Endpoint endpoint = endpointService.createEndpoint(actualEndpoint);

        assertEquals(endpoint.getTitle(), actualEndpoint.getTitle());
        assertEquals(endpoint.getDescription(), actualEndpoint.getDescription());
        assertEquals(
                endpoint.getBodyTemplate(),
                Set.of(
                        Param.builder()
                                .key("bodyStrField")
                                .type(ParamType.STRING)
                                .build(),
                        Param.builder()
                                .key("bodyIntField")
                                .type(ParamType.INTEGER)
                                .build(),
                        Param.builder()
                                .key("bodyRegexField")
                                .type(ParamType.REGEX)
                                .value(".{1,10}")
                                .build(),
                        Param.builder()
                                .key("bodyFixedField")
                                .type(ParamType.FIXED)
                                .value("fixed string")
                                .build()
                )
        );
        assertEquals(
                endpoint.getResponseTemplate(),
                Set.of(
                        Param.builder()
                                .key("responseStrField")
                                .type(ParamType.STRING)
                                .build(),
                        Param.builder()
                                .key("responseIntField")
                                .type(ParamType.INTEGER)
                                .build(),
                        Param.builder()
                                .key("responseRegexField")
                                .type(ParamType.REGEX)
                                .value(".{1,10}")
                                .build(),
                        Param.builder()
                                .key("responseFixedField")
                                .type(ParamType.FIXED)
                                .value("fixed string")
                                .build()
                )
        );
    }

    @Test
    void InvalidBodyParameterCreateEndpointTestCase() {
        EndpointDto endpointDto = EndpointDto.builder()
                .bodyTemplate(
                        Set.of(
                                ParamDto.builder()
                                        .key("bodyStrField")
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
                        Set.of(
                                ParamDto.builder()
                                        .key("responseStrField")
                                        .type("invalid type")
                                        .build()
                        )
                )
                .build();

        assertThrows(InvalidResponseTemplateException.class, () -> endpointService.createEndpoint(endpointDto));
    }

    @Test
    void endpointNotFoundUseEndpointCase() {
        endpointService.createEndpoint(getNormalEndpointDto());

        assertThrows(EndpointNotFoundException.class, () -> endpointService.useEndpoint(UUID.randomUUID(), Map.of()));
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
                        "bodyFixedField", "fixed string"
                )
        );

        assertTrue(response.get("responseStrField") instanceof String);
        assertDoesNotThrow(() -> Integer.parseInt(response.get("responseIntField").toString()));
        //assertTrue(response.get("responseRegexField").toString().matches(".{1,10}"));
        assertEquals(response.get("responseFixedField"), "fixed string");
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
                                "bodyFixedField", "fixed string"
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
                                "bodyFixedField", "fixed string"
                        )
                ));
        assertEquals(e.getMessage(), "Body parameter bodyIntField value is invalid");
    }

    @Test
    void regexInvalidParameterTypeUseEndpointTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        Endpoint id = endpointService.createEndpoint(normalEndpointDto);

        ParameterTypeMismatchException e = assertThrows(ParameterTypeMismatchException.class, () ->
                endpointService.useEndpoint(
                        id.getId(),
                        Map.of(
                                "bodyStrField", "some string",
                                "bodyIntField", 1024,
                                "bodyRegexField", "it must have only 10 symbols",
                                "bodyFixedField", "fixed string"
                        )
                ));
        assertEquals(e.getMessage(), "Body parameter bodyRegexField value does not match regex [.{1,10}]");
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
                                "bodyFixedField", "this is wrong fixed string"
                        )
                ));
        assertEquals(e.getMessage(), "Body parameter bodyFixedField value does not match fixed value [fixed string]");
    }
}
