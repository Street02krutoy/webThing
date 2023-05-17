package com.srit.modules.web.test.authorizationservice.model;

import com.srit.modules.web.lib.types.JsonResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicUser {
    private String nickname;
    private String displayName;

    public JsonResponse toJsonResponse() {
        return new JsonResponse(){{
            put("nickname", nickname);
            put("displayName", displayName);
        }};
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
