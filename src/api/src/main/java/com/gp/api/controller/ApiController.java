package com.gp.api.controller;


import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.service.EndpointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
