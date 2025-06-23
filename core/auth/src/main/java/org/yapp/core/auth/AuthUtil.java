package org.yapp.core.auth;

public class AuthUtil {

    private static int BEARER_PREFIX_LENGTH = 7;

    public static String extractAccessToken(String authorizationHeader) {
        return authorizationHeader.substring(BEARER_PREFIX_LENGTH);
    }
}
