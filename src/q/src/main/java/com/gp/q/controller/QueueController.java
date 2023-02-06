package com.gp.q.controller;

import com.gp.q.model.dto.QueueCreateParams;
import com.gp.q.service.QueueService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @PostMapping
    @ApiOperation("Create queue")
    void createQueue(@RequestBody QueueCreateParams queueCreateParams) {
        queueService.createQueue(queueCreateParams);
    }
}
