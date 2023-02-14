package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(400)
public class ParameterTypeMismatchException extends EndpointServiceException {
    public ParameterTypeMismatchException(String message) {
        super(message);
    }

    public ParameterTypeMismatchException(Throwable cause) {
        super(cause);
    }
}
