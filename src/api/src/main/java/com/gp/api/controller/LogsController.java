package com.gp.api.controller;


import com.gp.api.log.EndpointLogService;
import com.gp.api.mapper.LocalDateTimeMapper;
import com.gp.api.model.pojo.EndpointLog;
import com.gp.api.repository.EndpointLogRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/log")
@Slf4j
public class LogsController {

    @Autowired
    private EndpointLogRepository endpointLogRepository;
    @Autowired
    private EndpointLogService endpointLogService;
    @Autowired
    private LocalDateTimeMapper localDateTimeMapper;

    @GetMapping("/{endpointId}")
    @ApiOperation(value = "Get endpoint's logs by id.",
            notes = "If both start and end is null then all logs are returned. " +
                    "If start is null then all logs before end are returned. " +
                    "If end is null then all logs after start are returned. " +
                    "If both are present then logs in the range are returned.")
    @ApiResponse(code = 400, message = "Date range is invalid")
    @SneakyThrows
    public List<EndpointLog> getLogs(@PathVariable(name = "endpointId") UUID endpointID,
                                     @RequestParam(value = "start", required = false)
                                     @ApiParam(value = "yyyy-MM-dd HH:mm:ss", example = "2023-03-31 23:59:59")
                                     String start,
                                     @RequestParam(value = "end", required = false)
                                     @ApiParam(value = "yyyy-MM-dd HH:mm:ss", example = "2023-03-31 23:59:59")
                                     String end) {
        return endpointLogService.getLogsInDateRange(endpointID,
                localDateTimeMapper.getFromString(start),
                localDateTimeMapper.getFromString(end));
    }

}
