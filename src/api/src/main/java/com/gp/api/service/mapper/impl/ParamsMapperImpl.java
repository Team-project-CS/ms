package com.gp.api.service.mapper.impl;

import com.gp.api.exception.throwables.InvalidBodyTemplateException;
import com.gp.api.exception.throwables.InvalidResponseTemplateException;
import com.gp.api.model.dto.ParamDto;
import com.gp.api.model.entity.ParamEntity;
import com.gp.api.model.types.ParamType;
import com.gp.api.service.mapper.ParamsMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ParamsMapperImpl implements ParamsMapper {

    private static final String BODY_TEMPLATE_HAS_INVALID_TYPES = "Body template has invalid types";
    private static final String RESPONSE_TEMPLATE_HAS_INVALID_TYPES = "Response template has invalid types";

    @Override
    @SneakyThrows
    public Collection<ParamEntity> bodyParamsFromDto(Collection<ParamDto> bodyParams) {
        try {
            return mapTemplate(bodyParams, ParamType.BODY_TYPES);
        } catch (IllegalArgumentException e) {
            throw new InvalidBodyTemplateException(BODY_TEMPLATE_HAS_INVALID_TYPES);
        }
    }

    @Override
    @SneakyThrows
    public Collection<ParamEntity> responseParamsFromDto(Collection<ParamDto> responseParams) {
        try {
            return mapTemplate(responseParams, ParamType.RESPONSE_TYPES);
        } catch (IllegalArgumentException e) {
            throw new InvalidResponseTemplateException(RESPONSE_TEMPLATE_HAS_INVALID_TYPES);
        }
    }

    private Collection<ParamEntity> mapTemplate(Collection<ParamDto> template, ParamType[] bodyTypes) {
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

    private ParamType getType(ParamDto paramDto, ParamType[] types) {
        return Arrays.stream(types)
                .filter(type -> type.getShortType().equals(paramDto.getType().toLowerCase()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
