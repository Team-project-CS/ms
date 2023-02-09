package com.gp.api.service.impl;

import com.gp.api.exception.throwables.EndpointNotFoundException;
import com.gp.api.exception.throwables.MandatoryParameterNotSpecifiedException;
import com.gp.api.exception.throwables.ParameterTypeMismatchException;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.ParamType;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.repository.EndpointRepository;
import com.gp.api.service.EndpointService;
import com.gp.api.service.ResponseGenerator;
import com.gp.api.service.mapper.EndpointMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class EndpointServiceImpl implements EndpointService {

    private static final String ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND = "Endpoint with specified ID not found";
    private static final String MANDATORY_PARAMETER_NOT_FOUND = "Mandatory parameter not found";
    private static final String PARAMETER_MISMATCH = "Type of %s parameter is mismatched";

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

    private void checkBodyCompliance(Map<String, ?> body, Map<String, ParamType> bodyTemplate) {
        checkParametersKeys(body, bodyTemplate);
        checkParametersTypes(body, bodyTemplate);
    }

    @SneakyThrows
    private void checkParametersTypes(Map<String, ?> body, Map<String, ParamType> bodyTemplate) {
        for (String key : getIntegerParamsKeys(bodyTemplate)) {
            try {
                Integer.parseInt(body.get(key).toString());
            } catch (NumberFormatException e) {
                throw new ParameterTypeMismatchException(String.format(PARAMETER_MISMATCH, key));
            }
        }
    }

    private List<String> getIntegerParamsKeys(Map<String, ParamType> bodyTemplate) {
        return bodyTemplate.entrySet().stream()
                .filter(isIntegerParam())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static Predicate<Map.Entry<String, ParamType>> isIntegerParam() {
        return entry -> entry.getValue().equals(ParamType.INTEGER);
    }

    @SneakyThrows
    private void checkParametersKeys(Map<String, ?> body, Map<String, ParamType> bodyTemplate) {
        boolean templateKeyNotFound = bodyTemplate.keySet().stream()
                .anyMatch(bodyDoesNotContainsKey(body));
        if (templateKeyNotFound) {
            throw new MandatoryParameterNotSpecifiedException(MANDATORY_PARAMETER_NOT_FOUND);
        }
    }

    private Predicate<String> bodyDoesNotContainsKey(Map<String, ?> body) {
        return key -> !body.containsKey(key);
    }
}
