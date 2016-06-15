package com.maxmvc.model;

import com.maxmvc.entity.Contctlist;
import com.maxmvc.persistence.HibernateUtil;
import org.hibernate.Session;

public class CreateContDb {

    public void setContacts(String fn, String ln, String em, String adr, String tel) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        Contctlist user = new Contctlist();

        if (fn!=null) user.setFirstName(fn);
        if (ln!=null) user.setLastName(ln);
        if (em!=null) user.setEmail(em);
        if (adr!=null) user.setAddress(adr);
        if (tel!=null) user.setTelNum(tel);
        //user.setUserId(1L);   //to change piece of code
        //session.update(user);
        session.save(user);
        session.getTransaction().commit();
    }
}