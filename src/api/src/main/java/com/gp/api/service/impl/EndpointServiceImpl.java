package com.gp.api.service.impl;

import com.gp.api.exception.throwables.EndpointNotFoundException;
import com.gp.api.exception.throwables.MandatoryParameterNotSpecifiedException;
import com.gp.api.exception.throwables.ParameterTypeMismatchException;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.Param;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.types.BodyParamType;
import com.gp.api.repository.EndpointRepository;
import com.gp.api.service.EndpointService;
import com.gp.api.service.ResponseGenerator;
import com.gp.api.service.mapper.EndpointMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class EndpointServiceImpl implements EndpointService {

    private static final String ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND = "Endpoint with specified ID not found";
    private static final String MANDATORY_PARAMETER_NOT_FOUND = "Mandatory parameter %s not found";
    private static final String PARAMETER_VALUE_IS_INVALID = "Body parameter %s value is invalid";
    private static final String PARAMETER_VALUE_DOES_NOT_MATCH_REGEX = "Body parameter %s value does not match regex [%s]";
    private static final String PARAMETER_VALUE_DOES_NOT_MATCH_FIXED = "Body parameter %s value does not match fixed value [%s]";


    @Autowired
    private EndpointRepository endpointRepository;
    @Autowired
    private ResponseGenerator responseGenerator;
    @Autowired
    private EndpointMapper endpointMapper;

    @Override
    public Endpoint createEndpoint(EndpointDto endpointDto) {
        EndpointEntity endpointEntity = endpointMapper.fromDtoToEntity(endpointDto);
        return endpointMapper.fromEntityToPojo(endpointRepository.save(endpointEntity));
    }

    @Override
    @SneakyThrows
    public Map<String, ?> useEndpoint(UUID endpointId, Map<String, ?> body) {
        EndpointEntity endpointEntity = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new EndpointNotFoundException(ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND));
        Endpoint endpoint = endpointMapper.fromEntityToPojo(endpointEntity);
        checkBodyCompliance(body, endpoint.getBodyTemplate());
        return responseGenerator.generateResponse(endpoint.getResponseTemplate());
    }

    @Override
    @SneakyThrows
    public Endpoint deleteEndpoint(UUID endpointId) {
        EndpointEntity endpointEntity = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new EndpointNotFoundException(ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND));
        endpointRepository.deleteById(endpointId);
        return endpointMapper.fromEntityToPojo(endpointEntity);
    }

    private void checkBodyCompliance(Map<String, ?> body, Map<String, Param<BodyParamType>> bodyTemplate) {
        checkMandatoryBodyFieldsPresense(body, bodyTemplate);
        checkParametersValues(body, bodyTemplate);
    }

    private void checkParametersValues(Map<String, ?> body, Map<String, Param<BodyParamType>> bodyTemplate) {
        bodyTemplate.keySet()
                .forEach(param -> checkBodyParamValue(body.get(param), bodyTemplate.get(param), param));
    }

    @SneakyThrows
    private void checkBodyParamValue(Object bodyParam, Param<BodyParamType> bodyParamType, String param) {
        String value = ((Param<BodyParamType>) bodyParam).getValue();
        switch (bodyParamType.getType()) {
            case INTEGER:
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new ParameterTypeMismatchException(String.format(PARAMETER_VALUE_IS_INVALID, param));
                }
                break;
            case REGEX:
                boolean isValueMatched = value.matches(bodyParamType.getValue());
                if (!isValueMatched) {
                    throw new ParameterTypeMismatchException(String.format(PARAMETER_VALUE_DOES_NOT_MATCH_REGEX, param, value));
                }
                break;
            case FIXED:
                boolean valueDoesNotEquals = !value.equals(bodyParamType.getValue());
                if (valueDoesNotEquals) {
                    throw new ParameterTypeMismatchException(String.format(PARAMETER_VALUE_DOES_NOT_MATCH_FIXED, param, value));
                }
            default:
                break;
        }
    }

    @SneakyThrows
    private void checkMandatoryBodyFieldsPresense(Map<String, ?> body, Map<String, Param<BodyParamType>> bodyTemplate) {
        Optional<String> notFoundMandatoryParam = bodyTemplate.keySet().stream()
                .filter(bodyDoesNotContainsKey(body))
                .findAny();
        if (notFoundMandatoryParam.isPresent()) {
            throw new MandatoryParameterNotSpecifiedException(String.format(MANDATORY_PARAMETER_NOT_FOUND, notFoundMandatoryParam.get()));
        }
    }

    private Predicate<String> bodyDoesNotContainsKey(Map<String, ?> body) {
        return key -> !body.containsKey(key);
    }
}
