package lib.types;

import org.json.JSONObject;

public interface HttpRequest {
    public String getMethod();
    public JSONObject getBody();
}
