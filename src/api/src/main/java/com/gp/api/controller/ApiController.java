package com.gp.api.controller;


import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.service.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    @Autowired
    private EndpointService endpointService;

    @PostMapping
    public Endpoint createEndpoint(@RequestBody EndpointDto endpointDto) {
        return endpointService.createEndpoint(endpointDto);
    }

    @PostMapping("/{endpointId}")
    public void useEndpoint(@PathVariable(name = "endpointId") UUID endpointID, @RequestBody Map<String, ?> body){

    }
}
