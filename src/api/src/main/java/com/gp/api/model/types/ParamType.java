package com.gp.api.model.types;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ParamType {
    STRING("str"),
    INTEGER("int");

    private String shortType;

    ParamType(String shortType){
        this.shortType = shortType;
    }

    public static ParamType getByShortType(String shortType){
        return Arrays.stream(ParamType.values())
                .filter(type -> type.getShortType().equals(shortType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
