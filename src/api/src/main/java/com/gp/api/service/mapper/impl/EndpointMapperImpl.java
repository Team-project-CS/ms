package com.gp.api.service.mapper.impl;

import com.gp.api.exception.throwables.InvalidBodyTemplateException;
import com.gp.api.exception.throwables.InvalidResponseTemplateException;
import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.ParamDto;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.entity.ParamEntity;
import com.gp.api.model.types.ParamType;
import com.gp.api.service.mapper.EndpointMapper;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Set;
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
    }

    private static ParamType getType(ParamDto paramDto, ParamType[] types) {
        return Arrays.stream(types)
                .filter(type -> type.getShortType().equals(paramDto.getType().toLowerCase()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public EndpointEntity fromDtoToEntity(EndpointDto endpointDto) {
        return this.map(endpointDto, EndpointEntity.class);
    }

    @Override
    public Endpoint fromEntityToPojo(EndpointEntity endpointEntity) {
        return this.map(endpointEntity, Endpoint.class);
    }

    @Override
    @SneakyThrows
    public Set<ParamEntity> bodyParamsFromDto(Set<ParamDto> bodyParams) {
        try {
            return mapTemplate(bodyParams, ParamType.BODY_TYPES);
        } catch (IllegalArgumentException e) {
            throw new InvalidBodyTemplateException(BODY_TEMPLATE_HAS_INVALID_TYPES);
        }
    }

    @Override
    @SneakyThrows
    public Set<ParamEntity> responseParamsFromDto(Set<ParamDto> responseParams) {
        try {
            return mapTemplate(responseParams, ParamType.RESPONSE_TYPES);
        } catch (IllegalArgumentException e) {
            throw new InvalidResponseTemplateException(RESPONSE_TEMPLATE_HAS_INVALID_TYPES);
        }
    }

    private Set<ParamEntity> mapTemplate(Set<ParamDto> template, ParamType[] bodyTypes) {
        if (template == null) {
            return Set.of();
        }
        return template.stream()
                .map(paramDto -> mapParamDto(paramDto, bodyTypes))
                .collect(Collectors.toSet());
    }

    private ParamEntity mapParamDto(ParamDto paramDto, ParamType[] types) {
        return ParamEntity.builder()
                .key(paramDto.getKey())
                .value(paramDto.getValue())
                .type(getType(paramDto, types))
                .build();
    }
}
