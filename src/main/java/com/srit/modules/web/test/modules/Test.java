package com.srit.modules.web.test.modules;

import com.srit.modules.web.lib.Client;
import com.srit.modules.web.lib.Module;
import com.srit.modules.web.lib.Route;
import com.srit.modules.web.lib.RouteSettings;
import com.srit.modules.web.lib.types.HttpRequest;
import com.srit.modules.web.lib.types.HttpResponse;
import com.srit.modules.web.lib.types.JsonResponse;

public class Test extends Module {

    private Route disable;

    public Test(Client client) {
        disable = new Route(client) {
            @Override
            @RouteSettings(method = "GET", name = "/")
            protected void callback(HttpRequest req, HttpResponse res) {
                this.disable();
                res.send(
                        new JsonResponse() {
                            {
                                put("code", 200);
                                put("message", "OK");
                                setCode(200);
                            }
                        }
                );
            }
        };
        addRoute(disable);
        enable();
    }
}
