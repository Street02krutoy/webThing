package com.srit.modules.web.lib.types;

import com.sun.net.httpserver.Headers;
import org.json.JSONObject;

import java.util.Map;

public interface HttpRequest {
    public String getMethod();
    public JSONObject getBody();
    public Map<String, String> getQuery();
    public Headers getHeaders();
}
