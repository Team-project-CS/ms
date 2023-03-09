package com.gp.api.service;

import com.gp.api.model.pojo.Param;

import java.util.Map;
import java.util.Set;

public interface ResponseGenerator {
    Map<String, ?> generateResponse(Set<Param> responseTemplate);
}
