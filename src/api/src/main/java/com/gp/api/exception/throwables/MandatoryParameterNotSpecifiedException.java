package com.gp.api.exception.throwables;

import com.gp.api.exception.ApiErrorCode;

@ApiErrorCode(400)
public class MandatoryParameterNotSpecifiedException extends EndpointServiceException {
    public MandatoryParameterNotSpecifiedException(String message) {
        super(message);
    }

    public MandatoryParameterNotSpecifiedException(Throwable cause) {
        super(cause);
    }

    public MandatoryParameterNotSpecifiedException() {
    }
}
