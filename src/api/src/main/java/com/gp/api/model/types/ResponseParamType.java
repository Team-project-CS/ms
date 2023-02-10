package com.gp.api.model.types;

import lombok.Getter;

@Getter
public enum ResponseParamType {
    STRING("str"),
    INTEGER("int"),
    REGEX("regex"),
    FIXED("fixed");

    private final String shortType;

    ResponseParamType(String shortType){
        this.shortType = shortType;
    }
}
