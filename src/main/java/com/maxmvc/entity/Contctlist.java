package com.maxmvc.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contact_table")
public class Contctlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;
    @Column(name="FIRST_NAME")
    private String firstName = "nofirstname";
    @Column(name="LAST_NAME")
    private String lastName = "nolastname";
    @Column(name="TEL_NUM")
    private String telNum = "nonum";
    @Column(name="EMAIL")
    private String email = "noemail";
    @Column(name="ADDRESS")
    private String address = "noaddress";


    public Contctlist(String firstName, String lastName, String telNum, String email, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telNum = telNum;
        this.address = address;
    }

    public Contctlist() {
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}