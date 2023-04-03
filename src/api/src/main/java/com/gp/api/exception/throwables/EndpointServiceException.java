package com.gp.api.exception.throwables;

public class EndpointServiceException extends Exception {
    public EndpointServiceException(String message) {
        super(message);
    }
    public EndpointServiceException(Throwable cause){super(cause);}
    public EndpointServiceException(){super();}
}
