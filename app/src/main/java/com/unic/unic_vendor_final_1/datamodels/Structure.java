package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Structure {

    private String shopId;
    private ArrayList<Page> pages;

    public Structure(){

    }

    public Structure(String shopId){
        this.shopId = shopId;
        pages = new ArrayList<>();
        Page page = new Page("Home");
        page.setPageId(1001);
        pages.add(page);
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

    public  void addPage(String pageName){
        Page page = new Page(pageName);
        page.setPos(pages.size());
        page.setPageId(1001+pages.size());
        pages.add(page);
    }

    public void addView(int pageId,View view){
        for(int i=0;i<pages.size();i++){
            if (pages.get(i).getPageId() == pageId){
                pages.get(i).addView(view);
            }
        }
    }

    public void updateProductList(int pageId, String viewId, List<String> products){
        for (int i=0;i<pages.size();i++){
            if (pages.get(i).getPageId() == pageId){
                pages.get(i).updateView(viewId, products);
            }
        }
    }

    public Page getPage(int pageId){
        for(int i=0;i<pages.size();i++){
            if(pages.get(i).getPageId()==pageId)
                return pages.get(i);
        }
        return null;
    }
}
