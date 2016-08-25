package com.auth;

import com.maxmvc.entity.Contctlist;
import com.maxmvc.model.CreateContDb;
import com.maxmvc.model.ModelView;
import com.maxmvc.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class AuthDB {

    public AuthDB() {

    }

    public String getUserCodeByEmail(String email) {
        return(getUserByEmail(email))[5];
    }

    public Long getUserIdByEmail(String email) {
        return Long.valueOf((getUserByEmail(email))[0]);
    }

    public Long getUserStateByEmail(String email) {
        return Long.valueOf((getUserByEmail(email))[4]);
    }

    public String[] getUserByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "FROM com.maxmvc.entity.Contctlist E WHERE E.email = '"+email+"'";
        Query query = session.createQuery(hql);

        List<Contctlist> contacts = query.list();
        String[] rContacts = new String[6];
        for (Contctlist cont : contacts){
            rContacts = new String[] {cont.getUserId().toString(), cont.getFirstName(), cont.getLastName(), cont.getTelNum(), cont.getEmail(), cont.getAddress()};
        }
        return(rContacts);
    }

    public void updateUserOauthCode(Long id, String code) {
        CreateContDb dbupdate = new CreateContDb();
        dbupdate.updateContact(String.valueOf(id), "", "", "", code, "");
    }

    public void createOauthUser(String parsename, String parselastname, String parseemail, String code, String state) {
        CreateContDb ccd = new CreateContDb();
        ccd.setContacts(parsename, parselastname, parseemail, code, state);
    }

    public List getDBcontacts() {
        ModelView be = new ModelView();
        List result = be.getContacts();
        return result;
    }
}
