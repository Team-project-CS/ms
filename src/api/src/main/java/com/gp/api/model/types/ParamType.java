package com.gp.api.model.types;

import lombok.Getter;

@Getter
public enum ParamType {
    STRING("str"),
    INTEGER("int"),
    REGEX("regex"),
    FIXED("fixed");

    public static final ParamType[] BODY_TYPES = {STRING, INTEGER, REGEX, FIXED};
    public static final ParamType[] RESPONSE_TYPES = {STRING, INTEGER, REGEX, FIXED};
    private final String shortType;

    ParamType(String shortType) {
        this.shortType = shortType;
    }
}
