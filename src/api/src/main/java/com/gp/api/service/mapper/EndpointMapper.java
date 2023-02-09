package com.gp.api.service.mapper;

import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.model.entity.EndpointEntity;

public interface EndpointMapper {
    EndpointEntity fromDtoToEntity(EndpointDto endpointDto);
    Endpoint fromEntityToPojo(EndpointEntity endpointEntity);
}
