package com.b2infosoft.addley.model;

/**
 * Created by rajesh on 4/19/2016.
 */
public class ItemSlideMenu {
    private int imgId;
    private String title;

    public ItemSlideMenu(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
