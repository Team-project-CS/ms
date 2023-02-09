package com.gp.q.controller;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.dto.QueuePropertyDto;
import com.gp.q.service.QueueManagementService;
import com.gp.q.service.QueueService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/queue")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @Autowired
    private QueueManagementService managementService;

    @PostMapping("/list")
    @ApiOperation("create queues")
    ResponseEntity<List<QueuePropertyDto>> createQueues(@Valid @RequestBody List<QueuePropertyDto> list) {
        List<QueuePropertyDto> queues = managementService.createQueues(list);
        return ResponseEntity.ok(queues);
    }

    @GetMapping("/list")
    @ApiOperation("get list of queues")
    ResponseEntity<List<QueuePropertyDto>> getQueues() {
        List<QueuePropertyDto> queues = managementService.getQueues();
        return ResponseEntity.ok(queues);
    }

    @PutMapping("/list")
    @ApiOperation("update list of queues")
    ResponseEntity<List<QueuePropertyDto>> updateQueues() {
        // todo: update
        throw new RuntimeException("not implemented");
    }

    @DeleteMapping("/list")
    @ApiOperation("delete single queue")
    ResponseEntity<List<QueuePropertyDto>> deleteQueue() {
        // todo: delete
        throw new RuntimeException("not implemented");
    }



    @PostMapping
    @ApiOperation("post message in queue")
    ResponseEntity<QueueMessageDto> postMessageInQueue(@Valid @RequestBody QueueMessageDto queueMessageDto) {
        QueueMessageDto dto = queueService.pushInQueue(queueMessageDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @ApiOperation("get message from queue")
    public ResponseEntity<QueueMessageDto> getMessageFromQueue(@RequestParam String queue) {
        return ResponseEntity.ok(queueService.popFromQueue(queue));
    }
}
