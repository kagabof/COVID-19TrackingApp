package com.example.covidtrucking.ui.organisations;

public class LogoImageUpload {
    String name, imageUrl;

    public LogoImageUpload() {
    }

    public LogoImageUpload(String name, String imageUrl) {
        if(name.trim().equals("")){
            name = "No name";
        }
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
