package com.srit.modules.web.lib.types;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Response {
    private int code;

    private final Map<String, String> headers;

    public void setHeader(String key, String value){
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Deprecated
    public Response(int code) {
        this.code = code;
        this.headers = new HashMap<>();
    }

    public Response() {
        this.code = 0 ;
        this.headers = new HashMap<>();
    }

    public int getCode() {
        return code;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public abstract String getResponse() throws IOException;
}
