package com.b2infosoft.addley.model;

/**
 * Created by rajesh on 5/13/2016.
 */

public class Category {
    private int id;
    private String name;
    private int count;
    private String img;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replace("&amp;","&");
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImg() {
        if(img==null)
            return  "addley.png";
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
