package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id,fullName,email,phoneNo;

    public User(){
    }

    public User(String id,String fullName,String email,String phoneNo){
        this.id  = id;
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

    public String getId() {
        return id;
    }


}
