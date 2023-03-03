package com.srit.modules.web.lib.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JsonResponse extends Response {
    public Map<String, String> objects = new HashMap<>();


    public JsonResponse put(String key, String value) {
        objects.put(String.format("\"%s\"", key), String.format("\"%s\"", value));
        return this;
    }

    public JsonResponse put (String key, Number value) {
        objects.put(String.format("\"%s\"", key), value.toString());
        return this;
    }

    public JsonResponse put (String key, Boolean value) {
        objects.put(String.format("\"%s\"", key), value.toString());
        return this;
    }

    public JsonResponse put (String key, Object[] value) {
        objects.put(String.format("\"%s\"", key), Arrays.toString(value)
                .replace(",","\",\"")
                .replace("[", "[\"")
                .replace("]", "\"]")
        );
        return this;
    }

    public JsonResponse put (String key, JsonResponse value) {
        objects.put(String.format("\"%s\"", key), value.toString());
        return this;
    }

    @Override
    public String getResponse() {
        return this.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        for (String key: objects.keySet()) {
            String value = objects.get(key);
            System.out.println(key+" "+value);
            if(objects.size()-1 != objects.keySet().stream().toList().indexOf(key))
                builder.append(String.format("%s:%s,",key,value));
            else builder.append(String.format("%s:%s",key,value));
        }

        builder.append("}");

        return builder.toString();
    }
}