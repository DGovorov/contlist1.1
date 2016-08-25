package com.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
        final GoogleAuthHelper helper = new GoogleAuthHelper();
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
            if (request.getParameter("code") != null) {
                String respEmail = helper.getUserInfoJson(request.getParameter("code"));
                JsonParser parser = new JsonParser();

                JsonObject mainObject = parser.parse(respEmail).getAsJsonObject();
                String parseemail = mainObject.get("email").getAsString();
                String parsename = mainObject.get("given_name").getAsString();
                String parselastname = mainObject.get("family_name").getAsString();

                String email = "";
                Boolean emailmatch = false;
                List<String[]> contactsemails = (List) request.getAttribute("customparam");
                for (String[] cont : contactsemails) {
                    if (parseemail.equals(cont[4])) {
                        emailmatch = true;
                        email = cont[4];

                        setcookies(response, email, request.getParameter("code"));
                        setsession(session, email, request.getParameter("code"));

                        Long contUpdateId = authDB.getUserIdByEmail(email);
                        authDB.updateUserOauthCode(contUpdateId, request.getParameter("code"));

                        break;
                    }
                }
                if (!emailmatch) {
                    authDB.createOauthUser(parsename, parselastname, parseemail, request.getParameter("code"), request.getParameter("state"));
                    response.sendRedirect(helper.buildLoginUrl());
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

    public String AuthMessageMainPage(Boolean auth, HttpSession session, HttpServletRequest req) {
        String message = null;
        final GoogleAuthHelper helper = new GoogleAuthHelper();
        String curCookieEm = "empty";
        String curCookieCo = "empty";
        if (!checkcookies(req).isEmpty())
            if ((checkcookies(req).get(1) != null) && (checkcookies(req).get(2)) != null) {
                curCookieEm = checkcookies(req).get(1).getValue();
                curCookieCo = checkcookies(req).get(2).getValue();
            }

        if (!auth)
            message = "Email in cookies - " + curCookieEm + "<br>"
                    + "Code in cookies - " + curCookieCo;
        if (auth)
            message = "Authorised as " + session.getAttribute("email") + "<br>"
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
