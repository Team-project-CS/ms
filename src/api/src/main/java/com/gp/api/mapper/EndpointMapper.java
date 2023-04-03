package com.gp.api.mapper;

import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.pojo.Endpoint;

public interface EndpointMapper {
    EndpointEntity fromDtoToEntity(EndpointDto endpointDto);

    Endpoint fromEntityToPojo(EndpointEntity endpointEntity);

}
