package com.univ.chat.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Comm implements Serializable {
    @Expose
    private String from ;
    @Expose
    private String to ;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
