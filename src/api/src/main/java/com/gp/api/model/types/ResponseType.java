package com.gp.api.model.types;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ResponseType {
    RANDOM("random"),
    SPECIFIED("specified");

    private String shortType;

    ResponseType(String shortType){
        this.shortType = shortType;
    }

    public static ResponseType getByShortType(String shortType){
        return Arrays.stream(ResponseType.values())
                .filter(type -> type.getShortType().equals(shortType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
