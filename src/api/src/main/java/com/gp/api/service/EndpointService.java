package com.gp.api.service;

import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;

import java.util.Map;
import java.util.UUID;

public interface EndpointService {
    Endpoint createEndpoint(EndpointDto endpointDto);
    void useEndpoint(UUID endpointId, Map<String, ?> body);
}
