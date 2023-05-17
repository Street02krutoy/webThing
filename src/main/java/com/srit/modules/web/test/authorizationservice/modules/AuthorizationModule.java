package com.srit.modules.web.test.authorizationservice.modules;

import com.srit.modules.web.lib.*;
import com.srit.modules.web.lib.Module;
import com.srit.modules.web.lib.types.ErrorResponse;
import com.srit.modules.web.lib.types.HttpRequest;
import com.srit.modules.web.lib.types.HttpResponse;
import com.srit.modules.web.lib.types.JsonResponse;
import com.srit.modules.web.test.authorizationservice.Database;
import com.srit.modules.web.test.authorizationservice.model.PrivateUser;
import com.srit.modules.web.test.authorizationservice.model.PublicUser;

import java.sql.SQLException;
import java.util.Objects;

public class AuthorizationModule extends Module {
    public AuthorizationModule(Client client) {
        addRoute(new Route(client) {
            @Override
            @RouteSettings(method = "GET", name = "/user")
            protected void callback(HttpRequest req, HttpResponse res) {
                String token = req.getHeaders().get("authorization").get(0);
                if(token == null) {
                    res.send(new ErrorResponse(401));
                    return;
                };
                PublicUser user = getPrivateUserByEmail(token.split(";")[0]).toPublicUser();
                if(user == null) {
                    res.send(new ErrorResponse(403));
                    return;
                };
                res.send(user.toJsonResponse().setCode(200));
            }
        });

        addRoute(new Route(client) {
            @Override

            @RouteSettings(method = "POST", name = "/authorize")
            protected void callback(HttpRequest req, HttpResponse res) {
                String token = authorize(req.getBody().getString("email"), req.getBody().getString("password"));
                if(token == null) {
                    res.send(new ErrorResponse(401));
                    return;
                };
                res.send(new JsonResponse().put("token", token).setCode(200));
            }
        });

        enable();

        System.out.println("Authorization module has successfully initiated!");

    }

    private PrivateUser getPrivateUserByNickname(String nickname) {
        try {
            return Database.getUserByNickname(nickname);
        } catch (SQLException e) {
            return null;
        }
    }

    private String authorize(String email, String password) {
        try {
            PublicUser user = Database.getUserByEmail(email);
            return email+";"+password;
        } catch (SQLException e) {
            return null;
        }
    }

    private PrivateUser getPrivateUserByEmail(String email) {

        try {
            return Database.getUserByEmail(email);
        } catch (SQLException e) {
            return null;
        }
    }

    private PublicUser getPublicUser(String nickname){
        try {
            return Database.getUserByNickname(nickname).toPublicUser();
        } catch (SQLException e) {
            return null;
        }

    }
}
