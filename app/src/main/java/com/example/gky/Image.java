package com.example.gky;

import java.io.Serializable;

public class Image implements Serializable {

    public  String imgname, imgurl, title;

    public Image() {
    }

    public Image(String imgname, String imgurl, String title) {
        this.imgname = imgname;
        this.imgurl = imgurl;
        this.title = title;
    }

    public String getImgname() {
        return imgname;
    }

    public  String getImgurl() {
        return imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}

