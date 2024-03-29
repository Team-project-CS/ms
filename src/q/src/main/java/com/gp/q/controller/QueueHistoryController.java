package com.gp.q.controller;

import com.gp.q.model.dto.QueueLogDto;
import com.gp.q.model.dto.QueueLogPeriodDto;
import com.gp.q.service.QueueService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/queue/history")
@CrossOrigin
public class QueueHistoryController {
    @Autowired
    private QueueService queueService;

    @GetMapping("/{queueName}")
    @ApiOperation("return all messages ever received in the queue")
    ResponseEntity<List<QueueLogDto>> getMessagesByQueueName(@PathVariable String queueName) {
        return ResponseEntity.ok(queueService.getAllMessages(queueName));
    }

    @GetMapping
    @ApiOperation(value = "return all messages ever received in the queue for the specified period", consumes = """
            {
              "name": "example_queue_name_1",
              "start": "2023-02-13T14:55:18.497007",
              "end": "2023-02-13T14:55:38.497007"
            }""")
    ResponseEntity<List<QueueLogDto>> getMessagesByDate(@RequestBody QueueLogPeriodDto dto) {
        return ResponseEntity.ok(queueService.getAllMessages(dto));
    }

}
