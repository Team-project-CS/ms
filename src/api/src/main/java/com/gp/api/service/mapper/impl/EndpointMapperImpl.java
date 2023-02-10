package com.gp.api.service.mapper.impl;

import com.gp.api.exception.throwables.InvalidBodyTemplateException;
import com.gp.api.exception.throwables.InvalidResponseTemplateException;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.Param;
import com.gp.api.model.ParamDto;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.types.BodyParamType;
import com.gp.api.model.types.ResponseParamType;
import com.gp.api.service.mapper.EndpointMapper;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EndpointMapperImpl extends ModelMapper implements EndpointMapper {

    private static final String BODY_TEMPLATE_HAS_INVALID_TYPES = "Body template has invalid types";
    private static final String RESPONSE_TEMPLATE_HAS_INVALID_TYPES = "Response template has invalid types";

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
    public EndpointEntity fromDtoToEntity(EndpointDto endpointDto) {
        EndpointEntity endpointEntity = this.map(endpointDto, EndpointEntity.class);
        mapTemplates(endpointDto, endpointEntity);
        return endpointEntity;
    }

    @Override
    public Endpoint fromEntityToPojo(EndpointEntity endpointEntity) {
        Endpoint endpoint = this.map(endpointEntity, Endpoint.class);
        endpoint.setBodyTemplate(endpointEntity.getBodyTemplate());
        endpoint.setResponseTemplate(endpointEntity.getResponseTemplate());
        return endpoint;
    }

    @SneakyThrows
    private void mapTemplates(EndpointDto endpointDto, EndpointEntity endpointEntity) {
        Map<String, Param<BodyParamType>> bodyTemplate;
        Map<String, Param<ResponseParamType>> responseTemplate;
        try {
            bodyTemplate = mapBodyTemplate(endpointDto.getBodyTemplate());
        } catch (IllegalArgumentException e) {
            throw new InvalidBodyTemplateException(BODY_TEMPLATE_HAS_INVALID_TYPES);
        }
        try {
            responseTemplate = mapResponseTemplate(endpointDto.getResponseTemplate());
        } catch (IllegalArgumentException e) {
            throw new InvalidResponseTemplateException(RESPONSE_TEMPLATE_HAS_INVALID_TYPES);
        }
        endpointEntity.setBodyTemplate(bodyTemplate);
        endpointEntity.setResponseTemplate(responseTemplate);
    }

    private Map<String, Param<ResponseParamType>> mapResponseTemplate(Map<String, ParamDto> responseTemplate) {
        return responseTemplate.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), getResponseParamFromDto(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Param<ResponseParamType> getResponseParamFromDto(ParamDto paramDto) {
        return Param.<ResponseParamType>builder()
                .type(getResponseParamTypeFromDto(paramDto))
                .value(paramDto.getValue())
                .build();
    }

    private ResponseParamType getResponseParamTypeFromDto(ParamDto paramDto) {
        return Arrays.stream(ResponseParamType.values())
                .filter(type -> type.getShortType().equals(paramDto.getType()))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    private Map<String, Param<BodyParamType>> mapBodyTemplate(Map<String, ParamDto> bodyTemplate) {
        return bodyTemplate.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), getBodyParamFromDto(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Param<BodyParamType> getBodyParamFromDto(ParamDto paramDto) {
        return Param.<BodyParamType>builder()
                .type(getBodyParamTypeFromDto(paramDto))
                .value(paramDto.getValue())
                .build();
    }

    private BodyParamType getBodyParamTypeFromDto(ParamDto paramDto) {
        return Arrays.stream(BodyParamType.values())
                .filter(type -> type.getShortType().equals(paramDto.getType()))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }
}
