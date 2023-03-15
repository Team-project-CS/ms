package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(400)
public class InvalidDateRangeException extends EndpointServiceException {
    public InvalidDateRangeException(String message) {
        super(message);
    }

    public InvalidDateRangeException(Throwable cause) {
        super(cause);
    }

    public InvalidDateRangeException() {
    }
}
