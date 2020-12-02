package com.example.covidtrucking.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> currentUsername;
    private MutableLiveData<String> currentEmail;
    private MutableLiveData<String> current_user_email;
    private MutableLiveData<String> current_user_otherName;
    private MutableLiveData<String> currentPhoneNumber;
    private MutableLiveData<String> currentNationalId;

//    public ProfileViewModel() {
//        currentUsername = new MutableLiveData<>();
//        currentUsername.setValue("Profile Screen");
//    }

    public LiveData<String> getCurrentUsername() {
        return currentUsername;
    }

//    public ProfileViewModel(MutableLiveData<String> currentUsername, MutableLiveData<String> currentEmail, MutableLiveData<String> current_user_email, MutableLiveData<String> current_user_otherName, MutableLiveData<String> currentPhoneNumber, MutableLiveData<String> currentNationalId) {
//        this.currentUsername = currentUsername;
//        this.currentEmail = currentEmail;
//        this.current_user_email = current_user_email;
//        this.current_user_otherName = current_user_otherName;
//        this.currentPhoneNumber = currentPhoneNumber;
//        this.currentNationalId = currentNationalId;
//    }

    public void setCurrentUsername(MutableLiveData<String> currentUsername) {
        this.currentUsername = currentUsername;
    }

    public MutableLiveData<String> getCurrentEmail() {
        return currentEmail;
    }

    public void setCurrentEmail(MutableLiveData<String> currentEmail) {
        this.currentEmail = currentEmail;
    }

    public MutableLiveData<String> getCurrent_user_email() {
        return current_user_email;
    }

    public void setCurrent_user_email(MutableLiveData<String> current_user_email) {
        this.current_user_email = current_user_email;
    }

    public MutableLiveData<String> getCurrent_user_otherName() {
        return current_user_otherName;
    }

    public void setCurrent_user_otherName(MutableLiveData<String> current_user_otherName) {
        this.current_user_otherName = current_user_otherName;
    }

    public MutableLiveData<String> getCurrentPhoneNumber() {
        return currentPhoneNumber;
    }

    public void setCurrentPhoneNumber(MutableLiveData<String> currentPhoneNumber) {
        this.currentPhoneNumber = currentPhoneNumber;
    }

    public MutableLiveData<String> getCurrentNationalId() {
        return currentNationalId;
    }

    public void setCurrentNationalId(MutableLiveData<String> currentNationalId) {
        this.currentNationalId = currentNationalId;
    }
}