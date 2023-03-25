package com.gp.api.service;

import com.gp.api.exception.RequestInterpreterException;
import com.gp.api.model.pojo.Param;

import java.util.Map;
import java.util.Set;

public interface RequestProceedInterpreter
{
    Map<String, Object> execute(final Map<String, ?> requestField, String script)
            throws RequestInterpreterException;
}
