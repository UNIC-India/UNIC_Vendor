package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;

/*

    View code designation:

    _ _ _ _ (4 digits)

    _       -> 1: For image only views, click to open full image
               2: For product basic details, like name,price and image, add to cart button present
               3: For text type views only

    1 _     -> 1: For slider
               2: For single image display with tag

    2 _     -> 2: For double item horizontal scroller
               3: For triple item horizontal scroller

    3 _     -> 1: For showing categories
               2: For simple text views
        _ _ -> Last two digits are meant to show order of views in structure

*/

public class View {
    private String viewId,fields,header;
    private int pos,height,yPos;
    private List<String> products;

    public View(){
        products = new ArrayList<>();
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

}
