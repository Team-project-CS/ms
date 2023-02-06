package com.gp.api.service;

import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;

public interface EndpointService {
    Endpoint createEndpoint(EndpointDto endpointDto);
}
