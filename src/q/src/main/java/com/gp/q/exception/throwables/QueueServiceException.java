package com.gp.q.exception.throwables;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class QueueServiceException extends RuntimeException {
    @Getter
    private final HttpStatus status;

    public QueueServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
