package com.srit.modules.web.lib;


import com.srit.modules.web.lib.types.*;
import com.sun.net.httpserver.Headers;
import org.json.JSONObject;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Route {

    Client client;
    protected abstract void callback(HttpRequest req, HttpResponse res);
    public Route(Client client) {
        this.client = client;
    }

    public void enable(){
        try {
            checkAnnotations(client);
        } catch (NoSuchMethodException e) {
            System.out.println("abc "+ e);
        }
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
    private String httpMethod = null;

    public void disable() {
        if(path != null) client.getServer().removeContext(path);
    }

    private final Map<String, String> fileHeaders = new HashMap<String, String>(){{
        put("html", "text/html");
        put("css", "text/css");
        put("js", "text/javascript");
        put("json", "application/json");
        put("svg", "image/svg+xml");
    }};

    private void checkAnnotations(Client client) throws NoSuchMethodException {

        Map<Class<? extends Annotation>, PreHandler> PreHandlers = client.getPreHandlerMap();
        Map<Class<? extends Annotation>, PreHandler> ActivePreHandlers = new HashMap<>();

        Method method = this.getClass().getDeclaredMethod("callback", HttpRequest.class, HttpResponse.class);

        RouteSettings route = method.getAnnotation(RouteSettings.class);
        BodyKey[] bodyKeys = method.getAnnotationsByType(BodyKey.class);

        PreHandlers.forEach((a, h) -> {
            if(method.getAnnotation(a) != null) ActivePreHandlers.put(a, h);
        });


        // If the annotation is not null
        if (route != null) {
            path = route.name();
            httpMethod = route.method();
            client.getServer().createContext(route.name(), exchange -> {
                if(Objects.equals(route.method(), exchange.getRequestMethod())) {

                    HttpResponse res = new HttpResponse() {
                        @Override
                        public void send(Response data) {
                            data.getHeaders().forEach((key, value)-> {
                                exchange.getResponseHeaders().add(key, value);
                            });

                            if(data.getCode() == 0) {
                                data = new ErrorResponse(500);
                            };
                            OutputStream outputStream = exchange.getResponseBody();
                            String newdata;
                            try {
                                newdata = data.getResponse();
                            } catch (IOException e) {
                                newdata = new ErrorResponse(500).getResponse();
                            }

                            try {
                                exchange.sendResponseHeaders(data.getCode(), newdata.length());
                                outputStream.write(newdata.getBytes());
                                outputStream.flush();
                                outputStream.close();
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
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

                    AtomicBoolean rejected = new AtomicBoolean(false);

                    PreHandlers.forEach((aClass, preHandler) -> {
                        Response resp = preHandler.check(req);
                        if(resp==null) return;
                        res.send(resp);
                        rejected.set(true);
                    });


                    if(rejected.get()) return;

                    callback(req, res);
                }
            });
        }
    }
}
