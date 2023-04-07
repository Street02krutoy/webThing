package com.srit.modules.web.test.authorizationservice;

import com.srit.modules.web.lib.Client;

import java.sql.SQLException;

public class Main {

    private static Client client;

    public static void main(String[] args) {
        try {
            client = new Client(4000);
            try {
                Database.init();
            }catch (SQLException e) {
                System.out.println(e);
            }
            client.enable();
        } catch (Exception e) {
            System.out.println("Failed to start app");
            System.exit(0);
        }
    }
}
