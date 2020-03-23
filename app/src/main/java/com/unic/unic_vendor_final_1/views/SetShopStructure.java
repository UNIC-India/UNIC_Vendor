package com.unic.unic_vendor_final_1.views;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySetShopStructureBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

public class SetShopStructure extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {

    private Shop shop;
    private ActivitySetShopStructureBinding setStructureBinding;
    private boolean isDataAcquired = false;

    private SetStructureViewModel setStructureViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStructureBinding = ActivitySetShopStructureBinding.inflate(getLayoutInflater());
        setContentView(setStructureBinding.getRoot());

        setStructureViewModel = ViewModelProviders.of(this).get(SetStructureViewModel.class);
        setStructureViewModel.getShop().observe(this, new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {
                updateShop(shop);
            }
        });
        setStructureBinding.addView.setOnClickListener(this);
        setStructureViewModel.getProductDetails(shop.getId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStructureViewModel.getShopData(getIntent().getStringExtra("shopId"));

    }

    private void updateShop(Shop shop){
        this.shop = shop;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_view:
                View popupView = LayoutInflater.from(this).inflate(R.layout.view_selector,null);
                PopupWindow popupWindow =new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(findViewById(R.id.btn_add_shop));
                RadioGroup radioGroup = popupView.findViewById(R.id.rg_selector);
                radioGroup.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        isDataAcquired = false;
        getDisplayData(i);
        if(isDataAcquired){
            addView(i);
        }
    }

    private void addView(int id){

    }

    private void getDisplayData(int id){
        setStructureBinding.viewInflater.setVisibility(View.GONE);
        setStructureBinding.dataTableSelector.setVisibility(View.VISIBLE);
        switch(id){
            case R.id.rad_1:
                //TODO: Add image selector for slider
                break;
            case R.id.rad_2:
                //TODO: Add image & label selector
                break;
            case R.id.rad_3:
                //TODO: Add data for double image scroller
                break;
            case R.id.rad_4:
                //TODO: Add data for triple image scroller
                break;
        }
    }
}
