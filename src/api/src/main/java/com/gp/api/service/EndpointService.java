package com.gp.api.service;

import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.pojo.Endpoint;
import com.gp.api.model.types.Method;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface EndpointService {
    Endpoint createEndpoint(EndpointDto endpointDto);

    Endpoint getEndpoint(UUID endpointId);

    List<Endpoint> getAllEndpoints();

    Map<String, ?> useEndpoint(UUID endpointId, Map<String, ?> body, Method method);

    Endpoint deleteEndpoint(UUID endpointId);

}
