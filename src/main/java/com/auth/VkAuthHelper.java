package com.auth;



import java.io.IOException;
import java.security.SecureRandom;


public final class VkAuthHelper {


    private static final String CLIENT_ID = "5606005";
    private static final String CLIENT_SECRET = "7rDYJ5usypoU0v76QEeR";

    private static final String CALLBACK_URI = "http://localhost:8080/";
    private static final String SCOPE = "email";
    private static final String USER_INFO_URI = "https://oauth.vk.com/authorize";
    private static final String DISPLAY_TYPE = "page";
    private static final String RESPONSE_TYPE = "code";
    private static final String VERSION = "5.53";
    private static final String METHOD_URI = "https://api.vk.com/method/";
    private String stateToken;


    public VkAuthHelper() {

    }

    public String buildLoginUrl() {
        generateStateToken();
        String url = USER_INFO_URI + "?" +
                "client_id=" + CLIENT_ID + "&" +
                "redirect_uri=" + CALLBACK_URI + "&" +
                "display=" + DISPLAY_TYPE + "&" +
                "scope=" + SCOPE + "&" +
                "response_type=" + RESPONSE_TYPE + "&" +
                "scope=" + SCOPE + "&" +
                "v=" + VERSION + "&" +
                "state=" + stateToken;
        return url;
    }

    public String accessTokenUri(String code) {
        return "https://oauth.vk.com/access_token?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + code;
    }

    public String userInfoUri(int vkUserId, String[] fields, String authToken) {
        return METHOD_URI + "users.get?" +
                "uids=" + vkUserId + "&" +
                "fields=" + String.join(",", fields) + "&" +
                "access_token=" + authToken;
    }

    private void generateStateToken() {

        SecureRandom sr1 = new SecureRandom();

        stateToken = "vk;" + sr1.nextInt();

    }

    public String getStateToken() {
        return stateToken;
    }


}
