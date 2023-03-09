package com.gp.api.service.mapper.impl;

import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.pojo.Endpoint;
import com.gp.api.service.mapper.EndpointMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EndpointMapperImpl extends ModelMapper implements EndpointMapper {
    @PostConstruct
    void excludeTemplateMapping() {
        this.createTypeMap(EndpointDto.class, EndpointEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(EndpointEntity::setBodyTemplate);
                    mapper.skip(EndpointEntity::setResponseTemplate);
                });
    }

    @Override
    public EndpointEntity fromDtoToEntity(EndpointDto endpointDto) {
        return this.map(endpointDto, EndpointEntity.class);
    }

    @Override
    public Endpoint fromEntityToPojo(EndpointEntity endpointEntity) {
        return this.map(endpointEntity, Endpoint.class);
    }
}
