package com.unic.unic_vendor_final_1.datamodels;

import java.util.Map;

public class Shop {


    private String id,ownerId,name,address,locality,city,imageLink,logoLink;
    private int noOfProducts,noOfSubscribers;
    private Map<String,Double> location;

    public Shop(){}

    public Shop(String name,String address,String locality,String city,Map<String,Double> location){
        this.id = " ";
        this.name = name;
        this.address = address;
        this.locality = locality;
        this.city = city;
        this.imageLink = " ";
        this.logoLink = " ";
        this.noOfProducts = 0;
        this.noOfSubscribers = 0;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public int getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(int noOfProducts) {
        this.noOfProducts = noOfProducts;
    }

    public int getNoOfSubscribers() {
        return noOfSubscribers;
    }

    public void setNoOfSubscribers(int noOfSubscribers) {
        this.noOfSubscribers = noOfSubscribers;
    }

    public Map<String, Double> getLocation() {
        return location;
    }

    public void setLocation(Map<String, Double> location) {
        this.location = location;
    }
}
