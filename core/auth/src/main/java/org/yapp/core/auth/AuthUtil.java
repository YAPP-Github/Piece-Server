package org.yapp.core.auth;

public class AuthUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    public static String extractAccessToken(String authorizationHeader) {
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

    public static String getBearerPrefix() {
        return AuthUtil.BEARER_PREFIX;
    }

    public static int getBearPrefixLength() {
        return BEARER_PREFIX.length();
    }
}
