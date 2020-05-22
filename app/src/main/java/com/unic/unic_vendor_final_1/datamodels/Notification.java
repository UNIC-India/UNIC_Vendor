package com.unic.unic_vendor_final_1.datamodels;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Notification {
    private String title;
    String shopId;
    private String message;
    @ServerTimestamp
    Date time;


    public Notification() {
    }

    public Notification(String title, String shopId, String description) {
        this.title = title;
        this.shopId = shopId;
        this.message = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setMessage(String description) {
        this.message = description;
    }
}
