package com.univ.chat.model;

import java.io.Serializable;


public class User implements Serializable {
    private String sno;
    private String name;
    private String password;

    public String getId() {
        return sno;
    }

    public void setId(String id) {
        this.sno = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
