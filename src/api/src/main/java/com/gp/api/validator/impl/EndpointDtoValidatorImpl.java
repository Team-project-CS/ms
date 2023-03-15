package com.gp.api.validator.impl;

import com.gp.api.exception.throwables.InvalidEndpointMethodException;
import com.gp.api.exception.throwables.InvalidEndpointTitleException;
import com.gp.api.model.dto.EndpointDto;
import com.gp.api.model.types.Method;
import com.gp.api.validator.EndpointDtoValidator;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class EndpointDtoValidatorImpl implements EndpointDtoValidator {

    private static final String ENDPOINT_METHOD_IS_INVALID = "Endpoint method is invalid";
    private static final String ENDPOINT_TITLE_IS_BLANK_NULL_OR_EMPTY = "Endpoint title is null, blank or empty";

    @Override
    @SneakyThrows
    public void validate(EndpointDto endpointDto) {
        if (isTitleBLankOrEmpty(endpointDto.getTitle())) {
            throw new InvalidEndpointTitleException(ENDPOINT_TITLE_IS_BLANK_NULL_OR_EMPTY);
        }
        try {
            Method.getByShortType(endpointDto.getMethod().toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEndpointMethodException(ENDPOINT_METHOD_IS_INVALID);
        }
    }

    private boolean isTitleBLankOrEmpty(String title) {
        return StringUtils.isEmpty(title) || StringUtils.isBlank(title);
    }

}
