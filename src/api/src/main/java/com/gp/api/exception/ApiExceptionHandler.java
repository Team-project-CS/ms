package com.gp.api.exception;

import com.gp.api.exception.throwables.EndpointServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice(basePackages = {"com.gp.api"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));

        final ApiError.ApiErrorBuilder apiErrorBuilder = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .path(req.getContextPath() + req.getServletPath());

        if(e.getClass().equals(EndpointServiceException.class)){
            int httpCode = e.getClass().getAnnotation(ApiErrorCode.class).value();
            return new ResponseEntity<>(apiErrorBuilder.message(e.getMessage()).build(),
                    HttpStatus.valueOf(httpCode));
        }

        return new ResponseEntity<>(apiErrorBuilder.message(e.getMessage()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

