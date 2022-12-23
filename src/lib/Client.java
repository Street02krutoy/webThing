package lib;

import com.sun.net.httpserver.HttpServer;
import lib.types.HttpMethods;
import lib.types.HttpRequest;
import lib.types.HttpResponse;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Client {
    private final HttpServer server;

    public Client(Boolean test, int port) throws Exception {

        server = HttpServer.create(new InetSocketAddress(port), 1);

        if(test)
            new AbstractRoute(this){
                @Route(method = HttpMethods.GET, name = "/")
                @Override
                public void callback(HttpRequest req, HttpResponse res) {
                    System.out.println(req.getBody());
                    try {
                        res.send("Hello, World");
                    }catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            };
        server.start();
        System.out.printf("Started on %s", port);
    }

    public HttpServer getServer() {
        return server;
    }
}
