package com.srit.modules.web.lib;

import com.srit.modules.web.lib.types.PreHandler;
import com.sun.net.httpserver.HttpServer;

import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private final HttpServer server;

    private final Map<Class<? extends Annotation>, PreHandler> preHandlerMap;
    private final Map<String, Module> modules;

    public Client(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 1);
        this.preHandlerMap =new HashMap<>();
        this.modules = new HashMap<>();
    }

    public void enable() {
        server.start();
    }

    public void addModule(String id, Module module) {
        modules.put(id, module);
    }

    public void enableModule(String id) {
        modules.get(id).enable();
    }

    public void disableModule(String id) {
        modules.get(id).disable();
    }

    public Map<Class<? extends Annotation>, PreHandler> getPreHandlerMap() {
        return preHandlerMap;
    }

    public void setPreHandler(Class<? extends Annotation> a, PreHandler h) {
        preHandlerMap.put(a, h);
    }

    public HttpServer getServer() {
        return server;
    }

    public void disable() {
        server.stop(0);
    }
}
