package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;

public class Page {
    private int pageId;
    private int pos,size;
    private ArrayList<View> views;

    public Page(){
        this.pageId = 0;
        views = new ArrayList<>();
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<View> getViews() {
        return views;
    }

    public void setViews(ArrayList<View> views) {
        this.views = views;
    }

    public void addView(View view){
        this.views.add(view);

    }

    private void updateHeight(){
        int height = 0;
        for(int i=0;i<views.size();i++){
            height+=views.get(i).getHeight();
        }
        this.setSize(height);
    }
}
