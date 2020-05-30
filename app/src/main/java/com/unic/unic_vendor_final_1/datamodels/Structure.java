package com.unic.unic_vendor_final_1.datamodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void updateProductList(int pageId, int viewCode, List<Map<String,Object>> data){
        for (int i=0;i<pages.size();i++){
            if (pages.get(i).getPageId() == pageId){
                pages.get(i).updateView(viewCode,data);
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

    public void deleteView(int pageId, int viewCode){
        for(int i=0;i<pages.size();i++){
            if(pages.get(i).getPageId()==pageId)
                pages.get(i).deleteView(viewCode);
        }
    }
}
