package com.gp.api.controller;


import com.gp.api.model.Endpoint;
import com.gp.api.model.EndpointDto;
import com.gp.api.service.EndpointService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation("Create endpoint")
    @ApiResponse(code = 400, message = "Type of parameter in body or response template is invalid")
    public Endpoint createEndpoint(@RequestBody EndpointDto endpointDto) {
        return endpointService.createEndpoint(endpointDto);
    }

    @PostMapping("/{endpointId}")
    @ApiOperation("Send body to specified endpoint and get randomly-generated response")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Body parameters does not match the endpoint's body template"),
            @ApiResponse(code = 400, message = "Specified endpoint is not found")
    })
    public Map<String, ?> useEndpoint(
            @PathVariable(name = "endpointId") UUID endpointID,
            @ApiParam(example = "{\n" +
                    "\"bodyField1\" : \"some string\",\n" +
                    "\"bodyField2\" : \"some int\"\n" +
                    "}",
                    value = "Body for endpoint. Must match it's template.")
            @RequestBody Map<String, ?> body
    ) {
        return endpointService.useEndpoint(endpointID, body);
    }

    @DeleteMapping("/{endpointId}")
    @ApiOperation("Delete endpoint by ID")
    @ApiResponse(code = 400, message = "Specified endpoint is not found")
    public Endpoint deleteEndpoint(@PathVariable(name = "endpointId") UUID endpointID) {
        return endpointService.deleteEndpoint(endpointID);
    }
}
