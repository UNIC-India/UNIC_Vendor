package com.unic.unic_vendor_final_1.datamodels;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Order {
    private String ownerId, id, shopId,status;
    private List<Map<String,Object>> items;
    private List<Integer> quantity;
    public int no_of_items;

    private int orderStatus;
    @ServerTimestamp
    Date time;
    private String createBy;
    private String phoneNo;

    public Order() {
        //No argument constructor for firestore
    }

    public Order(String shopId, List<Map<String, Object>> items, List<Integer> quantity, int no_of_items,String createdBy) {
        this.shopId = shopId;
        this.items = items;
        this.quantity = quantity;
        this.no_of_items = no_of_items;
        this.id=" ";
        this.status=" ";
        this.orderStatus=0;
        this.createBy=createdBy;
    }

    public static Comparator<Order> compareByDate=new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            return o2.getTime().compareTo(o1.getTime());
        }
    };

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.id==((Order) obj).getId();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public List<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
    }

    public int getNo_of_items() {
        return no_of_items;
    }

    public void setNo_of_items(int no_of_items) {
        this.no_of_items = no_of_items;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
