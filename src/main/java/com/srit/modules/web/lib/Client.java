package com.srit.modules.web.lib;

import com.srit.modules.web.lib.types.AuthorizationChecker;
import com.srit.modules.web.lib.types.HttpRequest;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Client {
    private final HttpServer server;

    private final AuthorizationChecker checker;

    public Client(int port, AuthorizationChecker checker) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 1);
        this.checker= (checker != null) ? checker : new AuthorizationChecker() {
            @Override
            public boolean check(HttpRequest req) {
                return true;
            }
        };
        server.start();
    }

    public AuthorizationChecker getChecker() {
        return checker;
    }

    public HttpServer getServer() {
        return server;
    }

    public void disable() {
        server.stop(0);
    }
}
