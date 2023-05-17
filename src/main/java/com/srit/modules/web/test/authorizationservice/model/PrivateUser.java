package com.srit.modules.web.test.authorizationservice.model;

import com.srit.modules.web.lib.types.JsonResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateUser extends PublicUser{
    private String password;
    private String email;

    @Override
    public JsonResponse toJsonResponse() {
        return super.toJsonResponse().put("password", password).put("email", email);
    }

    public PublicUser toPublicUser(){
        return (PublicUser) this;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
