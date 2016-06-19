package com.maxmvc.persistence;

import com.maxmvc.model.CreateContDb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.maxmvc.model.ModelView.getContactById;

@WebServlet("/change")
public class ChangeContact extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.valueOf(req.getParameter("id"));
        String[] thisCont = getContactById(id);
        List<String[]> result = new ArrayList();
        result.add(new String[]{"First name", String.valueOf(thisCont[1])}); //test list
        result.add(new String[]{"Last Name", String.valueOf(thisCont[2])});
        result.add(new String[]{"Email", String.valueOf(thisCont[4])});
        result.add(new String[]{"TelNum", String.valueOf(thisCont[3])});
        result.add(new String[]{"Address", String.valueOf(thisCont[5])});
        req.setAttribute("name", result);
        //req.setAttribute("thiscont", thisCont);
        req.getRequestDispatcher("changejsp.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        CreateContDb ccd = new CreateContDb();
        ccd.updateContact(
                req.getParameter("id"),
                req.getParameter("First name"),
                req.getParameter("Last Name"),
                req.getParameter("Email"),
                req.getParameter("Address"),
                req.getParameter("TelNum"));


        resp.sendRedirect("/");
    }
}
