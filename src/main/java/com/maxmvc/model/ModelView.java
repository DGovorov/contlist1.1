package com.maxmvc.model;

import java.util.ArrayList;
import java.util.List;

public class ModelView {
    public List getContacts() {
        List contacts = new ArrayList();
        contacts.add("one"); //test list
        contacts.add("two");
        contacts.add("three");
        return(contacts);
    }
}