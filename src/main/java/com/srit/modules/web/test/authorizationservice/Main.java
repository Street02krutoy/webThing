package com.srit.modules.web.test.authorizationservice;

import com.srit.modules.web.lib.Client;
import com.srit.modules.web.test.authorizationservice.modules.AuthorizationModule;

import java.sql.SQLException;

public class Main {

    private static Client client;

    public static void main(String[] args) {
        try {
            client = new Client(4000);
            try {
                Database.init();
                System.out.println("Database initiated");

            }catch (Exception e) {
                System.out.println("Database initiation error:");
                System.out.println(e);
            }
            client.enable();
            client.addModule("authorization", new AuthorizationModule(client));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to start app");
            System.exit(0);
        }
    }
}
