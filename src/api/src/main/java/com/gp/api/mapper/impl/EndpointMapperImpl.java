package com.gp.api.mapper.impl;

import com.gp.api.mapper.EndpointMapper;
import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.pojo.Endpoint;
import com.gp.api.model.types.Method;
import com.gp.api.validator.EndpointDtoValidator;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EndpointMapperImpl extends ModelMapper implements EndpointMapper {
    private static final String ENDPOINT_METHOD_IS_INVALID = "Endpoint method is invalid";

    private static final String ENDPOINT_TITLE_IS_BLANK_NULL_OR_EMPTY = "Endpoint title is null, blank or empty";

    @Autowired
    private EndpointDtoValidator endpointDtoValidator;

    @PostConstruct
    void excludeTemplateMapping() {
        this.createTypeMap(EndpointDto.class, EndpointEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(EndpointEntity::setBodyTemplate);
                    mapper.skip(EndpointEntity::setResponseTemplate);
                    mapper.skip(EndpointEntity::setMethod);
                });
    }

    @Override
    @SneakyThrows
    public EndpointEntity fromDtoToEntity(EndpointDto endpointDto) {
        endpointDtoValidator.validate(endpointDto);
        EndpointEntity endpointEntity = this.map(endpointDto, EndpointEntity.class);
        endpointEntity.setMethod(Method.getByShortType(endpointDto.getMethod()));
        return endpointEntity;
    }

    @Override
    public Endpoint fromEntityToPojo(EndpointEntity endpointEntity) {
        return this.map(endpointEntity, Endpoint.class);
    }
}
