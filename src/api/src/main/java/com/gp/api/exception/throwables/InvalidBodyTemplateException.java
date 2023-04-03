package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(400)
public class InvalidBodyTemplateException extends EndpointServiceException {
    public InvalidBodyTemplateException(String message) {
        super(message);
    }

    public InvalidBodyTemplateException(Throwable cause) {
        super(cause);
    }

    public InvalidBodyTemplateException() {
    }
}
