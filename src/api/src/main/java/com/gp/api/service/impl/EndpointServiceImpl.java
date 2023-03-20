package com.gp.api.service.impl;

import com.gp.api.exception.throwables.EndpointHasDifferentMethodException;
import com.gp.api.exception.throwables.EndpointNotFoundException;
import com.gp.api.exception.throwables.MandatoryParameterNotSpecifiedException;
import com.gp.api.exception.throwables.ParameterTypeMismatchException;
import com.gp.api.log.EndpointLogService;
import com.gp.api.mapper.EndpointMapper;
import com.gp.api.mapper.ParamsMapper;
import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.dto.ParamDto;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.entity.ParamEntity;
import com.gp.api.model.pojo.Endpoint;
import com.gp.api.model.pojo.Param;
import com.gp.api.model.types.Method;
import com.gp.api.repository.EndpointRepository;
import com.gp.api.repository.ParamRepository;
import com.gp.api.service.EndpointService;
import com.gp.api.service.ResponseGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EndpointServiceImpl implements EndpointService {

    private static final String ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND = "Endpoint with specified ID not found";
    private static final String MANDATORY_PARAMETER_NOT_FOUND = "Mandatory parameter %s not found";
    private static final String PARAMETER_VALUE_IS_INVALID = "Body parameter %s value is invalid";
    private static final String PARAMETER_VALUE_DOES_NOT_MATCH_REGEX = "Body parameter %s value does not match regex [%s]";
    private static final String PARAMETER_VALUE_DOES_NOT_MATCH_FIXED = "Body parameter %s value does not match fixed value [%s]";
    private static final String ENDPOINT_HAS_DIFFERENT_METHOD = "Endpoint's method is %s, got %s";

    private final EndpointRepository endpointRepository;
    private final ParamRepository paramRepository;
    private final ResponseGenerator responseGenerator;
    private final EndpointMapper endpointMapper;
    private final ParamsMapper paramsMapper;
    private final EndpointLogService endpointLogService;

    private static Predicate<Param> bodyDoesNotContainsKey(Map<String, ?> body) {
        return param -> !body.containsKey(param.getKey());
    }

    @Override
    public Endpoint createEndpoint(EndpointDto endpointDto) {
        EndpointEntity endpointEntity = endpointRepository.save(endpointMapper.fromDtoToEntity(endpointDto));
        Set<ParamEntity> savedBodyTemplate = saveBodyTemplate(endpointDto.getBodyTemplate(), endpointEntity);
        Set<ParamEntity> savedResponseTemplate = saveResponseTemplate(endpointDto.getResponseTemplate(), endpointEntity);
        endpointEntity.getBodyTemplate().addAll(savedBodyTemplate);
        endpointEntity.getResponseTemplate().addAll(savedResponseTemplate);
        return endpointMapper.fromEntityToPojo(endpointEntity);
    }

    @Override
    @SneakyThrows
    public Endpoint getEndpoint(UUID endpointId) {
        EndpointEntity endpointEntity = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new EndpointNotFoundException(ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND));
        return endpointMapper.fromEntityToPojo(endpointEntity);
    }

    @Override
    public List<Endpoint> getAllEndpoints() {
        return IterableUtils.toList(endpointRepository.findAll()).stream()
                .map(endpointMapper::fromEntityToPojo)
                .collect(Collectors.toList());
    }

    private Set<ParamEntity> saveBodyTemplate(Set<ParamDto> bodyTemplate, EndpointEntity endpointEntity) {
        return paramsMapper.bodyParamsFromDto(bodyTemplate).stream()
                .map(paramEntity -> {
                    paramEntity.setBodyEndpointEntity(endpointEntity);
                    return paramRepository.save(paramEntity);
                })
                .collect(Collectors.toSet());
    }

    private Set<ParamEntity> saveResponseTemplate(Set<ParamDto> responseTemplate, EndpointEntity endpointEntity) {
        return paramsMapper.responseParamsFromDto(responseTemplate).stream()
                .map(paramEntity -> {
                    paramEntity.setResponseEndpointEntity(endpointEntity);
                    return paramRepository.save(paramEntity);
                })
                .collect(Collectors.toSet());
    }

    private static void checkEndpointMethod(Method targetMethod, EndpointEntity endpointEntity) throws EndpointHasDifferentMethodException {
        if (!endpointEntity.getMethod().equals(targetMethod)) {
            throw new EndpointHasDifferentMethodException(String.format(ENDPOINT_HAS_DIFFERENT_METHOD,
                    endpointEntity.getMethod().getShortType(),
                    targetMethod.getShortType()));
        }
    }

    @Override
    @SneakyThrows
    public Map<String, ?> useEndpoint(UUID endpointId, Map<String, ?> body, Method targetMethod) {
        EndpointEntity endpointEntity = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new EndpointNotFoundException(ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND));
        checkEndpointMethod(targetMethod, endpointEntity);
        Endpoint endpoint = endpointMapper.fromEntityToPojo(endpointEntity);
        checkBodyCompliance(body, endpoint.getBodyTemplate());
        Map<String, ?> response = responseGenerator.generateResponse(endpoint.getResponseTemplate());
        endpointLogService.createLogEvent(endpointId, body, response);
        return response;
    }

    @Override
    @SneakyThrows
    public Endpoint deleteEndpoint(UUID endpointId) {
        EndpointEntity endpointEntity = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new EndpointNotFoundException(ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND));
        endpointRepository.deleteById(endpointId);
        return endpointMapper.fromEntityToPojo(endpointEntity);
    }

    private void checkBodyCompliance(Map<String, ?> body, Set<Param> bodyTemplate) {
        checkMandatoryBodyFieldsPresense(body, bodyTemplate);
        checkParametersValues(body, bodyTemplate);
    }

    private void checkParametersValues(Map<String, ?> body, Set<Param> bodyTemplate) {
        bodyTemplate.forEach(param -> checkBodyParamValue(body.get(param.getKey()), param));
    }

    @SneakyThrows
    private void checkBodyParamValue(Object bodyParam, Param param) {
        String value = bodyParam.toString();
        switch (param.getType()) {
            case INTEGER:
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new ParameterTypeMismatchException(String.format(PARAMETER_VALUE_IS_INVALID, param.getKey()));
                }
                break;
            case REGEX:
                boolean isValueMatched = value.matches(param.getValue());
                if (!isValueMatched) {
                    throw new ParameterTypeMismatchException(String.format(PARAMETER_VALUE_DOES_NOT_MATCH_REGEX, param.getKey(), param.getValue()));
                }
                break;
            case FIXED:
                boolean valueDoesNotEquals = !value.equals(param.getValue());
                if (valueDoesNotEquals) {
                    throw new ParameterTypeMismatchException(String.format(PARAMETER_VALUE_DOES_NOT_MATCH_FIXED, param.getKey(), param.getValue()));
                }
            default:
                break;
        }
    }

    @SneakyThrows
    private void checkMandatoryBodyFieldsPresense(Map<String, ?> body, Set<Param> bodyTemplate) {
        Optional<Param> notFoundMandatoryParam = bodyTemplate.stream()
                .filter(bodyDoesNotContainsKey(body))
                .findAny();
        if (notFoundMandatoryParam.isPresent()) {
            throw new MandatoryParameterNotSpecifiedException(String.format(MANDATORY_PARAMETER_NOT_FOUND, notFoundMandatoryParam.get().getKey()));
        }
    }
}
