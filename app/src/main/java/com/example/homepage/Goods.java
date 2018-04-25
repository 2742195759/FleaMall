package com.example.homepage;

/**
 * Created by xuan on 2018/3/13.
 */

public class Goods {
    private String name;
    private int imageId;
    public Goods(String name, int imageId) {
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
