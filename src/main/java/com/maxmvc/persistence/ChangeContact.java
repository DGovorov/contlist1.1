package com.maxmvc.persistence;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/change")
public class ChangeContact extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //resp.getAttribute("id");

        List result = new ArrayList();
        result.add("First name"); //test list
        result.add("Last Name");
        result.add("Email");
        result.add("TelNum");
        result.add("Address");
        req.setAttribute("name", result);
        //req.setAttribute("id", result);
        req.getRequestDispatcher("changejsp.jsp").forward(req, resp);

    }
}
