package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Page {
    private String pageName;
    private int pageId,pos,size;
    private ArrayList<View> views;

    public Page(){}

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

    public void addView(View view,int code){
        view.setyPos(size);
        view.setPos(views.size());
        List<Map<String,Object>> data = new ArrayList<>();
        view.setData(data);
        view.setViewCode(code*100+views.size()+1);
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

    public View getView(int viewCode){
        for(View view : views)
            if(view.getViewCode()==viewCode)
                return view;
        return null;
    }

    public void updateView(int viewCode, List<Map<String,Object>> data){
        for(int i=0;i<views.size();i++){
            if(views.get(i).getViewCode()==viewCode){
                views.get(i).setData(data);
            }
        }
    }
}
