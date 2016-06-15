package com.maxmvc.persistence;

import com.maxmvc.model.CreateContDb;
import com.maxmvc.model.ModelView;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/create")
public class CreateContact extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List result = new ArrayList();
        result.add("First name"); //test list
        result.add("Last Name");
        result.add("Email");
        result.add("TelNum");
        result.add("Address");
        req.setAttribute("name", result);
        req.getRequestDispatcher("createjsp.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateContDb ccd = new CreateContDb();
        ccd.setContacts(
                req.getParameter("First name"),
                req.getParameter("Last Name"),
                req.getParameter("Email"),
                req.getParameter("Address"),
                req.getParameter("TelNum"));


        resp.sendRedirect("/");
    }
}
