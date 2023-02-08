package com.gp.api.model;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(
            example = "{" +
                    "\"bodyField1\" : \"str\"," +
                    "\"bodyField2\" : \"int\"" +
                    "}",
            value = "Template for body. Each row must be form of \"String : type\" where type is in {\"int\", \"str\"}"
    )
    private Map<String, ?> bodyTemplate;
    @ApiModelProperty(
            example = "{" +
                    "\"responseField1\" : \"str\"," +
                    "\"responseField2\" : \"int\"" +
                    "}",
            value = "Template for response. Each row must be form of \"String : type\" where type is in {\"int\", \"str\"}"
    )
    private Map<String, ?> responseTemplate;
}
