package com.gp.api.mapper.impl;

import com.gp.api.mapper.EndpointMapper;
import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.entity.EndpointEntity;
import com.gp.api.model.pojo.Endpoint;
import com.gp.api.model.types.Method;
import com.gp.api.validator.EndpointDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class EndpointMapperImpl extends ModelMapper implements EndpointMapper {

    private final EndpointDtoValidator endpointDtoValidator;

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
        endpointEntity.setMethod(Method.getByShortType(endpointDto.getMethod().toLowerCase()));
        return endpointEntity;
    }

    @Override
    public Endpoint fromEntityToPojo(EndpointEntity endpointEntity) {
        return this.map(endpointEntity, Endpoint.class);
    }
}
