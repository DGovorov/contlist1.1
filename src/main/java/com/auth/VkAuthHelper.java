package com.auth;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;


public final class VkAuthHelper {

    private static final String CLIENT_ID = "5606005";
    private static final String CLIENT_SECRET = "7rDYJ5usypoU0v76QEeR";

    private static final String CALLBACK_URI = "http://localhost:8080/";
    private static final String SCOPE = "email";
    private static final String VK_AUTH_URI = "https://oauth.vk.com/authorize";
    private static final String DISPLAY_TYPE = "page";
    private static final String RESPONSE_TYPE = "code";
    private static final String VERSION = "5.53";
    private static final String METHOD_URI = "https://api.vk.com/method/";
    private String stateToken;

    public VkAuthHelper() {
    }

    public String buildLoginUrl() {
        generateStateToken();
        String url = VK_AUTH_URI + "?" +
                "client_id=" + CLIENT_ID + "&" +
                "redirect_uri=" + CALLBACK_URI + "&" +
                "display=" + DISPLAY_TYPE + "&" +
                "scope=" + SCOPE + "&" +
                "response_type=" + RESPONSE_TYPE + "&" +
                "v=" + VERSION + "&" +
                "state=" + stateToken;
        return url;
    }

    public String accessTokenUri(String code) {
        return "https://oauth.vk.com/access_token?client_id="
                + CLIENT_ID
                + "&client_secret=" + CLIENT_SECRET
                + "&redirect_uri=" + CALLBACK_URI
                + "&code=" + code;
    }

    public String userInfoUri(String vkUserId, String[] fields, String authToken) {
        return METHOD_URI + "users.get?" +
                "uids=" + vkUserId + "&" +
                "fields=" + String.join(",", fields) + "&" +
                "access_token=" + authToken;
    }

    private void generateStateToken() {

        SecureRandom sr1 = new SecureRandom();
        stateToken = "vk;" + sr1.nextInt();

    }

    private String[] getTokenVk(VkAuthHelper vkHelper, String code) throws IOException {
        String tokenUri = vkHelper.accessTokenUri(code);
        URL tokenurl = new URL(tokenUri);
        HttpURLConnection connectionToken = (HttpURLConnection) tokenurl.openConnection();
        connectionToken.setRequestMethod("GET");
        connectionToken.connect();

        String tokenresp = getRespAsJSONString(connectionToken);

        JsonParser parserToken = new JsonParser();
        JsonObject mainObjectToken = parserToken.parse(tokenresp).getAsJsonObject();
        String parsetoken = mainObjectToken.get("access_token").getAsString();
        String parseiud = mainObjectToken.get("user_id").getAsString();
        String parseemail = mainObjectToken.get("email").getAsString();

        return new String[]{parseemail, parseiud, parsetoken};
    }

    private String[] getNameVk(VkAuthHelper vkHelper, String parseiud, String parsetoken) throws IOException {

        String[] fields = new String[]{"first_name", "last_name"};

        String userDataUrl = vkHelper.userInfoUri(parseiud, fields, parsetoken);

        URL url = new URL(userDataUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        String response = getRespAsJSONString(connection);

        JsonParser parser = new JsonParser();
        JsonObject mainObject = parser.parse(response).getAsJsonObject();
        //System.out.print("Object " + mainObject);
        JsonObject innerObject = mainObject.get("response").getAsJsonArray().get(0).getAsJsonObject();

        String parselastname = innerObject.get("last_name").getAsString();
        String parsename = innerObject.get("first_name").getAsString();


        return new String[]{parsename, parselastname};
    }

    public String[] getUserDataVk(String code, VkAuthHelper vkHelper) throws IOException {

        String[] tokendata = getTokenVk(vkHelper, code);
        String parseemail = tokendata[0];
        String parseiud = tokendata[1];
        String parsetoken = tokendata[2];
        System.out.print("URL " + parsetoken + " " + parseiud + " " + parseemail);
        String[] namedata = getNameVk(vkHelper, parseiud, parsetoken);
        String parsename = namedata[0];
        String parselastname = namedata[1];

        return new String[]{parseemail, parsename, parselastname};
    }

    public String getStateToken() {
        return stateToken;
    }

    private String getRespAsJSONString(HttpURLConnection connectionToken) throws IOException {
        String tokenresp;
        BufferedReader br;

        if (200 <= connectionToken.getResponseCode() && connectionToken.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader((connectionToken.getInputStream()), "UTF-8"));
        } else {
            br = new BufferedReader(new InputStreamReader((connectionToken.getErrorStream()), "UTF-8"));
        }

        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null)
            sb.append(output);

        tokenresp = sb.toString();
        return tokenresp;
    }


}
