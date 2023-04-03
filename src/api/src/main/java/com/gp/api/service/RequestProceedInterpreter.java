package com.gp.api.service;

import com.gp.api.exception.RequestInterpreterException;

import java.util.Map;

public interface RequestProceedInterpreter {
    Map<String, Object> execute(final Map<String, ?> requestField, String script)
            throws RequestInterpreterException;
}
