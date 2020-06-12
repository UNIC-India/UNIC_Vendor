package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String category, company, name, id, imageId, tags, shopId, subcategory,firestoreId,desc;
    private double price;
    private List<String> nameKeywords;

    public Product(String  shopId) {

        this.category = "null";
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
}
