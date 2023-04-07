package com.srit.modules.web.test.authorizationservice.modules;

import com.srit.modules.web.lib.*;
import com.srit.modules.web.lib.Module;
import com.srit.modules.web.lib.types.ErrorResponse;
import com.srit.modules.web.lib.types.HttpRequest;
import com.srit.modules.web.lib.types.HttpResponse;
import com.srit.modules.web.lib.types.JsonResponse;
import com.srit.modules.web.test.authorizationservice.model.PrivateUser;
import com.srit.modules.web.test.authorizationservice.model.PublicUser;

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
                PublicUser user = getPublicUserByToken(token);
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
    }

    private PrivateUser getPrivateUserByNickname(String nickname) {
        if(Objects.equals(nickname, "asd")) return null;
        return new PrivateUser();
    }

    private String authorize(String email, String password) {
        if(Objects.equals(email, "asd")) return null;
        if(Objects.equals(password, "asd")) return null;
        return "";
    }

    private PrivateUser getPrivateUserByEmail(String email) {
        if(Objects.equals(email, "asd")) return null;
        return new PrivateUser();
    }

    private PrivateUser getPrivateUserByToken(String token){
        if(Objects.equals(token, "asd")) return null;
        return new PrivateUser();
    }

    private PublicUser getPublicUser(String nickname){
        if(Objects.equals(nickname, "asd")) return null;
        return new PublicUser();
    }

    private PublicUser getPublicUserByToken(String token) {
        if(Objects.equals(token, "asd")) return null;
        return new PublicUser();
    }
}
