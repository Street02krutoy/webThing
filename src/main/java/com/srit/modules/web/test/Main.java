package com.srit.modules.web.test;

import com.srit.modules.web.lib.*;
import com.srit.modules.web.lib.Module;
import com.srit.modules.web.lib.types.*;
import com.srit.modules.web.test.modules.Test;
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
            Client client = new Client(4000);
            Module test = new Test(client);

            test.enable();

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
