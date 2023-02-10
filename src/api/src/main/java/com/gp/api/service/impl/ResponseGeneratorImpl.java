package com.gp.api.service.impl;

import com.gp.api.model.types.ParamType;
import com.gp.api.service.ResponseGenerator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ResponseGeneratorImpl implements ResponseGenerator {
    private static final Random generator = new Random();
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int MAX_STRING_LENGTH = 15;
    private static final int MAX_INTEGER_VALUE = 50;

    @Override
    public Map<String, ?> generateResponse(Map<String, ParamType> responseTemplate) {
        Map<String, Object> response = new HashMap<>();
        responseTemplate.forEach((key, value) -> response.put(key, generateRandomValue(value)));
        return response;
    }

    private Object generateRandomValue(ParamType type) {
        switch (type) {
            case STRING:
                return generateRandomString();
            case INTEGER:
                return generator.nextInt(MAX_INTEGER_VALUE);
            default:
                return null;
        }
    }

    private String generateRandomString() {
        int length = generator.nextInt(MAX_STRING_LENGTH) + 1;
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = CHARACTERS.charAt(generator.nextInt(CHARACTERS.length()));
        }
        return new String(text);
    }
}
