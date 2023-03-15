package com.gp.api.mapper;

import com.gp.api.model.entity.EndpointLogEntity;
import com.gp.api.model.pojo.EndpointLog;

import java.util.List;
import java.util.Map;

public interface EndpointLogMapper {
    List<EndpointLog> fromEntityToPojoList(List<EndpointLogEntity> endpointLogEntities);

    Map<String, String> mapValuesToString(Map<String, ?> map);
}
