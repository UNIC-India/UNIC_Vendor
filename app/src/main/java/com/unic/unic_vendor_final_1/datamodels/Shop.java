package com.unic.unic_vendor_final_1.datamodels;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shop {


    private String id;
    private String ownerId,name,address,locality,city,imageLink,logoLink,dynSubscribeLink;
    private int noOfProducts,noOfSubscribers;
    private List<String> nameKeywords;
    private Map<String,Double> location;
    private GeoPoint geoLocation;
    private boolean isPrivate;

    public Shop(){
        this.id = "null";
        this.imageLink = "null";
        this.logoLink = "null";
        this.dynSubscribeLink = "null";
        this.isPrivate = false;
    }

    public Shop(String name,String address,String locality,String city,Map<String,Double> location,GeoPoint geoLocation){
        this.id = " ";
        this.name = name;
        this.address = address;
        this.locality = locality;
        this.city = city;
        this.imageLink = " ";
        this.logoLink = " ";
        this.dynSubscribeLink = " ";
        this.noOfProducts = 0;
        this.noOfSubscribers = 0;
        this.location = location;
        this.geoLocation = geoLocation;
        this.nameKeywords = new ArrayList<>();
        this.isPrivate = false;
        for(String key : name.split(" "))
            if(key.length()>1)
                nameKeywords.add(key.substring(0,2).toLowerCase());
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

        List<String> keywords = new ArrayList<>();

        String [] keys = name.split("[ -()]");

        for(int i=0;i< keys.length;i++) {
            if(keys[i].length()>=2) {
                keywords.add(keys[i].substring(0,2).toLowerCase());
            }

            else if(keys[i].length()==1){
                keywords.add(String.format("%-2s", keys[i]));
                if(i<keys.length-1) {
                    keywords.add(keys[i]+keys[i+1].substring(0,1));
                }
            }
        }

        this.nameKeywords = keywords;

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

    public String getDynSubscribeLink() {
        return dynSubscribeLink;
    }

    public void setDynSubscribeLink(String dynSubscribeLink) {
        this.dynSubscribeLink = dynSubscribeLink;
    }

    public List<String> getNameKeywords() {
        return nameKeywords;
    }

    public void setNameKeywords(List<String> nameKeywords) {
        this.nameKeywords = nameKeywords;
    }

    public Map<String, Double> getLocation() {
        return location;
    }

    public void setLocation(Map<String, Double> location) {
        this.location = location;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
