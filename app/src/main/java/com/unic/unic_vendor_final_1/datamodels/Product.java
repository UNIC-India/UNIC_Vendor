package com.unic.unic_vendor_final_1.datamodels;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
    private String category, company, name, id, imageId, tags, shopId, subcategory,firestoreId,desc,extraInfo1,extraInfo2;
    private double price,discount;
    private List<String> nameKeywords;
    @ServerTimestamp
    Date time;

    public Product(String  shopId) {

        this.category = "null";
        this.extraInfo1="null";
        this.extraInfo2="null";
        this.company = "null";
        this.name= "null";
        this.shopId = shopId;
        this.price = 0.0;
        this.id="null";
        this.firestoreId=" ";
        this.imageId="null";
        this.tags="null";
        this.subcategory="null";
        this.desc = "null";
        this.nameKeywords = new ArrayList<>();


    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Product(String category, String company, String name, String shopId, double price) {
        this.category = category;
        this.company = company;
        this.name = name;
        this.shopId = shopId;
        this.price = price;
        this.id=" ";
        this.firestoreId=" ";
        this.imageId=" ";
        this.tags=" ";
        this.subcategory=" ";
        this.desc = " ";
    }

    public String getShopId() {
        return shopId;
    }

    public String getId() {
        return id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public String getCategory() {
        return category;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public String getTags() {
        return tags;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public double getPrice() {
        return price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getNameKeywords() {
        return nameKeywords;
    }

    public void setNameKeywords(List<String> nameKeywords) {
        this.nameKeywords = nameKeywords;
    }

    public String getExtraInfo1() {
        return extraInfo1;
    }

    public void setExtraInfo1(String extraInfo1) {
        this.extraInfo1 = extraInfo1;
    }

    public String getExtraInfo2() {
        return extraInfo2;
    }

    public void setExtraInfo2(String extraInfo2) {
        this.extraInfo2 = extraInfo2;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
