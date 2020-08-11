package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id,fullName,email,phoneNo,vendorInstanceId;

    public User(){}

    public User(String id,String fullName,String email,String phoneNo){
        this.id  = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.vendorInstanceId = " ";
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

    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getVendorInstanceId() {
        return vendorInstanceId;
    }

    public void setVendorInstanceId(String instanceId) {
        this.vendorInstanceId = vendorInstanceId;
    }
}