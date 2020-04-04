package com.unic.unic_vendor_final_1.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.SelectViewAdapter;
import com.unic.unic_vendor_final_1.adapters.ShopAdapter;

import java.util.ArrayList;

public class SelectView extends AppCompatActivity {
    private SelectViewAdapter adapter;
    public ArrayList<String> arrayList1;
    int checkedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_view);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        RecyclerView recyclerView =findViewById(R.id.rv_view_categories);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SelectViewAdapter(this);
        arrayList1=new ArrayList<>();
        arrayList1.add("Slider");
        arrayList1.add("Fixed Image With Label");
        arrayList1.add("Double images scroller");
        arrayList1.add("Triple images scroller");
        arrayList1.add("Categories list");
        adapter.setViewCategories(arrayList1);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }
}
