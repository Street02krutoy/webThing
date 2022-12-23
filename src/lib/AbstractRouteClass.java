package lib;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lib.types.HttpRequest;
import lib.types.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Objects;

public abstract class AbstractRouteClass {
    public abstract void callback(HttpRequest req, HttpResponse res);
    public AbstractRouteClass(Client client) throws Exception {
        checkAnnotations(client);
    }

    public void checkAnnotations(Client client) throws Exception {

        Class<AbstractRouteClass> annotatedMethodsClass = (Class<AbstractRouteClass>) this.getClass();

        for (Method method : annotatedMethodsClass.getDeclaredMethods()) {

            Route route = method.getAnnotation(Route.class);
            Method callback = this.getClass().getMethod("callback", HttpRequest.class, HttpResponse.class);

            // If the annotation is not null
            if (route != null) {
                client.getServer().createContext(route.name(), exchange -> {
                    if(Objects.equals(route.method(), exchange.getRequestMethod())) {
                        callback(
                                new HttpRequest() {
                                    public final String method = route.method();
                                },
                                data -> {
                                    OutputStream outputStream = exchange.getResponseBody();
                                    String newdata = data.toString();
                                    exchange.sendResponseHeaders(200, newdata.length());

                                    outputStream.write(newdata.getBytes());
                                    outputStream.flush();
                                    outputStream.close();
                                }
                        );
                    }
                });
                try {
                    System.out.println(route.method());
                } catch (Throwable ex) {
                    System.out.println(ex.getCause());
                }

            }
        }
}
}
