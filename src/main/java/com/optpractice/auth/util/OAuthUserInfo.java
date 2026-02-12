package com.optpractice.auth.util;

import com.optpractice.user.entity.AuthType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserInfo {
    private String name;

    private String oauthId;

    private AuthType authType;
}
