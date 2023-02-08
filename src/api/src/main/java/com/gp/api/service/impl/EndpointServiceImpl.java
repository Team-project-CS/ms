package com.gp.api.service.impl;

import com.gp.api.exception.throwables.*;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.ParamType;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.repository.EndpointRepository;
import com.gp.api.service.EndpointService;
import com.gp.api.service.ResponseGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//TODO extract ModelMapper
@Service
@Slf4j
public class EndpointServiceImpl extends ModelMapper implements EndpointService {

    private static final String BODY_TEMPLATE_HAS_INVALID_TYPES = "Body template has invalid types";
    private static final String RESPONSE_TEMPLATE_HAS_INVALID_TYPES = "Response template has invalid types";
    private static final String ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND = "Endpoint with specified ID not found";
    private static final String MANDATORY_PARAMETER_NOT_FOUND = "Mandatory parameter not found";
    private static final String PARAMETER_MISMATCH = "Type of %s parameter is mismatched";

    @Autowired
    private EndpointRepository endpointRepository;
    @Autowired
    private ResponseGenerator responseGenerator;

    @PostConstruct
    void excludeTemplateMapping() {
        this.createTypeMap(EndpointDto.class, EndpointEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(EndpointEntity::setBodyTemplate);
                    mapper.skip(EndpointEntity::setResponseTemplate);
                });
        this.createTypeMap(EndpointEntity.class, Endpoint.class)
                .addMappings(mapper -> {
                    mapper.skip(Endpoint::setBodyTemplate);
                    mapper.skip(Endpoint::setResponseTemplate);
                });
    }

    @Override
    public Endpoint createEndpoint(EndpointDto endpointDto) {
        EndpointEntity endpointEntity = this.map(endpointDto, EndpointEntity.class);
        mapTemplates(endpointDto, endpointEntity);
        EndpointEntity savedEndpoint = endpointRepository.save(endpointEntity);
        Endpoint endpoint = this.map(savedEndpoint, Endpoint.class);
        endpoint.setBodyTemplate(endpointEntity.getBodyTemplate());
        endpoint.setResponseTemplate(endpointEntity.getResponseTemplate());
        return endpoint;
    }

    @SneakyThrows
    private void mapTemplates(EndpointDto endpointDto, EndpointEntity endpointEntity) {
        Map<String, ParamType> bodyTemplate;
        Map<String, ParamType> responseTemplate;
        try {
            bodyTemplate = mapValuesToParamTypes(endpointDto.getBodyTemplate());
        } catch (IllegalArgumentException e) {
            throw new InvalidBodyTemplateException(BODY_TEMPLATE_HAS_INVALID_TYPES);
        }
        try {
            responseTemplate = mapValuesToParamTypes(endpointDto.getResponseTemplate());
        } catch (IllegalArgumentException e) {
            throw new InvalidResponseTemplateException(RESPONSE_TEMPLATE_HAS_INVALID_TYPES);
        }
        endpointEntity.setBodyTemplate(bodyTemplate);
        endpointEntity.setResponseTemplate(responseTemplate);
    }

    @Override
    @SneakyThrows
    public Map<String, ?> useEndpoint(UUID endpointId, Map<String, ?> body) {
        EndpointEntity endpointEntity = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new EndpointNotFoundException(ENDPOINT_WITH_SPECIFIED_ID_NOT_FOUND));
        Endpoint endpoint = this.map(endpointEntity, Endpoint.class);
        endpoint.setBodyTemplate(endpointEntity.getBodyTemplate());
        endpoint.setResponseTemplate(endpointEntity.getResponseTemplate());
        checkBodyCompliance(body, endpoint.getBodyTemplate());
        return responseGenerator.generateResponse(endpoint.getResponseTemplate());
    }

    @Override
    public Endpoint deleteEndpoint(UUID endpointId) {
        EndpointEntity endpointEntity = endpointRepository.findById(endpointId).orElseThrow(IllegalAccessError::new);
        Endpoint endpoint = this.map(endpointEntity, Endpoint.class);
        endpointRepository.deleteById(endpointId);
        return endpoint;
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

    private Map<String, ParamType> mapValuesToParamTypes(Map<String, ?> template) {
        return template.entrySet().stream()
                .map(this::castToEntryWithParamType)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, ParamType> castToEntryWithParamType(Map.Entry<String, ?> entry) {
        return Map.entry(entry.getKey(), getParamTypeEquivalent(entry.getValue()));
    }

    private ParamType getParamTypeEquivalent(Object value) {
        return ParamType.getByShortType(value.toString());
    }
}
