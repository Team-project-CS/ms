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
import java.util.stream.Collectors;

@Service
@Slf4j
public class EndpointServiceImpl extends ModelMapper implements EndpointService {

    @Autowired
    private EndpointRepository endpointRepository;

    @PostConstruct
    void excludeTemplateMapping() {
        this.createTypeMap(EndpointDto.class, EndpointEntity.class)
                .addMappings(mapper -> mapper.skip(EndpointEntity::setTemplate));
    }

    @Override
    public Endpoint createEndpoint(EndpointDto endpointDto) {
        EndpointEntity endpointEntity = this.map(endpointDto, EndpointEntity.class);
        Map<String, ParamType> templateWithParamTypesAsValues = mapValuesToParamTypes(endpointDto.getTemplate());
        endpointEntity.setTemplate(templateWithParamTypesAsValues);
        return this.map(endpointRepository.save(endpointEntity), Endpoint.class);
    }

    private static Map<String, ParamType> mapValuesToParamTypes(Map<String, ?> template) {
        return template.entrySet().stream()
                .map(EndpointServiceImpl::castToEntryWithParamType)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Map.Entry<String, ParamType> castToEntryWithParamType(Map.Entry<String, ?> entry) {
        return Map.entry(entry.getKey(), getParamTypeEquivalent(entry.getValue()));
    }

    private static ParamType getParamTypeEquivalent(Object value) {
        return ParamType.getByShortType(value.toString());
    }
}
