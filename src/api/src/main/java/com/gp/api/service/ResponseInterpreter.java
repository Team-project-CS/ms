package com.gp.api.service;

import com.gp.api.model.pojo.Param;

import java.util.Map;
import java.util.Set;

public interface ResponseInterpreter {
    Map<String, ?> generateResponse(Map<String, ?> requestFields, String script);
}
