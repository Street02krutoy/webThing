package com.srit.modules.web.test.authorizationservice;

import com.srit.modules.web.test.authorizationservice.model.PrivateUser;

import java.sql.*;

public class Database {

    private static Connection conn;
    public static void init() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:auth.db");

        conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS users(" +
                "password VARCHAR(255) NOT NULL," +
                "display_name VARCHAR(255) NOT NULL,"+
                "email VARCHAR(255) NOT NULL UNIQUE,"+
                "nickname VARCHAR(255) NOT NULL UNIQUE"+
                ")");




    }

    private static void registerUser (PrivateUser user) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO users(password,email,nickname,display_name) VALUES (?,?,?,?)");
        st.setString(1, user.getPassword());
        st.setString(2, user.getEmail());
        st.setString(3, user.getNickname());
        st.setString(4, user.getDisplayName());
        st.execute();
    }

    public static PrivateUser getUserByEmail(String email) throws SQLException {
        ResultSet set = conn.createStatement().executeQuery(String.format("SELECT * FROM users WHERE email = '%s'", email));

        PrivateUser user = new PrivateUser();

        user.setPassword (set.getString("password"));
        user.setEmail(set.getString("email"));
        user.setNickname(set.getString("nickname"));
        user.setDisplayName (set.getString("display_name"));

        return user;
    }

    public static PrivateUser getUserByNickname(String nickname) throws SQLException {
        ResultSet set = conn.createStatement().executeQuery(String.format("SELECT * FROM users WHERE nickname = '%s'", nickname));

        PrivateUser user = new PrivateUser();

        user.setPassword (set.getString("password"));
        user.setEmail(set.getString("email"));
        user.setNickname(set.getString("nickname"));
        user.setDisplayName (set.getString("display_name"));

        return user;
    }

    public static PrivateUser registerUser(String password, String email, String nickname, String displayName) throws SQLException {

        PrivateUser user = new PrivateUser();

        user.setPassword(password);
        user.setEmail(email);
        user.setNickname(nickname);
        user.setDisplayName(displayName);

        Database.registerUser(user);

        return user;
    }

    public static PrivateUser registerUser(String password, String email, String nickname ) throws SQLException {

        PrivateUser user = new PrivateUser();

        user.setPassword(password);
        user.setEmail(email);
        user.setNickname(nickname);
        user.setDisplayName(nickname);

        Database.registerUser(user);

        return user;
    }

    public static PrivateUser registerUser(String password, String email) throws SQLException {

        PrivateUser user = new PrivateUser();

        user.setPassword(password);
        user.setEmail(email);

        String nickname = email.split("@")[0];

        user.setNickname(nickname);
        user.setDisplayName(nickname);

        Database.registerUser(user);

        return user;
    }

}
