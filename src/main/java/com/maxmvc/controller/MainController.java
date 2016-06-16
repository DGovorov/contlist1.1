package com.maxmvc.controller;

import com.maxmvc.model.ModelView;
import com.maxmvc.persistence.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.hibernate.Session;
import com.maxmvc.entity.Contctlist;
import com.maxmvc.persistence.HibernateUtil;

import static com.maxmvc.model.ModelView.removeById;

@WebServlet("/")
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //Session session = HibernateUtil.getSessionFactory()..
        ModelView be = new ModelView();
        List result = be.getContacts();

        req.setAttribute("customparam", result);
        req.getRequestDispatcher("mainview.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //resp.setContentType("text/html;charset=utf-8");
        if (req.getParameter("action").equals("remove")) removeById(req.getParameter("id"));

        resp.sendRedirect("/");
    }
}