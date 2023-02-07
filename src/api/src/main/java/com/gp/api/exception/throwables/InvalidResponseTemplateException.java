package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(400)
public class InvalidResponseTemplateException extends EndpointServiceException {
    public InvalidResponseTemplateException(String message) {
        super(message);
    }

    public InvalidResponseTemplateException(Throwable cause) {
        super(cause);
    }
}
