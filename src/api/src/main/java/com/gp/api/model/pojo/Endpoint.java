package com.gp.api.model.pojo;

import com.gp.api.model.types.Method;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class Endpoint {
    private UUID id;
    private String title;
    private Set<Param> bodyTemplate;
    private Set<Param> responseTemplate;
    private String description;
    private Method method;
}
