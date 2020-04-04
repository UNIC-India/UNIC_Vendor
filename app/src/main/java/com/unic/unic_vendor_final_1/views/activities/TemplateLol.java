package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Shop;

public class TemplateLol extends AppCompatActivity implements View.OnClickListener {

    int status;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_lol);
        imageView = findViewById(R.id.template);
        status = 0;
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (status){
            case 0:
                imageView.setImageResource(R.drawable.templateafterclick);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(R.drawable.template);
                        status = 1;
                        startActivity(new Intent(TemplateLol.this, ShopLol.class));
                    }
                },100);
                break;
            case 1:
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String shopId = getIntent().getStringExtra("shopId");
                        Intent intent = new Intent(TemplateLol.this,SetShopStructure.class);
                        intent.putExtra("shopId",shopId);
                        startActivity(intent);
                    }
                },100);
                break;

        }
    }
}
