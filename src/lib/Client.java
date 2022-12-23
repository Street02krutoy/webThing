package lib;

import com.sun.net.httpserver.HttpServer;
import lib.types.HttpMethods;
import lib.types.HttpRequest;
import lib.types.HttpResponse;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Client {
    private final HttpServer server;

    public Client(Boolean test) throws Exception {

        server = HttpServer.create(new InetSocketAddress(4000), 1);

        if(test)
            new AbstractRouteClass(this){
                @Route(method = HttpMethods.GET, name = "/")
                @Override
                public void callback(HttpRequest req, HttpResponse res) {
                    try {
                        res.send("Hello, World");
                    }catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            };
        server.start();
    }

    public HttpServer getServer() {
        return server;
    }
}
