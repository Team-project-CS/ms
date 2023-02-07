package com.gp.api.model;

import lombok.Data;

import java.util.Map;

@Data
public class EndpointDto {
    private String title;
    private Map<String, ParamType> bodyTemplate;
    private Map<String, ParamType> responseTemplate;
    private String description;
}
