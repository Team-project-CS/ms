package com.gp.api.service.impl;

import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.ParamType;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.repository.EndpointRepository;
import com.gp.api.service.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EndpointServiceImpl extends ModelMapper implements EndpointService {

    @Autowired
    private EndpointRepository endpointRepository;

    @PostConstruct
    void excludeTemplateMapping() {
        this.createTypeMap(EndpointDto.class, EndpointEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(EndpointEntity::setBodyTemplate);
                    mapper.skip(EndpointEntity::setResponseTemplate);
                });
    }

    @Override
    public Endpoint createEndpoint(EndpointDto endpointDto) {
        EndpointEntity endpointEntity = this.map(endpointDto, EndpointEntity.class);
        mapTemplates(endpointDto, endpointEntity);
        return this.map(endpointRepository.save(endpointEntity), Endpoint.class);
    }

    private void mapTemplates(EndpointDto endpointDto, EndpointEntity endpointEntity) {
        Map<String, ParamType> bodyTemplate = mapValuesToParamTypes(endpointDto.getBodyTemplate());
        Map<String, ParamType> responseTemplate = mapValuesToParamTypes(endpointDto.getResponseTemplate());
        endpointEntity.setBodyTemplate(bodyTemplate);
        endpointEntity.setResponseTemplate(responseTemplate);
    }

    @Override
    public void useEndpoint(UUID endpointId, Map<String, ?> body) {
        Endpoint endpoint = this.map(endpointRepository.findById(endpointId), Endpoint.class);
        checkBodyCompliance(body, endpoint.getBodyTemplate());
    }

    private void checkBodyCompliance(Map<String, ?> body, Map<String, ParamType> bodyTemplate) {
        checkParametersKeys(body, bodyTemplate);

    }

    private void checkParametersKeys(Map<String, ?> body, Map<String, ParamType> bodyTemplate) {
        boolean templateKeyNotFound = bodyTemplate.keySet().stream()
                .anyMatch(bodyDoesNotContainsKey(body));
        if (templateKeyNotFound) {
            throw new IllegalArgumentException();
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
