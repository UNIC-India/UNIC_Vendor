package com.unic.unic_vendor_final_1.datamodels;

public class User {
    private String fullName,email,phoneNo;

    public User(){}

    public User(String fullName,String email,String phoneNo){
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
