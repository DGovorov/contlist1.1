package com.maxmvc.model;

import com.maxmvc.entity.Contctlist;
import com.maxmvc.persistence.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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

    public void updateContact(String id, String fn, String ln, String em, String adr, String tel) {
        SessionFactory sessionFact = HibernateUtil.getSessionFactory();
        Session session = sessionFact.openSession();
        Transaction tx = session.beginTransaction();
        Contctlist user = (Contctlist) session.load(Contctlist.class, Long.valueOf(id));
        System.out.println("User object loaded. " + user);
        tx.commit();

        if (fn!="") user.setFirstName(fn); else user.setFirstName(user.getFirstName());
        if (ln!="") user.setLastName(ln); else user.setLastName(user.getLastName());
        if (em!="") user.setEmail(em); else user.setEmail(user.getEmail());
        if (adr!="") user.setAddress(adr); else user.setAddress(user.getAddress());
        if (tel!="") user.setTelNum(tel); else user.setTelNum(user.getTelNum());

        Transaction txUpd = session.beginTransaction();
        session.update(user);
        txUpd.commit();
    }
}