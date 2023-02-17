package com.srit.modules.web.lib.types;

public interface HttpResponse {
    void send(Response data);
    void setCookie(String key, String value);
    void redirect(String url);
    void setHeader(String key, String value);
}
