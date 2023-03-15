package com.gp.api.mapper.impl;

import com.gp.api.exception.throwables.InvalidDateRangeException;
import com.gp.api.mapper.LocalDateTimeMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class LocalDateTimeMapperImpl implements LocalDateTimeMapper {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String DATE_RANGE_IS_INVALID = "Date range must be either null or match yyyy-MM-dd HH:mm:ss";

    @Override
    @SneakyThrows
    public LocalDateTime getFromString(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTime, format);
        } catch (DateTimeParseException e) {
            throw new InvalidDateRangeException(DATE_RANGE_IS_INVALID);
        }
    }
}
