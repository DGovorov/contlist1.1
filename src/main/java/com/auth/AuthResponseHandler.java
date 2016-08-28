package com.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Максим on 28.08.2016.
 */
public class AuthResponseHandler {

    public static final String CODECOOKIENAME = "pdbcode";
    public static final String EMAILCOOKIENAME = "pdbemail";

    public void handleOauthResponse(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {



        final AuthDAO authDAO = new AuthDAO();

        String state = request.getParameter("state");
        String code = request.getParameter("code");
        String[] userData = new String[3];

        if (state.startsWith("google")) {
            final GoogleAuthHelper googleHelper = new GoogleAuthHelper();
            userData = googleHelper.getUserDataGoogle(code, googleHelper);
        }
        if (state.startsWith("vk")) {
            final VkAuthHelper vkHelper = new VkAuthHelper();
            userData = vkHelper.getUserDataVk(code, vkHelper);
        }

        String parseemail = userData[0];
        String parsename = userData[1];
        String parselastname = userData[2];

        Boolean emailmatch = false;

        String securecode = secureCodeGenerate(request).substring(0, 99);

        setcookies(response, parseemail, securecode);
        setsession(session, parseemail, securecode);

        emailmatch = authDAO.checkExistingEmail(parseemail);
        if (emailmatch) {
            Long contUpdateId = authDAO.getUserIdByEmail(parseemail);
            authDAO.updateUserOauthCode(contUpdateId, securecode);
        }
        if (!emailmatch) {
            authDAO.createOauthUser(parsename, parselastname, parseemail, securecode, state);
         /*if (state.startsWith("google"))
         response.sendRedirect(googleHelper.buildLoginUrl());
         if (state.startsWith("vk"))
         response.sendRedirect(vkHelper.buildLoginUrl());*/
         /*if (state.startsWith("fb"))
         userData = getUserDataFb(state, code, fbHelper);*/
        }
    }


    private String secureCodeGenerate(HttpServletRequest req) {
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = req.getRemoteAddr();
        }
        String useragent = req.getHeader("User-Agent").replace("/", "").replace(" ", "").replace(";", "");
        return ip + "." + useragent;
    }

    public void setcookies(HttpServletResponse resp, String email, String code) {
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

    public void setsession(HttpSession session, String email, String code) {
        session.setAttribute("email", email);
        session.setAttribute("code", code);
    }

    public List<Cookie> checkcookies(HttpServletRequest req) {
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

    public Boolean iscookies(List cookies) {
        if (!cookies.isEmpty()) {
            if ((cookies.get(1) != null) && (cookies.get(2) != null))
                return true;
            else return false;
        } else return false;
    }

    public void eraseCookie(Cookie cookie, HttpServletResponse resp) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

}