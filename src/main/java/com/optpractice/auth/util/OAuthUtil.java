package com.optpractice.auth.util;

public interface OAuthUtil {
    OAuthUserInfo getUserInfoFromOAuthToken(String oAuthToken);
}
