package com.srit.modules.web.lib;


import com.srit.modules.web.lib.types.ErrorResponse;
import com.srit.modules.web.lib.types.HttpRequest;
import com.srit.modules.web.lib.types.HttpResponse;
import com.srit.modules.web.lib.types.Response;
import com.sun.net.httpserver.Headers;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public abstract class Route {
    protected abstract void callback(HttpRequest req, HttpResponse res);
    public Route(Client client) {
        checkAnnotations(client);
    }

    private JSONObject getJsonFromBody(InputStream inputStream) throws IOException {
        InputStreamReader isr =  new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();
        return new JSONObject(buf.toString());
    }

    private String path = null;
    private String method = null;

    public void remove(Client client) {
        if(path != null) client.getServer().removeContext(path);
    }

    private final Map<String, String> fileHeaders = new HashMap<String, String>(){{
        put("html", "text/html");
        put("css", "text/css");
        put("js", "text/javascript");
        put("json", "application/json");
        put("svg", "image/svg+xml");
    }};

    private void checkAnnotations(Client client) {

        Class<Route> annotatedMethodsClass = (Class<Route>) this.getClass();

        for (Method methodd : annotatedMethodsClass.getDeclaredMethods()) {

            RouteSettings route = methodd.getAnnotation(RouteSettings.class);
            RequireAuthorisation auth = methodd.getAnnotation(RequireAuthorisation.class);
            BodyKey[] bodyKeys = methodd.getAnnotationsByType(BodyKey.class);

            // If the annotation is not null
            if (route != null) {
                path = route.name();
                method = route.method();
                client.getServer().createContext(route.name(), exchange -> {
                    if(Objects.equals(route.method(), exchange.getRequestMethod())) {

                        HttpResponse res = new HttpResponse() {
                            @Override
                            public void send(Response data) {
                                if(data.getCode() == 0) {
                                    data = new ErrorResponse(500);
                                };
                                OutputStream outputStream = exchange.getResponseBody();
                                String newdata = data.toString();


                                try {
                                    exchange.sendResponseHeaders(data.getCode(), newdata.length());
                                    outputStream.write(newdata.getBytes());
                                    outputStream.flush();
                                    outputStream.close();
                                } catch (IOException ignored) {

                                }
                            }

                            @Override
                            public void setCookie(String key, String value) {
                                this.setHeader("Set-Cookie", String.format("%s=%s", key, value));
                            }


                            @Override
                            public void redirect(String url) {
                                OutputStream outputStream = exchange.getResponseBody();
                                try {
                                    Headers headers = exchange.getResponseHeaders();
                                    headers.set("Location", url);
                                    exchange.sendResponseHeaders(302, 0);
                                    outputStream.close();
                                } catch (IOException ignored) {}
                            }

                            @Override
                            public void setHeader(String key, String value) {
                                Headers headers = exchange.getResponseHeaders();
                                headers.set(key, value);
                            }

                        };

                        HttpRequest req = new HttpRequest() {

                            @Override
                            public String getMethod() {
                                return route.method();
                            }

                            @Override
                            public JSONObject getBody() {
                                if(!Objects.equals(getMethod(), "POST")) return null;
                                try {
                                    return getJsonFromBody(exchange.getRequestBody());
                                } catch (IOException e) {
                                    return null;
                                }

                            }

                            @Override
                            public Map<String, String> getQuery() {
                                String query = exchange.getRequestURI().getQuery();
                                if(query == null) {
                                    return new HashMap<>();
                                }
                                Map<String, String> result = new HashMap<>();
                                for (String param : query.split("&")) {
                                    String[] entry = param.split("=");
                                    if (entry.length > 1) {
                                        result.put(entry[0], entry[1]);
                                    }else{
                                        result.put(entry[0], "");
                                    }
                                }
                                return result;
                            }

                            @Override
                            public Headers getHeaders() {
                                return exchange.getRequestHeaders();
                            }
                        };

                        for(BodyKey key: bodyKeys) {
                            Object value = req.getBody().get(key.key());
                            if(value == null) {
                                return;
                            }
                            switch (key.value()) {
                                case NUMBER -> {
                                    if(!(value instanceof Number)) return;
                                }

                                case STRING -> {
                                    if(!(value instanceof String)) return;
                                }
                            }
                        }

                        if (auth != null && !client.getChecker().check(req)) {
                            res.send(new ErrorResponse(403));
                            return;
                        }

                        callback(req, res);
                    }
                });
            }
        }
}
}
