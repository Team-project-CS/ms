package com.gp.api.model;

import com.gp.api.model.types.BodyParamType;
import com.gp.api.model.types.ResponseParamType;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class Endpoint {
    private UUID id;
    private String title;
    private Map<String, Param<BodyParamType>> bodyTemplate;
    private Map<String, Param<ResponseParamType>> responseTemplate;
    private String description;
}
