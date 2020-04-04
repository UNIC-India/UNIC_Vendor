package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private String pageName;
    private int pageId,pos,size;
    private ArrayList<View> views;

    public Page(String pageName){
        views = new ArrayList<>();
        this.pageName = pageName;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
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
        view.setyPos(size);
        view.setPos(views.size());
        views.add(view);
        updateHeight();
    }

    private void updateHeight(){
        int height = 0;
        for(int i=0;i<views.size();i++){
            height+=views.get(i).getHeight();
        }
        this.setSize(height);
    }

    public void updateView(String viewId, List<String> products){
        for(int i=0;i<views.size();i++){
            if(views.get(i).getViewId().equals(viewId)){
                views.get(i).setProducts(products);
            }
        }
    }
}
