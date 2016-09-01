package com.auth;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public final class FbAuthHelper {
    public static final String FB_APP_ID = "1146383145436395";
    public static final String FB_APP_SECRET = "7c2332f4b229fa531c700a9c48157fbb";
    public static final String REDIRECT_URI = "http://localhost:8080/";
    private String stateToken;

    static String accessToken = "";

    public String getFBAuthUrl() {
        String fbLoginUrl = "";
        generateStateToken();
        try {
            fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id="
                    + FbAuthHelper.FB_APP_ID + "&redirect_uri="
                    + URLEncoder.encode(FbAuthHelper.REDIRECT_URI, "UTF-8")
                    + "&scope=email"
                    + "&state=" + stateToken;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fbLoginUrl;
    }

    private void generateStateToken() {

        SecureRandom sr1 = new SecureRandom();
        stateToken = "fb;" + sr1.nextInt();

    }

    public String getFBGraphUrl(String code) {
        String fbGraphUrl = "";
        try {
            fbGraphUrl = "https://graph.facebook.com/oauth/access_token?"
                    + "client_id=" + FbAuthHelper.FB_APP_ID + "&redirect_uri="
                    + URLEncoder.encode(FbAuthHelper.REDIRECT_URI, "UTF-8")
                    + "&client_secret=" + FB_APP_SECRET + "&code=" + code;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fbGraphUrl;
    }

    public String getAccessToken(String code) {
        if ("".equals(accessToken)) {
            URL fbGraphURL;
            try {
                fbGraphURL = new URL(getFBGraphUrl(code));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Invalid code received " + e);
            }
            URLConnection fbConnection;
            StringBuffer b = null;
            try {
                fbConnection = fbGraphURL.openConnection();
                BufferedReader in;
                in = new BufferedReader(new InputStreamReader(
                        fbConnection.getInputStream()));
                String inputLine;
                b = new StringBuffer();
                while ((inputLine = in.readLine()) != null)
                    b.append(inputLine + "\n");
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to connect with Facebook "
                        + e);
            }

            accessToken = b.toString();
            if (accessToken.startsWith("{")) {
                throw new RuntimeException("ERROR: Access Token Invalid: "
                        + accessToken);
            }
        }
        return accessToken;
    }

    public String getFBGraph() {
        String graph = null;
        try {

            String g = "https://graph.facebook.com/me?fields=last_name,first_name,email&" + accessToken;
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine + "\n");
            in.close();
            graph = b.toString();
            System.out.println(graph);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR in getting FB graph data. " + e);
        }
        return graph;
    }

    public String[] getGraphData(String fbGraph) {
        Map fbProfile = new HashMap();
        String tokenresp = fbGraph;

        JsonParser parserToken = new JsonParser();
        JsonObject mainObjectToken = parserToken.parse(tokenresp).getAsJsonObject();
        String parseemail = mainObjectToken.get("email").getAsString();
        String parsefn = mainObjectToken.get("first_name").getAsString();
        String parseln = mainObjectToken.get("last_name").getAsString();

        return new String[]{parseemail, parsefn, parseln};
    }


    public String[] getUserDataFb(String code, FbAuthHelper fbHelper) {

        accessToken = fbHelper.getAccessToken(code);

        String graph = getFBGraph();

        String[] fbProfileData = getGraphData(graph);

        return fbProfileData;
    }
}
