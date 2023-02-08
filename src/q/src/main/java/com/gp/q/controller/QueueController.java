package com.gp.q.controller;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.service.QueueService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/queue")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @PostMapping
    @ApiOperation("Create queue")
    ResponseEntity<QueueMessageDto> createQueue(@Valid @RequestBody QueueMessageDto queueMessageDto) {
        QueueMessageDto dto = queueService.createQueue(queueMessageDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<QueueMessageDto> get(@RequestParam String id) {
        return ResponseEntity.ok(queueService.getQueue(id));
    }
}
