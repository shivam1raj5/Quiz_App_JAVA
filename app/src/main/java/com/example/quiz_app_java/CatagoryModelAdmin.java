package com.example.quiz_app_java;

import java.util.List;

public class CatagoryModelAdmin {

    private String name;
    private List<String> sets;
    private String url;
    String key;

    public  CatagoryModelAdmin(){
        //for firebase
    }

    public CatagoryModelAdmin(String name, List<String> sets, String url, String key) {
        this.name = name;
        this.sets = sets;
        this.url = url;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
