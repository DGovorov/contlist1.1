package com.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public final class AuthChecker {

    private static final String DOMAINNAME = "localhost:8080";


    final AuthResponseHandler authHandler = new AuthResponseHandler();
    final AuthDAO authDAO = new AuthDAO();

    private String CODESESSIONNAME = authHandler.CODESESSIONNAME;
    private String EMAILSESSIONNAME = authHandler.EMAILSESSIONNAME;

    public AuthChecker() {

    }

    public Boolean CkeckStatus(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Boolean auth = false;

        List<Cookie> gotcookies = authHandler.checkcookies(request);
        if (authHandler.iscookies(gotcookies)) {
            Cookie emCookie = gotcookies.get(1);
            Cookie coCookie = gotcookies.get(2);

            String dbCode = authDAO.getUserCodeByEmail(emCookie.getValue());

            if (dbCode != null)
                if (dbCode.equals(coCookie.getValue())) {
                    authHandler.setsession(session, emCookie.getValue(), coCookie.getValue());
                    auth = true;
                }
        }

        handleCallback(session,request,response);

        if ((session.getAttribute(CODESESSIONNAME) == null) || (session.getAttribute(EMAILSESSIONNAME) == null)) {
            auth = false;
        }

        if ((session.getAttribute(CODESESSIONNAME) != null) && (session.getAttribute(EMAILSESSIONNAME) != null)) {
            auth = true;
        }
        return auth;
    }

    public void handleCallback(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ((session.getAttribute(CODESESSIONNAME) == null) || (session.getAttribute(EMAILSESSIONNAME) == null))
            if ((request.getParameter("code") != null) && (request.getParameter("state") != null)) {

                authHandler.handleOauthResponse(request, response, session);
            }
    }

    public String AuthMessageMainPage(Boolean auth, HttpSession session, HttpServletRequest req) throws UnknownHostException {
        String message = null;

        String curCookieEm = "empty";
        String curCookieCo = "empty";
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = req.getRemoteAddr();
        }

        if (!authHandler.checkcookies(req).isEmpty())
            if ((authHandler.checkcookies(req).get(1) != null) && (authHandler.checkcookies(req).get(2)) != null) {
                curCookieEm = authHandler.checkcookies(req).get(1).getValue();
                curCookieCo = authHandler.checkcookies(req).get(2).getValue();
            }

        if (!auth)
            message = "UserAgent - " + req.getHeader("User-Agent") + "<br>"
                    + "IP - " + ip + "<br>"
                    + "Email in cookies - " + curCookieEm + "<br>"
                    + "Code in cookies - " + curCookieCo;
        if (auth)
            message = "Authorised as " + session.getAttribute(EMAILSESSIONNAME) + "<br>"
                    + "UserAgent - " + req.getHeader("User-Agent") + "<br>"
                    + "IP - " + ip + "<br>"
                    + "Email in cookies - " + curCookieEm + "<br>"
                    + "Code in cookies - " + curCookieCo;
        return message;
    }

    public void logOut(HttpSession session, HttpServletRequest req, HttpServletResponse resp) {
        session.removeAttribute(CODESESSIONNAME);
        session.removeAttribute(EMAILSESSIONNAME);
        Cookie cookies[] = req.getCookies();
        Boolean emRemoved = false;
        Boolean coRemoved = false;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(authHandler.CODECOOKIENAME)) {
                    emRemoved = true;
                    authHandler.eraseCookie(cookies[i], resp);
                }
                if (cookies[i].getName().equals(authHandler.EMAILCOOKIENAME)) {
                    coRemoved = true;
                    authHandler.eraseCookie(cookies[i], resp);
                }
                if ((emRemoved) && (coRemoved)) {
                    break;
                }
            }
        }
    }

    public void logInGoogle(HttpServletResponse response) throws IOException {
        final GoogleAuthHelper helper = new GoogleAuthHelper();
        response.sendRedirect(helper.buildLoginUrl());
    }

    public void logInVk(HttpServletResponse response) throws IOException {
        final VkAuthHelper helper = new VkAuthHelper();
        response.sendRedirect(helper.buildLoginUrl());
    }


    public void logInFb(HttpServletResponse response) throws IOException {
        final FbAuthHelper helper = new FbAuthHelper();
        response.sendRedirect(helper.getFBAuthUrl());
    }
}
