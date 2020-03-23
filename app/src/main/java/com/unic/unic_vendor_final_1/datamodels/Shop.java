package com.unic.unic_vendor_final_1.datamodels;

public class Shop {


    private String id,ownerId,name,address,locality,imageLink,logoLink;
    private int noOfProducts,noOfSubscribers;

    public Shop(){}

    public Shop(String name,String address,String locality){
        this.id = " ";
        this.name = name;
        this.address = address;
        this.locality = locality;
        this.imageLink = " ";
        this.logoLink = " ";
        this.noOfProducts = 0;
        this.noOfSubscribers = 0;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getAddress() {
        return address;
    }

    public String getLocality() {
        return locality;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public int getNoOfProducts() {
        return noOfProducts;
    }

    public int getNoOfSubscribers() {
        return noOfSubscribers;
    }
}
