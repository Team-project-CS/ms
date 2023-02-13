package com.gp.q.controller;

import com.gp.q.model.dto.QueueMessageDto;
import com.gp.q.model.dto.QueueMessagePeriodDto;
import com.gp.q.service.QueueService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/queue/history")
public class QueueHistoryController {
    @Autowired
    private QueueService queueService;

    @GetMapping("/{queueName}")
    @ApiOperation("return all messages ever received in the queue")
    ResponseEntity<List<QueueMessageDto>> getMessagesByQueueName(@PathVariable String queueName) {
        return ResponseEntity.ok(queueService.getAllMessages(queueName));
    }

    @GetMapping
    @ApiOperation("return all messages ever received in the queue for the specified period")
    ResponseEntity<List<QueueMessageDto>> getMessagesByDate(@RequestBody QueueMessagePeriodDto dto) {
        List<QueueMessageDto> allMessages = queueService.getAllMessages(dto.getQueueName(), dto.getStartDate(), dto.getEndDate());

        return ResponseEntity.ok(allMessages);
    }

}
