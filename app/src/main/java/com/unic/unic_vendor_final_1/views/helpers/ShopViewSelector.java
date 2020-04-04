package com.unic.unic_vendor_final_1.views.helpers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ViewSelectorAdapter;
import com.unic.unic_vendor_final_1.databinding.ActivityShopViewSelectorBinding;

public class ShopViewSelector extends AppCompatActivity implements View.OnClickListener {

    ActivityShopViewSelectorBinding shopViewSelectorBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopViewSelectorBinding = ActivityShopViewSelectorBinding.inflate(getLayoutInflater());
        setContentView(shopViewSelectorBinding.getRoot());
        ViewSelectorAdapter adapter = new ViewSelectorAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        shopViewSelectorBinding.viewOptionsSelector.setLayoutManager(layoutManager);
        shopViewSelectorBinding.viewOptionsSelector.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
