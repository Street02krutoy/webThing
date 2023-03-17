package com.srit.modules.web.lib;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private List<Route> routes = new ArrayList<>();

    private boolean enabled = false;

    public void enable(){
         enabled = true;
         routes.forEach(Route::enable);
    };

    public void disable(){
        enabled = false;
        routes.forEach(Route::disable);
    }

    public void addRoute(Route route) {
        routes.add(route);
        if(enabled) route.enable();
        else route.disable();
    }

    public void removeRoute(int index) {
        Route route = routes.get(index);
        route.disable();
        routes.remove(index);
    }

    public List<Route> getRoutes() {
        return routes;
    }


}
