package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.HashMap;

public class Structure {

    private String shopId;
    private ArrayList<Page> pages;

    public Structure(String shopId){
        this.shopId = shopId;
        this.pages = new ArrayList<>();
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public String getShopId() {
        return shopId;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void addView(View view){
        this.pages.get(0).addView(view);
    }
}
