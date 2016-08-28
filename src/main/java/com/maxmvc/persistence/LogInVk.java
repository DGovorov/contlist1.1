package com.maxmvc.persistence;

import com.auth.AuthChecker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Максим on 13.08.2016.
 */
@WebServlet("/login/vk")
public class LogInVk extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        final AuthChecker checker = new AuthChecker();
        checker.logInVk(resp);
    }
}
