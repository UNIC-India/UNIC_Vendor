package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*

    View code designation:

    _ _ _ _ (4 digits)

    _       -> 1: For image only views, click to open full image
               2: For category views
               3: For sliders
               4: For product basic details, like name,price and image, add to cart button present

    1 _     -> 1: For single image display with tag
               2: For double image display with tag
               3: For triple image display with tag

    2 _     -> 1: For vertical categories list view with images
               2: For horizontal categories scroll view with images
               3: For vertical grid view with images
               4: For text only vertical list

    3 _     -> 1: Single image slider without position indicator
               2: Single image slider with position indicator

    4 _     -> 1: For double item horizontal scroller
               2: For triple item horizontal scroller
               3: For double item grid view horizontal scroller

    5_      ->1: TextView

        _ _ -> Last two digits are meant to show order of views in structure

*/

public class View {
    private String  fields,header;
    private int viewCode,pos,height,yPos;
    private List<Map<String,Object>> data;

    public View(){
        data = new ArrayList<>();
    }

    public int getViewCode() {
        return viewCode;
    }

    public void setViewCode(int viewCode) {
        this.viewCode = viewCode;
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

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

}
