package com.srit.modules.web.lib;

import com.srit.modules.web.lib.types.PreHandler;
import com.sun.net.httpserver.HttpServer;

import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.Map;

public class Client {
    private final HttpServer server;

    private final Map<Class<? extends Annotation>, PreHandler> preHandlerMap;

    public Client(int port, Map<Class<? extends Annotation>, PreHandler> preHandlerMap) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 1);
        this.preHandlerMap = preHandlerMap;
        server.start();
    }

    public Map<Class<? extends Annotation>, PreHandler> getPreHandlerMap() {
        return preHandlerMap;
    }

    public HttpServer getServer() {
        return server;
    }

    public void disable() {
        server.stop(0);
    }
}
