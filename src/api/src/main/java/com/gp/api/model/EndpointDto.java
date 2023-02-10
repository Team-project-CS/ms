package com.gp.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointDto {
    private String title;
    private String description;
    private Map<String, ParamDto> bodyTemplate;
    private Map<String, ParamDto> responseTemplate;
}
