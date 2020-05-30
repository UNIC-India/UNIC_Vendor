package com.unic.unic_vendor_final_1.datamodels;

public class Product {
    private String category, company, name, id, imageId, tags, shopId, subcategory,firestoreId;
    private double price;

    public Product() {
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
}
