package com.gp.api.service.impl;

import org.springframework.stereotype.Service;
import com.gp.api.model.pojo.Param;
import com.gp.api.service.ResponseInterpreter;

import java.util.Map;
import java.util.Set;

@Service
public class LuaCustomLogicResponseInterpreter implements ResponseInterpreter {

    @Override
    public Map<String, ?> generateResponse(Map<String, ?> requestFields, String script) {
        LuaRequestProceedInterpreter generator = new LuaRequestProceedInterpreter();
        return generator.execute(requestFields, script);
    }
}
