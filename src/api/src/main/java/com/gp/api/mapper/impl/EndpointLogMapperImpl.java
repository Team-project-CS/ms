package com.gp.api.mapper.impl;

import com.gp.api.mapper.EndpointLogMapper;
import com.gp.api.model.entity.EndpointLogEntity;
import com.gp.api.model.pojo.EndpointLog;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EndpointLogMapperImpl extends ModelMapper implements EndpointLogMapper {

    private EndpointLog fromEntityToPojo(EndpointLogEntity endpointLogEntity) {
        return this.map(endpointLogEntity, EndpointLog.class);
    }

    @Override
    public List<EndpointLog> fromEntityToPojoList(List<EndpointLogEntity> endpointLogEntities) {
        return endpointLogEntities.stream()
                .map(this::fromEntityToPojo)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> mapValuesToString(Map<String, ?> input) {
        return input.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
