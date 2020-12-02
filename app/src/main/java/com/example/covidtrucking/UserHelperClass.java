package com.example.covidtrucking;

public class UserHelperClass {
    String first_name, other_names, phone, email, national_id, image_url;

    public UserHelperClass() {
    }

    public UserHelperClass(String national_id, String first_name, String other_names, String phone, String email, String image_url) {
        this.first_name = first_name;
        this.other_names = other_names;
        this.phone = phone;
        this.email = email;
        this.national_id = national_id;
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getOther_names() {
        return other_names;
    }

    public void setOther_names(String other_names) {
        this.other_names = other_names;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
