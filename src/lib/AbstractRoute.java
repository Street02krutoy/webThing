package lib;

import lib.types.HttpRequest;
import lib.types.HttpResponse;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public abstract class AbstractRoute {
    public abstract void callback(HttpRequest req, HttpResponse res);
    public AbstractRoute(Client client) throws Exception {
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

    public void checkAnnotations(Client client)  throws Exception {

        Class<AbstractRoute> annotatedMethodsClass = (Class<AbstractRoute>) this.getClass();

        for (Method method : annotatedMethodsClass.getDeclaredMethods()) {

            Route route = method.getAnnotation(Route.class);
            Method callback = this.getClass().getMethod("callback", HttpRequest.class, HttpResponse.class);

            // If the annotation is not null
            if (route != null) {
                client.getServer().createContext(route.name(), exchange -> {
                    if(Objects.equals(route.method(), exchange.getRequestMethod())) {
                        callback(
                                new HttpRequest() {

                                    @Override
                                    public String getMethod() {
                                        return route.method();
                                    }

                                    @Override
                                    public JSONObject getBody() {
                                        if(!Objects.equals(getMethod(), "POST")) return null;
                                        try {
                                            return getJsonFromBody(exchange.getRequestBody());
                                        } catch (UnsupportedEncodingException e) {
                                            return null;
                                        } catch (IOException e ){
                                            return null;
                                        }

                                    }
                                },
                                new HttpResponse() {
                                    @Override
                                    public void send(Object data) throws IOException {
                                        OutputStream outputStream = exchange.getResponseBody();
                                        String newdata = data.toString();
                                        exchange.sendResponseHeaders(200, newdata.length());

                                        outputStream.write(newdata.getBytes());
                                        outputStream.flush();
                                        outputStream.close();
                                    }
                                }
                        );
                    }
                });
            }
        }
}
}
