package com.example.covidtrucking.ui.organisations;


import android.net.Uri;

public class OrganisationHelperClass {
    private String org_id;
    String name, description, location, logoImageUri, qrCodeUrl;

    public OrganisationHelperClass() {
    }

    public OrganisationHelperClass(String org_id, String name, String description, String location, String logoImageUri, String qrCodeUrl) {
        this.org_id = org_id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.logoImageUri = logoImageUri;
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogoImageUri() {
        return logoImageUri;
    }

    public void setLogoImageUri(String logoImageUri) {
        this.logoImageUri = logoImageUri;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
