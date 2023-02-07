package com.gp.api.service;

import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;

import java.util.Map;
import java.util.UUID;

public interface EndpointService {
    Endpoint createEndpoint(EndpointDto endpointDto);
    Map<String, ?> useEndpoint(UUID endpointId, Map<String, ?> body);
    Endpoint deleteEndpoint(UUID endpointId);

}
