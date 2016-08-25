package com.maxmvc.persistence;


import com.auth.AuthChecker;
import com.auth.GoogleAuthHelper;
import com.maxmvc.model.ModelView;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.maxmvc.model.ModelView.getContactById;


@WebServlet("/authorized")
public class AccessProtected extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        final AuthChecker checker = new AuthChecker();
        if (checker.CkeckStatus(req.getSession(), req, resp))
        req.getRequestDispatcher("protected.jsp").forward(req, resp);
        else
        req.getRequestDispatcher("denied.jsp").forward(req, resp);


    }
}
