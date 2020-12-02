package com.example.covidtrucking.ui.organisations;

import com.example.covidtrucking.OrganisationActivity;
import com.example.covidtrucking.UserHelperClass;

import java.util.Date;

public class VisitOrgHelperClass {
    private OrganisationHelperClass organisation;
    private UserHelperClass user;
    private Date visitTime;
    private Date outTime;
    private Boolean isIn;

    public VisitOrgHelperClass() {
    }

    public VisitOrgHelperClass(OrganisationHelperClass organisation, UserHelperClass user, Date visitTime, Date outTime, Boolean isIn) {
        this.organisation = organisation;
        this.user = user;
        this.visitTime = visitTime;
        this.outTime = outTime;
        this.isIn = isIn;
    }

    public Boolean getIn() {
        return isIn;
    }

    public void setIn(Boolean in) {
        isIn = in;
    }

    public OrganisationHelperClass getOrganisation() {
        return organisation;
    }

    public void setOrganisation(OrganisationHelperClass organisation) {
        this.organisation = organisation;
    }

    public UserHelperClass getUser() {
        return user;
    }

    public void setUser(UserHelperClass user) {
        this.user = user;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
}
