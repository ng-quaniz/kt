package com.example.gky;

public class listview {
    public String name, title;
    public   int img;

    public listview(){

    }


    public listview(String name, int img , String title ){
        this.name = name;
        this.img = img;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
