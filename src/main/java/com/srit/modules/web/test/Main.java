package com.srit.modules.web.test;

import com.srit.modules.web.lib.*;
import com.srit.modules.web.lib.types.AuthorizationChecker;
import com.srit.modules.web.lib.types.FileResponse;
import com.srit.modules.web.lib.types.HttpRequest;
import com.srit.modules.web.lib.types.HttpResponse;

import java.io.File;
import java.net.URL;

public class Main {
    public static Client client;

    public static void main(String[] args) {
        try {
            client=new Client(4000, req -> req.getQuery().get("a").equals("b"));
            System.out.println("Started on 4000");
            new Route(client){
                @Override
                @BodyKey(key = "a", value = BodyTypes.STRING)
                @RequireAuthorisation
                @RouteSettings(name = "/", method = "GET")
                protected void callback(HttpRequest req, HttpResponse res) {
                    res.send(new FileResponse().setFile(getResourceFile("index.html")).setCode(200));
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
