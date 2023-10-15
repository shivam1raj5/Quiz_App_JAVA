package com.example.quiz_app_java;

public class CategoryModel {

    private String imageurl, title;

    public CategoryModel(String imageurl, String title) {
        this.imageurl = imageurl;
        this.title = title;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

