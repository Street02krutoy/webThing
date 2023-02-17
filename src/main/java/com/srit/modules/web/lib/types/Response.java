package com.srit.modules.web.lib.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Response {
    public Map<String, String> objects = new HashMap<>();
    private int code;

    @Deprecated
    public Response(int code) {
        this.code = code;
    }

    public Response() {
        this.code = 0 ;
    }

    public int getCode() {
        return code;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public Response put(String key, String value) {
        objects.put(String.format("\"%s\"", key), String.format("\"%s\"", value));
        return this;
    }

    public Response put (String key, Number value) {
        objects.put(String.format("\"%s\"", key), value.toString());
        return this;
    }

    public Response put (String key, Boolean value) {
        objects.put(String.format("\"%s\"", key), value.toString());
        return this;
    }

    public Response put (String key, Object[] value) {
        objects.put(String.format("\"%s\"", key), Arrays.toString(value)
                .replace(",","\",\"")
                .replace("[", "[\"")
                .replace("]", "\"]")
        );
        return this;
    }

    public Response put (String key, Response value) {
        objects.put(String.format("\"%s\"", key), value.toString());
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        for (String key: objects.keySet()) {
            String value = objects.get(key);
            if(objects.size()-1 != objects.keySet().stream().toList().indexOf(key))
                builder.append(String.format("%s:%s,",key,value));
            else builder.append(String.format("%s:%s",key,value));
        }

        builder.append("}");

        return builder.toString();
    }
}