package com.unic.unic_vendor_final_1.commons;

import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.datamodels.View;

public class StructureTemplates {

    public static Structure getTemplate1(String shopId){
        Structure structure = new Structure(shopId);
        View view1 = new View();
        view1.setHeight(260);
        view1.setHeader("NEWEST");
        view1.setFields("name,imageLink,price");
        structure.getPage(1001).addView(view1,41);

        View view2 = new View();
        view2.setHeight(260);
        view2.setHeader("TRENDING");
        view2.setFields("name,imageId,price");
        structure.getPage(1001).addView(view2,41);
        return structure;
    }

}