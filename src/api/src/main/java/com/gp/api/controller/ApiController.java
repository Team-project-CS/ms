package com.gp.api.controller;


import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.pojo.Endpoint;
import com.gp.api.model.types.Method;
import com.gp.api.service.EndpointService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ApiController {

    private final EndpointService endpointService;

    @PostMapping
    @ApiOperation("Create endpoint")
    @ApiResponse(code = 400, message = "Type of parameter in body or response template is invalid")
    public Endpoint createEndpoint(@RequestBody EndpointDto endpointDto) {
        return endpointService.createEndpoint(endpointDto);
    }

    @PostMapping(path = "/use/{endpointId}")
    @ApiOperation("Send body to specified POST endpoint and get randomly-generated response")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Body parameters does not match the endpoint's body template"),
            @ApiResponse(code = 404, message = "Specified endpoint is not found"),
            @ApiResponse(code = 405, message = "Endpoint's method that you are trying to call has different value, POST is expected")
    })
    public Map<String, ?> usePostEndpoint(@PathVariable(name = "endpointId") UUID endpointID, @RequestBody Map<String, ?> body) {
        return endpointService.useEndpoint(endpointID, body, Method.POST);
    }

    @GetMapping(path = "/use/{endpointId}")
    @ApiOperation("Send body to specified GET endpoint and get randomly-generated response")
    @ApiResponses({
            @ApiResponse(code = 400, message = "URL parameters does not match the endpoint's body template"),
            @ApiResponse(code = 404, message = "Specified endpoint is not found"),
            @ApiResponse(code = 405, message = "Endpoint's method that you are trying to call has different value, GET is expected")
    })
    public Map<String, ?> useGetEndpoint(@PathVariable(name = "endpointId") UUID endpointID, @RequestParam Map<String, ?> body) {
        return endpointService.useEndpoint(endpointID, body, Method.GET);
    }

    @GetMapping("/{endpointId}")
    @ApiOperation("Get endpoint by id")
    @ApiResponse(code = 404, message = "Specified endpoint is not found")
    public Endpoint getEndpoint(@PathVariable(name = "endpointId") UUID endpointID) {
        return endpointService.getEndpoint(endpointID);
    }

    @GetMapping
    @ApiOperation("Get all created endpoints")
    public List<Endpoint> getAllEndpoints() {
        return endpointService.getAllEndpoints();
    }

    @DeleteMapping("/{endpointId}")
    @ApiOperation("Delete endpoint by ID")
    @ApiResponse(code = 404, message = "Specified endpoint is not found")
    public Endpoint deleteEndpoint(@PathVariable(name = "endpointId") UUID endpointID) {
        return endpointService.deleteEndpoint(endpointID);
    }
}
