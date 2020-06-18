package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Page {
    private String pageName;
    private int pageId,pos,size;
    private List<View> views;

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

    public List<View> getViews() {
        return views;
    }

    public void setViews(ArrayList<View> views) {
        this.views = views;
    }

    public void addNewView(View view, int code){

        setSize(0);

        List<View> newViews = new ArrayList<>(views);

        views.clear();

        addView(view,code);

        for(View v : newViews){
            addView(v,v.getViewCode()/100);
        }

    }

    public void addView(View view,int code){
        view.setyPos(size);
        view.setPos(views.size());
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

    public void deleteView(int viewCode){
        for(int i=0;i<views.size();i++){
            if(views.get(i).getViewCode()==viewCode){
                views.remove(i);
                break;
            }
        }
    }
}
