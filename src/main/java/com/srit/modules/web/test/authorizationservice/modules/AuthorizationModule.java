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
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Objects;

public class AuthorizationModule extends Module {
    public AuthorizationModule(Client client) {

        // GET /user
        addRoute(new Route(client) {
            @Override
            @RouteSettings(method = "GET", name = "/user")
            protected void callback(HttpRequest req, HttpResponse res) {
                String token = req.getHeaders().get("authorization").get(0);
                if(token == null) {
                    res.send(new ErrorResponse(401));
                    return;
                };
                PrivateUser user = getPrivateUserByEmail(token.split(";")[0]);
                if(user == null) {
                    res.send(new ErrorResponse(403));
                    return;
                };
                res.send(user.toPublicUser().toJsonResponse().setCode(200));
            }
        });

        addRoute(new Route(client) {
            @Override

            @RouteSettings(method = "POST", name = "/authorize")
            protected void callback(HttpRequest req, HttpResponse res) {

                JSONObject body = req.getBody();

                String token = authorize(body.getString("email"), body.getString("password"));

                if(token == null) {
                    res.send(new ErrorResponse(401));
                    return;
                };
                res.send(new JsonResponse().put("token", token).setCode(200));
            }
        });

        addRoute(new Route(client) {
            @Override
            @RouteSettings(method = "POST", name = "/register")
            protected void callback(HttpRequest req, HttpResponse res) {
                PrivateUser user = null;

                JSONObject body = req.getBody();

                try {
                    user = Database.registerUser(body.getString("password"), body.getString("email"));
                } catch (SQLException e) {
                    System.out.println(e);
                }

                if(user == null) {
                    res.send(new ErrorResponse(403));
                    return;
                };

                String token = user.getEmail()+";"+user.getPassword();

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
            PrivateUser user = Database.getUserByEmail(email);

            if(user == null || !Objects.equals(user.getPassword(), password)) {
                return null;
            }

            return user.getEmail()+";"+user.getPassword();
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
