package com.unic.unic_vendor_final_1.views.helpers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.ArrayList;
import java.util.List;

public class SelectTemplate extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_template);

        findViewById(R.id.t1).setOnClickListener(this);
        findViewById(R.id.custom).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t1:
                Intent intent = new Intent(this, SetShopStructure.class);
                intent.putExtra("shopId",getIntent().getStringExtra("shopId"));
                intent.putExtra("stat",1);
                startActivity(intent);
            case R.id.custom:
                Intent intent2 = new Intent(this, SetShopStructure.class);
                intent2.putExtra("shopId",getIntent().getStringExtra("shopId"));
                startActivity(intent2);
        }
    }
}
