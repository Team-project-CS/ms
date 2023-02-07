package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(404)
public class EndpointNotFoundException extends EndpointServiceException {
    public EndpointNotFoundException(String message) {
        super(message);
    }
    public EndpointNotFoundException(Throwable cause) {
        super(cause);
    }

    public EndpointNotFoundException() {
        super();
    }
}
