package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(400)
public class InvalidEndpointMethodException extends EndpointServiceException {
    public InvalidEndpointMethodException(String message) {
        super(message);
    }

    public InvalidEndpointMethodException(Throwable cause) {
        super(cause);
    }

    public InvalidEndpointMethodException() {
    }
}
