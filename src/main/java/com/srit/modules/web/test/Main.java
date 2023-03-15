package com.srit.modules.web.test;

import com.srit.modules.web.lib.*;
import com.srit.modules.web.lib.types.*;
import com.srit.modules.web.test.prehandlers.RequireAuthorisation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static Client client;

    public static void main(String[] args) {
        try {
            Map<Class<? extends Annotation>, PreHandler> preHandlers = new HashMap<>();
            preHandlers.put(RequireAuthorisation.class, req -> {
                if(req.getQuery().get("a").equals("b")) return null;
                return new ErrorResponse(401);
            });

            client=new Client(4000, preHandlers);
            System.out.println("Started on 4000");
            new Route(client){
                @Override
                //@BodyKey(key = "a", value = BodyTypes.STRING)
                @RequireAuthorisation
                @RouteSettings(name = "/", method = "GET")
                protected void callback(HttpRequest req, HttpResponse res) {
                    res.send(null);
                    //res.send(new JsonResponse().put("hello","world").setCode(200));
                }
            };
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private static File getResourceFile(final String fileName)
    {
        URL url = Main.class
                .getClassLoader()
                .getResource(fileName);

        if(url == null) {
            return null;
        }

        return new File(url.getFile());
    }
}
