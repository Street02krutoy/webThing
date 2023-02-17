package com.srit.modules.web.lib.types;

public interface AuthorizationChecker {
    public boolean check(HttpRequest req);
}
