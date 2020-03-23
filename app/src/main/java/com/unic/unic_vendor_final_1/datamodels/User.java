package com.unic.unic_vendor_final_1.datamodels;

public class User {
    private String fullName,Email,phoneNo;

    public User(){}

    public User(String fullName,String Email,String phoneNo){
        this.fullName = fullName;
        this.Email = Email;
        this.phoneNo = phoneNo;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
