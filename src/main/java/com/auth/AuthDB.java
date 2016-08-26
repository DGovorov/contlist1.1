package com.auth;

import com.maxmvc.entity.Contctlist;
import com.maxmvc.model.CreateContDb;
import com.maxmvc.model.ModelView;
import com.maxmvc.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class AuthDB implements AuthDBApi {

    public AuthDB() {

    }

    public String getUserCodeByEmail(String email) {
        if (checkExistingEmail(email))
            return (getUserByEmail(email))[5];
        else return null;
    }

    public Long getUserIdByEmail(String email) {
        if (checkExistingEmail(email))
            return Long.valueOf((getUserByEmail(email))[0]);
        else return null;
    }

    public Long getUserStateByEmail(String email) {
        if (checkExistingEmail(email))
            return Long.valueOf((getUserByEmail(email))[4]);
        else return null;
    }

    public String[] getUserByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "FROM com.maxmvc.entity.Contctlist E WHERE E.email = '" + email + "'";
        Query query = session.createQuery(hql);

        List<Contctlist> contacts = query.list();
        if (!contacts.isEmpty()) {
            String[] rContacts = new String[6];
            for (Contctlist cont : contacts) {
                rContacts = new String[]{cont.getUserId().toString(), cont.getFirstName(), cont.getLastName(), cont.getTelNum(), cont.getEmail(), cont.getAddress()};
            }
            return (rContacts);
        } else return null;
    }

    public void updateUserOauthCode(Long id, String code) {
        CreateContDb dbupdate = new CreateContDb();
        dbupdate.updateContact(String.valueOf(id), "", "", "", code, "");
    }

    public Boolean checkExistingEmail(String email) {
        String[] userlist = getUserByEmail(email);
        if (userlist == null) return false;
        else return true;
    }

    public void createOauthUser(String parsename, String parselastname, String parseemail, String code, String state) {
        CreateContDb ccd = new CreateContDb();
        ccd.setContacts(parsename, parselastname, parseemail, code, state);
    }


}
