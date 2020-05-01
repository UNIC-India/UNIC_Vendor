package com.unic.unic_vendor_final_1.commons;

import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.datamodels.View;

public class StructureTemplates {

    public static Structure getTemplate1(String shopId){
        Structure structure = new Structure(shopId);
        View view1 = new View();

        view1.setHeader("Master");
        view1.setFields("name,imageLink,price");
        structure.getPage(1001).addView(view1,00);

        return structure;
    }

}