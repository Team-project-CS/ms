package com.gp.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointDto {
    private String title;
    private String description;
    private Set<ParamDto> bodyTemplate;
    private Set<ParamDto> responseTemplate;
	private String method;
	private String proceedLogic;
}
