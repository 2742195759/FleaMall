package com.example.homepage;

/**
 * Created by xuan on 2018/3/14.
 */

public class Message1 {
    private String name;
    private int imageId;
    public Message1(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }
    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }
}
