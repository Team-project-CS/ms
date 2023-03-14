package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(405)
public class EndpointHasDifferentMethodException extends EndpointServiceException {
    public EndpointHasDifferentMethodException(String message) {
        super(message);
    }

    public EndpointHasDifferentMethodException(Throwable cause) {
        super(cause);
    }

    public EndpointHasDifferentMethodException() {
        super();
    }
}
