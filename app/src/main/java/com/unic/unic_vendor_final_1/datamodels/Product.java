package com.unic.unic_vendor_final_1.datamodels;

public class Product {
    private String category, company, name, id, imageid, tags, shopid, subcategory;
    private double price;

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getShopid() {
        return shopid;
    }

    public String getId() {
        return id;
    }

    public String getImageid() {
        return imageid;
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

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
