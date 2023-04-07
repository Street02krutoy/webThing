package com.srit.modules.web.test.authorizationservice;

import java.sql.*;

public class Database {
    public static void init() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:sof.db");
        System.out.println("Created database");
        conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS users(" +
                "password VARCHAR(255)," +
                "display_name VARCHAR(255),"+
                "email VARCHAR(255),"+
                "nickname VARCHAR(255)"+
                ")");
        PreparedStatement st = conn.prepareStatement("INSERT INTO users(password,display_name,email,nickname) VALUES (?,?,?,?)");
        st.setString(0, "1rwer23t45");
        st.setString(1, "user");
        st.setString(2, "user@example.com");
        st.setString(3, "random_chars");
        st.execute();

        ResultSet set = conn.createStatement().executeQuery("SELECT password, display_name, email, nickname FROM users");
        while (set.next()) {
            System.out.println(
                    set.getString("password")
                            +" "+
                            set.getString("display_name")
                            +" "+
                            set.getString("email")
                        +" "+
                            set.getString("nickname")
            );
        }

    }
}
