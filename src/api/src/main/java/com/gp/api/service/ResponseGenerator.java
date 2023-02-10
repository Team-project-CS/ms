package com.gp.api.service;

import com.gp.api.model.Param;
import com.gp.api.model.types.ResponseParamType;

import java.util.Map;

public interface ResponseGenerator {
    Map<String, ?> generateResponse(Map<String, Param<ResponseParamType>> responseTemplate);
}
