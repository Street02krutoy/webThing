package com.srit.modules.web.lib.types;

import java.io.IOException;

public abstract class Response {
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

    public abstract String getResponse() throws IOException;
}
