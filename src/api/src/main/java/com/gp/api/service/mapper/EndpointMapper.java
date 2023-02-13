package com.gp.api.service.mapper;

import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.ParamDto;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.entity.ParamEntity;

import java.util.Set;

public interface EndpointMapper {
    EndpointEntity fromDtoToEntity(EndpointDto endpointDto);

    Endpoint fromEntityToPojo(EndpointEntity endpointEntity);

    Set<ParamEntity> bodyParamsFromDto(Set<ParamDto> bodyParams);

    Set<ParamEntity> responseParamsFromDto(Set<ParamDto> responseParams);

}
