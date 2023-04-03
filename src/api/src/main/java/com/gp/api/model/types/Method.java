package com.gp.api.model.types;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Method {
    POST("post"),
    GET("get");

    private final String shortType;

    Method(String shortType) {
        this.shortType = shortType;
    }

    public static Method getByShortType(String type) {
        return Arrays.stream(Method.values())
                .filter(method -> method.getShortType().toLowerCase().equals(type))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
