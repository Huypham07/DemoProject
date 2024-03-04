package com.example.demoproject;

public class ItemOverview {
    private String name;
    private int imgid;
    private int notice;

    public ItemOverview(String name, int imgid, int notice) {
        this.name = name;
        this.imgid = imgid;
        this.notice = notice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }
}
