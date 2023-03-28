package com.gp.api.log.impl;

import com.gp.api.exception.throwables.InvalidDateRangeException;
import com.gp.api.log.EndpointLogService;
import com.gp.api.mapper.EndpointLogMapper;
import com.gp.api.model.entity.EndpointLogEntity;
import com.gp.api.model.pojo.EndpointLog;
import com.gp.api.repository.EndpointLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EndpointLogServiceImpl implements EndpointLogService {

    private static final String END_DATE_IS_LESS_THAN_START_DATE = "Date range is invalid: end date cannot be less than start date";

    private final EndpointLogRepository endpointLogRepository;
    private final EndpointLogMapper endpointLogMapper;

    private static boolean onlyEndDateIsSet(LocalDateTime end) {
        return end != null;
    }

    private static boolean allDatesAreSet(LocalDateTime start, LocalDateTime end) {
        return end != null && start != null;
    }

    private static boolean noDateIsSet(LocalDateTime start, LocalDateTime end) {
        return start == null && end == null;
    }

    @Override
    public void createLogEvent(UUID endpointId, Map<String, ?> input, Map<String, ?> output) {
        endpointLogRepository.save(new EndpointLogEntity(endpointId,
                endpointLogMapper.mapValuesToString(input),
                endpointLogMapper.mapValuesToString(output)));
    }

    @Override
    @SneakyThrows
    public List<EndpointLog> getLogsInDateRange(UUID endpointId, LocalDateTime start, LocalDateTime end) {
        if (noDateIsSet(start, end)) {
            return map(getAllLogs(endpointId));
        }
        if (allDatesAreSet(start, end)) {
            if (end.isBefore(start)) {
                throw new InvalidDateRangeException(END_DATE_IS_LESS_THAN_START_DATE);
            }
            return map(getLogsInRange(endpointId, start, end));
        }
        if (onlyEndDateIsSet(end)) {
            return map(getLogsBeforeEndDate(endpointId, end));
        }
        return map(getLogsAfterStartDate(endpointId, start));
    }

    private List<EndpointLogEntity> getLogsAfterStartDate(UUID endpointId, LocalDateTime start) {
        return endpointLogRepository.findByEndpointIdAndCreationDateGreaterThanEqual(endpointId, start);
    }

    private List<EndpointLogEntity> getLogsBeforeEndDate(UUID endpointId, LocalDateTime end) {
        return endpointLogRepository.findByEndpointIdAndCreationDateLessThanEqual(endpointId, end);
    }

    private List<EndpointLogEntity> getLogsInRange(UUID endpointId, LocalDateTime start, LocalDateTime end) {
        return endpointLogRepository.findByEndpointIdAndCreationDateBetween(endpointId, start, end);
    }

    private List<EndpointLogEntity> getAllLogs(UUID endpointId) {
        return endpointLogRepository.findByEndpointId(endpointId);
    }

    private List<EndpointLog> map(List<EndpointLogEntity> endpointLogEntities) {
        return endpointLogMapper.fromEntityToPojoList(endpointLogEntities);
    }
}
