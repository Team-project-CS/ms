package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(400)
public class InvalidEndpointTitleException extends EndpointServiceException {
    public InvalidEndpointTitleException(String message) {
        super(message);
    }

    public InvalidEndpointTitleException(Throwable cause) {
        super(cause);
    }

    public InvalidEndpointTitleException() {
        super();
    }
}
