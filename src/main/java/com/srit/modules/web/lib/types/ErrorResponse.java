package com.srit.modules.web.lib.types;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse extends JsonResponse {
    private Map<Integer, String> messages = new HashMap<>(){{
        put(200, "OK");
        put(202, "Accepted");
        put(204, "No content");
        put(401, "Unauthorized");
        put(404, "Not found");
        put(403, "Forbidden");
        put(500, "Server error");
    }};

    public ErrorResponse(int code) {
        super();
        this.put("error", new JsonResponse().put("message", messages.get(code)).put("code", code)).setCode(code);
    }

    public ErrorResponse(int code, String message) {
        super();
        this.put("error", new JsonResponse().put("message", message).put("code", code)).setCode(code);
    }
}
