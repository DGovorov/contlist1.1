package com.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public final class AuthChecker {

    private static final String DOMAINNAME = "localhost:8080";
    private static final String CODECOOKIENAME = "pdbcode";
    private static final String EMAILCOOKIENAME = "pdbemail";

    private static final String CODESESSIONNAME = "pdbcode";
    private static final String EMAILSESSIONNAME = "pdbemail";

    public AuthChecker() {

    }

    public Boolean CkeckStatus(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        final GoogleAuthHelper googleHelper = new GoogleAuthHelper();
        final VkAuthHelper vkHelper = new VkAuthHelper();

        final AuthDB authDB = new AuthDB();
        Boolean auth = false;

        List<Cookie> gotcookies = checkcookies(request);
        if (iscookies(gotcookies)) {
            Cookie emCookie = gotcookies.get(1);
            Cookie coCookie = gotcookies.get(2);

            String dbCode = authDB.getUserCodeByEmail(emCookie.getValue());

            if (dbCode != null)
                if (dbCode.equals(coCookie.getValue())) {
                    setsession(session, emCookie.getValue(), coCookie.getValue());
                    auth = true;
                }
        }

        if ((session.getAttribute("code") == null) || (session.getAttribute("email") == null))
            if ((request.getParameter("code") != null) && (request.getParameter("state") != null)) {

                String state = request.getParameter("state");
                String code = request.getParameter("code");
                String[] userData = new String[3];
                String id = null;

                if (state.startsWith("google"))
                    userData = getUserDataGoogle(code, googleHelper);
                /*if (state.startsWith("vk")) {
                    userData = getUserDataVk(code, vkHelper);
                }*/
                /*if (state.startsWith("fb"))
                    userData = getUserDataFb(state, code, fbHelper);*/

                String parseemail = userData[0];
                String parsename = userData[1];
                String parselastname = userData[2];

                String email = parseemail;
                Boolean emailmatch = false;

                String securecode = secureCodeGenerate(request).substring(0, 99);

                setcookies(response, email, securecode);
                setsession(session, email, securecode);

                emailmatch = authDB.checkExistingEmail(parseemail);
                if (emailmatch) {
                    Long contUpdateId = authDB.getUserIdByEmail(email);
                    authDB.updateUserOauthCode(contUpdateId, securecode);
                }
                if (!emailmatch) {
                    authDB.createOauthUser(parsename, parselastname, parseemail, securecode, state);
                    if (state.startsWith("google"))
                        response.sendRedirect(googleHelper.buildLoginUrl());
                    /*if (state.startsWith("vk"))
                        response.sendRedirect(vkHelper.buildLoginUrl());*/
                    /*if (state.startsWith("fb"))
                        userData = getUserDataFb(state, code, fbHelper);*/

                }
            }

        if ((session.getAttribute("code") == null) || (session.getAttribute("email") == null)) {
            auth = false;
        }

        if ((session.getAttribute("code") != null) && (session.getAttribute("email") != null)) {
            auth = true;
        }
        return auth;
    }

    private String[] getUserDataVk(String code, VkAuthHelper vkHelper) throws IOException {

        String tokenUri = vkHelper.accessTokenUri(code);
        URL tokenurl = new URL(tokenUri);
        HttpURLConnection connectionToken = (HttpURLConnection)tokenurl.openConnection();
        connectionToken.setRequestMethod("GET");
        connectionToken.connect();
        String tokenresp = connectionToken.getResponseMessage();
        JsonParser parserToken = new JsonParser();
        JsonObject mainObjectToken = parserToken.parse(tokenresp).getAsJsonObject();
        String parsetoken = mainObjectToken.get("access_token").getAsString();
        String parseiud = mainObjectToken.get("user_id").getAsString();


        String[] fields = new String[]{"first_name", "last_name", "email"};
        String userDataUrl = vkHelper.userInfoUri(Integer.parseInt(parseiud), fields, parsetoken);
        URL url = new URL(userDataUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        String response = connection.getResponseMessage();
        JsonParser parser = new JsonParser();
        JsonObject mainObject = parser.parse(response).getAsJsonObject();
        String parseemail = mainObject.get("email").getAsString();
        String parsename = mainObject.get("first_name").getAsString();
        String parselastname = mainObject.get("last_name").getAsString();


        return new String[]{parseemail, parsename, parselastname};
    }

    private String secureCodeGenerate(HttpServletRequest req) {
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = req.getRemoteAddr();
        }
        String useragent = req.getHeader("User-Agent").replace("/", "").replace(" ", "").replace(";", "");
        return ip + "." + useragent;
    }

    private String[] getUserDataGoogle(String code, GoogleAuthHelper googleHelper) throws IOException {

        String respEmail = googleHelper.getUserInfoJson(code);
        JsonParser parser = new JsonParser();

        JsonObject mainObject = parser.parse(respEmail).getAsJsonObject();
        String parseemail = mainObject.get("email").getAsString();
        String parsename = mainObject.get("given_name").getAsString();
        String parselastname = mainObject.get("family_name").getAsString();

        return new String[]{parseemail, parsename, parselastname};
    }

    public String AuthMessageMainPage(Boolean auth, HttpSession session, HttpServletRequest req) throws UnknownHostException {
        String message = null;
        final GoogleAuthHelper helper = new GoogleAuthHelper();
        String curCookieEm = "empty";
        String curCookieCo = "empty";
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = req.getRemoteAddr();
        }

        if (!checkcookies(req).isEmpty())
            if ((checkcookies(req).get(1) != null) && (checkcookies(req).get(2)) != null) {
                curCookieEm = checkcookies(req).get(1).getValue();
                curCookieCo = checkcookies(req).get(2).getValue();
            }

        if (!auth)
            message = "UserAgent - " + req.getHeader("User-Agent") + "<br>"
                    + "IP - " + ip + "<br>"
                    + "Email in cookies - " + curCookieEm + "<br>"
                    + "Code in cookies - " + curCookieCo;
        if (auth)
            message = "Authorised as " + session.getAttribute("email") + "<br>"
                    + "UserAgent - " + req.getHeader("User-Agent") + "<br>"
                    + "IP - " + ip + "<br>"
                    + "Email in cookies - " + curCookieEm + "<br>"
                    + "Code in cookies - " + curCookieCo;
        return message;
    }

    public void logOut(HttpSession session, HttpServletRequest req, HttpServletResponse resp) {
        session.removeAttribute("code");
        session.removeAttribute("email");
        Cookie cookies[] = req.getCookies();
        Boolean emRemoved = false;
        Boolean coRemoved = false;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(CODECOOKIENAME)) {
                    emRemoved = true;
                    eraseCookie(cookies[i], resp);
                }
                if (cookies[i].getName().equals(EMAILCOOKIENAME)) {
                    coRemoved = true;
                    eraseCookie(cookies[i], resp);
                }
                if ((emRemoved) && (coRemoved)) {
                    break;
                }
            }
        }
    }

    public void logIn(HttpServletResponse response) throws IOException {
        final GoogleAuthHelper helper = new GoogleAuthHelper();
        response.sendRedirect(helper.buildLoginUrl());
    }

    private void setcookies(HttpServletResponse resp, String email, String code) {
        //String cleancode = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36";
        Cookie codecookie = new Cookie(CODECOOKIENAME, code);
        //codecookie.setSecure(true);
        //codecookie.setDomain(DOMAINNAME);
        resp.addCookie(codecookie);
        Cookie emailcookie = new Cookie(EMAILCOOKIENAME, email);
        //emailcookie.setSecure(true);
        //emailcookie.setDomain(DOMAINNAME);
        resp.addCookie(emailcookie);
    }

    private void setsession(HttpSession session, String email, String code) {
        session.setAttribute("email", email);
        session.setAttribute("code", code);
    }

    private List<Cookie> checkcookies(HttpServletRequest req) {
        Cookie cookies[] = req.getCookies();
        Cookie emailCookie = null;
        Cookie codeCookie = null;
        List bothcookies = new ArrayList<Cookie>();
        bothcookies.add(null);
        bothcookies.add(null);
        bothcookies.add(null);
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(CODECOOKIENAME)) {
                    codeCookie = cookies[i];
                }
                if (cookies[i].getName().equals(EMAILCOOKIENAME)) {
                    emailCookie = cookies[i];
                }
                if ((emailCookie != null) && (codeCookie != null)) {
                    break;
                }
            }
        }
        if ((emailCookie != null) && (codeCookie != null)) {
            bothcookies.add(1, emailCookie);
            bothcookies.add(2, codeCookie);
        }
        return bothcookies;
    }

    private Boolean iscookies(List cookies) {
        if (!cookies.isEmpty()) {
            if ((cookies.get(1) != null) && (cookies.get(2) != null))
                return true;
            else return false;
        } else return false;
    }

    private void eraseCookie(Cookie cookie, HttpServletResponse resp) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

}
