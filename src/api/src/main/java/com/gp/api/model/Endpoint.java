package com.gp.api.model;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class Endpoint {
    private UUID id;
    private String title;
    private Map<String, ParamType> bodyTemplate;
    private Map<String, ParamType> responseTemplate;
    private String description;
}
