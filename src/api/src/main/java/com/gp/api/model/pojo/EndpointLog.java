package com.gp.api.model.pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class EndpointLog {
    private UUID id;
    private UUID endpointId;
    private Map<String, String> input;
    private Map<String, String> output;
    private LocalDateTime creationDate;
}
