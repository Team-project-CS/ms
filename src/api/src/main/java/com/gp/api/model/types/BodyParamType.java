package com.gp.api.model.types;

import lombok.Getter;

@Getter
public enum BodyParamType {
    STRING("str"),
    INTEGER("int"),
    REGEX("regex"),
    FIXED("fixed");

    private final String shortType;

    BodyParamType(String shortType){
        this.shortType = shortType;
    }
}
