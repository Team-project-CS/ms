package com.gp.api.service.impl;

import com.gp.api.model.Param;
import com.gp.api.service.ResponseGenerator;
import org.cornutum.regexpgen.RandomGen;
import org.cornutum.regexpgen.js.Provider;
import org.cornutum.regexpgen.random.RandomBoundsGen;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
public class ResponseGeneratorImpl implements ResponseGenerator {
    private static final Random generator = new Random();
    private static final RandomGen random = new RandomBoundsGen();
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int MAX_STRING_LENGTH = 15;
    private static final int MAX_INTEGER_VALUE = 50;
    private static final int MIN_REGEX_STRING_LENGTH = 1;
    private static final int MAX_REGEX_STRING_LENGTH = 15;

    @Override
    public Map<String, ?> generateResponse(Set<Param> responseTemplate) {
        Map<String, Object> response = new HashMap<>();
        responseTemplate.forEach(param -> response.put(param.getKey(), generateRandomValue(param)));
        return response;
    }

    private Object generateRandomValue(Param param) {
        switch (param.getType()) {
            case STRING:
                return generateRandomString();
            case INTEGER:
                return generator.nextInt(MAX_INTEGER_VALUE);
            case REGEX:
                return generateRandomRegex(param.getValue());
            case FIXED:
                return param.getValue();
            default:
                return null;
        }
    }

    private Object generateRandomRegex(String regex) {
        return Provider
                .forEcmaScript()
                .matching(regex)
                .generate(random, MIN_REGEX_STRING_LENGTH, MAX_REGEX_STRING_LENGTH);
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
