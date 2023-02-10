package com.gp.api.service;

import com.gp.api.model.types.ParamType;

import java.util.Map;

public interface ResponseGenerator {
    Map<String, ?> generateResponse(Map<String, ParamType> responseTemplate);
}
