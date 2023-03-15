package com.gp.api.log;

import com.gp.api.model.pojo.EndpointLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface EndpointLogService {
    void createLogEvent(UUID endpointId, Map<String, ?> input, Map<String, ?> output);

    List<EndpointLog> getLogsInDateRange(UUID endpointId, LocalDateTime start, LocalDateTime end);
}
