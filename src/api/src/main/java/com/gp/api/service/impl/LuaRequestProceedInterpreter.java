package com.gp.api.service.impl;

import com.gp.api.exception.RequestInterpreterException;
import com.gp.api.model.pojo.Param;
import com.gp.api.service.RequestProceedInterpreter;
import org.json.JSONException;
import org.json.JSONObject;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

import java.util.*;

public class LuaRequestProceedInterpreter implements RequestProceedInterpreter
{
    @Override
    public Map<String, Object> execute(final Map<String, ?> requestField, String script)
            throws RequestInterpreterException {
        Globals globals = JsePlatform.standardGlobals();
        LuaValue chunk = globals.load(script);
        final Varargs args = LuaValue.varargsOf(getCallArguments(requestField, script));
        final Varargs response = chunk.invoke(args);
        if (response instanceof LuaTable) {
            return buildSetFromLuaTable((LuaTable) response);
        }

        return null;
    }

    private LuaValue[] getCallArguments(final Map<String, ?> request, String script)
    {
        ArrayList<String> fields = new ArrayList<>();
        String[] parts = script.split("\\.\\.\\.");
        String[] params = parts[0].replaceAll("local", "").replaceAll("=", "").split(",");
        // In luaj arguments are passed backwards for some reason
        for (int i = 0; i < params.length; ++i) {
            fields.add(params[i].trim());
        }
        LuaValue[] result = new LuaValue[fields.size()];
        int size = 0;
        for (String field : fields) {
            String value = request.get(field).toString();
            result[size++] = LuaValue.valueOf(value);
        }

        return result;
    }

    private Map<String, Object> buildSetFromLuaTable(final LuaTable responseTable) throws RequestInterpreterException {
        final LuaValue[] tableKeys = responseTable.keys();
        HashMap<String, Object> result = new HashMap<>();
        for (LuaValue key : tableKeys) {
            LuaValue value = responseTable.get(key);
            if (value.isnil()) {
                throw new RequestInterpreterException(
                        "One of Lua Interpreter response value was nil: " + key);
            }
            try {
                switch (value.type()) {
                    case LuaValue.TNUMBER:
                        if (value.islong()) {
                            result.put(key.toString(), value.tolong());
                        } else {
                            result.put(key.toString(), value.todouble());
                        }
                        break;
                    case LuaValue.TSTRING:
                        if (value.isstring()) {
                            result.put(key.toString(), value.toString());
                        }
                        break;
                    default:
                        throw new RequestInterpreterException(
                                "Lua Interpreter parsed unknown type from response table.");
                }
            } catch (Exception e) {
                throw new RequestInterpreterException(
                        "Lua Interpreter failed to construct json response object: " + e.getMessage());
            }
        }

        return result;
    }
}
