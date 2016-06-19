package com.maxmvc.model;

import com.maxmvc.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import com.maxmvc.entity.Contctlist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelView {
    public List getContacts() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "FROM com.maxmvc.entity.Contctlist";
        Query query = session.createQuery(hql);

        List<Contctlist> contacts = query.list();
        List<String[]> rContacts = new ArrayList<String[]>();
        for (Contctlist cont : contacts){
            rContacts.add(new String[] {cont.getUserId().toString(), cont.getFirstName(), cont.getLastName(), cont.getTelNum(), cont.getEmail(), cont.getAddress()});
        }
        return(rContacts);
    }

    public static void removeById(String id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        //id="1"; //test
        Query query =  session.createQuery("delete com.maxmvc.entity.Contctlist where userId = :param");
        query.setParameter("param", Long.valueOf(id));
        int result = query.executeUpdate();
        System.out.println("Rows affected: " + result);
        session.getTransaction().commit();
    }

    public static String[] getContactById(Long id) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "FROM com.maxmvc.entity.Contctlist E WHERE E.userId = "+id;
        Query query = session.createQuery(hql);

        List<Contctlist> contacts = query.list();
        String[] rContacts = new String[6];
        for (Contctlist cont : contacts){
            rContacts = new String[] {cont.getUserId().toString(), cont.getFirstName(), cont.getLastName(), cont.getTelNum(), cont.getEmail(), cont.getAddress()};
        }
        return(rContacts);
    }

}