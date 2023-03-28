package com.gp.api.tests;

import com.gp.api.BaseTest;
import com.gp.api.BaseTestConfig;
import com.gp.api.exception.throwables.*;
import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.dto.ParamDto;
import com.gp.api.model.pojo.Endpoint;
import com.gp.api.model.pojo.Param;
import com.gp.api.model.types.Method;
import com.gp.api.model.types.ParamType;
import com.gp.api.repository.EndpointRepository;
import com.gp.api.service.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
    void clearTable() {
        endpointRepository.deleteAll();
    }

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

    @Test
    @DisplayName("Get pre-created endpoint")
    void happyGetEndpointTestCase() {
        EndpointDto actualEndpoint = getNormalEndpointDto();

        UUID id = endpointService.createEndpoint(actualEndpoint).getId();
        Endpoint endpoint = endpointService.getEndpoint(id);

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
    @DisplayName("Try to get endpoint that does not exist")
    void getEndpointNotFoundUseEndpointCase() {
        endpointService.createEndpoint(getNormalEndpointDto());

        assertThrows(EndpointNotFoundException.class, () -> endpointService.getEndpoint(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Get all 3 pre-created endpoints")
    void getAllThreeEndpointsTestCase() {
        endpointService.createEndpoint(getNormalEndpointDto());
        endpointService.createEndpoint(getNormalEndpointDto());
        endpointService.createEndpoint(getNormalEndpointDto());

        List<Endpoint> allEndpoints = endpointService.getAllEndpoints();

        assertEquals(3, allEndpoints.size());

    }

    @Test
    @DisplayName("Get all 0 pre-created endpoints")
    void getAllZeroEndpointsTestCase() {
        List<Endpoint> allEndpoints = endpointService.getAllEndpoints();

        assertEquals(0, allEndpoints.size());
    }

    @Test
    @DisplayName("Create a valid endpoint")
    void happyCreateEndpointTestCase() {
        EndpointDto actualEndpoint = getNormalEndpointDto();

        Endpoint endpoint = endpointService.createEndpoint(actualEndpoint);

        assertEquals(endpoint.getTitle(), actualEndpoint.getTitle());
        assertEquals(endpoint.getDescription(), actualEndpoint.getDescription());
        assertEquals(endpoint.getMethod(), Method.POST);
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
    @DisplayName("Create endpoint with upper and lower cases in param types")
    void happyUpperAndLowerCaseParamTypeCreationTestCase() {
        EndpointDto actualEndpoint = EndpointDto.builder()
                .title("test endpoint")
                .description("endpoint for tests")
                .bodyTemplate(
                        Set.of(
                                ParamDto.builder()
                                        .key("bodyStrField")
                                        .type("STR")
                                        .build(),
                                ParamDto.builder()
                                        .key("bodyIntField")
                                        .type("iNt")
                                        .build(),
                                ParamDto.builder()
                                        .key("bodyRegexField")
                                        .type("rEgEx")
                                        .value(".{1,10}")
                                        .build(),
                                ParamDto.builder()
                                        .key("bodyFixedField")
                                        .type("fixeD")
                                        .value("fixed string")
                                        .build()
                        )
                )
                .responseTemplate(
                        Set.of(
                                ParamDto.builder()
                                        .key("responseStrField")
                                        .type("StR")
                                        .build(),
                                ParamDto.builder()
                                        .key("responseIntField")
                                        .type("INt")
                                        .build(),
                                ParamDto.builder()
                                        .key("responseRegexField")
                                        .type("reGex")
                                        .value(".{1,10}")
                                        .build(),
                                ParamDto.builder()
                                        .key("responseFixedField")
                                        .type("FIXED")
                                        .value("fixed string")
                                        .build()
                        )
                )
                .method("post")
                .build();

        assertDoesNotThrow(() -> endpointService.createEndpoint(actualEndpoint));
    }

    @Test
    @DisplayName("Try to create endpoint with invalid param type value in body template")
    void InvalidBodyParameterCreateEndpointTestCase() {
        EndpointDto endpointDto = EndpointDto.builder()
                .title("endpoint")
                .bodyTemplate(
                        Set.of(
                                ParamDto.builder()
                                        .key("bodyStrField")
                                        .type("invalid type")
                                        .build()
                        )
                )
                .method("post")
                .build();

        assertThrows(InvalidBodyTemplateException.class, () -> endpointService.createEndpoint(endpointDto));
    }

    @Test
    @DisplayName("Try to create endpoint with invalid param type value in response template")
    void InvalidResponseParameterCreateEndpointTestCase() {
        EndpointDto endpointDto = EndpointDto.builder()
                .title("endpoint")
                .responseTemplate(
                        Set.of(
                                ParamDto.builder()
                                        .key("responseStrField")
                                        .type("invalid type")
                                        .build()
                        )
                )
                .method("post")
                .build();

        assertThrows(InvalidResponseTemplateException.class, () -> endpointService.createEndpoint(endpointDto));
    }

    @ParameterizedTest
    @DisplayName("Try to create endpoint with invalid method type")
    @ValueSource(strings = {"", "   ", "postpostpost", "abc"})
    @NullSource
    void InvalidMethodTypeCreateEndpointTestCase(String method) {
        EndpointDto endpointDto = EndpointDto.builder()
                .title("endpoint")
                .method(method)
                .build();

        assertThrows(InvalidEndpointMethodException.class, () -> endpointService.createEndpoint(endpointDto));
    }

    @ParameterizedTest
    @DisplayName("Try to create endpoint with invalid title")
    @ValueSource(strings = {"", "   "})
    @NullSource
    void InvalidMethodTitleCreateEndpointTestCase(String title) {
        EndpointDto endpointDto = EndpointDto.builder()
                .title(title)
                .method("post")
                .build();

        assertThrows(InvalidEndpointTitleException.class, () -> endpointService.createEndpoint(endpointDto));
    }

    @Test
    @DisplayName("Try to use endpoint that does not exist")
    void endpointNotFoundUseEndpointCase() {
        endpointService.createEndpoint(getNormalEndpointDto());

        assertThrows(EndpointNotFoundException.class, () -> endpointService.useEndpoint(UUID.randomUUID(), Map.of(), Method.POST));
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

        assertTrue(response.get("responseStrField") instanceof String);
        assertDoesNotThrow(() -> Integer.parseInt(response.get("responseIntField").toString()));
        assertEquals(response.get("responseFixedField"), "fixed string");
    }

    @Test
    @DisplayName("Invalid use of pre-created endpoint: endpoint method is different from specified")
    void endpointMethodIsDifferentTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        UUID id = endpointService.createEndpoint(normalEndpointDto).getId();

        EndpointHasDifferentMethodException e = assertThrows(EndpointHasDifferentMethodException.class, () ->
                endpointService.useEndpoint(
                        id,
                        Map.of(
                                "bodyStrField", "some string",
                                "bodyIntField", 1024,
                                "bodyRegexField", "ab1?%",
                                "bodyFixedField", "fixed string"
                        ),
                        Method.GET));
        assertEquals(e.getMessage(), "Endpoint's method is post, got get");
    }

    @Test
    @DisplayName("Invalid use of pre-created endpoint: mandatory parameter is not specified")
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
                        ),
                        Method.POST));
        assertEquals(e.getMessage(), "Mandatory parameter bodyStrField not found");
    }

    @Test
    @DisplayName("Invalid use of pre-created endpoint: parameter type is mismatched")
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
                        ),
                        Method.POST));
        assertEquals(e.getMessage(), "Body parameter bodyIntField value is invalid");
    }

    @Test
    @DisplayName("Invalid use of pre-created endpoint: regex parameter value is mismatched")
    void regexInvalidParameterTypeUseEndpointTestCase() {
        EndpointDto normalEndpointDto = getNormalEndpointDto();

        Endpoint endpoint = endpointService.createEndpoint(normalEndpointDto);

        ParameterTypeMismatchException e = assertThrows(ParameterTypeMismatchException.class, () ->
                endpointService.useEndpoint(
                        endpoint.getId(),
                        Map.of(
                                "bodyStrField", "some string",
                                "bodyIntField", 1024,
                                "bodyRegexField", "it must have only 10 symbols",
                                "bodyFixedField", "fixed string"
                        ),
                        Method.POST));
        assertEquals(e.getMessage(), "Body parameter bodyRegexField value does not match regex [.{1,10}]");
    }

    @Test
    @DisplayName("Delete pre-created endpoint")
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
    @DisplayName("Try to delete endpoint that does not exist")
    void endpointNotFoundDeleteEndpointCate() {
        endpointService.createEndpoint(getNormalEndpointDto());

        assertThrows(EndpointNotFoundException.class, () -> endpointService.deleteEndpoint(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Invalid use of pre-created endpoint: fixed parameter value is mismatched")
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
                        ),
                        Method.POST));
        assertEquals(e.getMessage(), "Body parameter bodyFixedField value does not match fixed value [fixed string]");
    }
}
