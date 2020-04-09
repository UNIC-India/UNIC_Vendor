package com.unic.unic_vendor_final_1.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.unic.unic_vendor_final_1.R;

public class OrdersLol extends AppCompatActivity implements View.OnClickListener {
    int status;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_lol);
        status = 0;
        imageView = findViewById(R.id.order);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (status){
            case 0:
                imageView.setImageResource(R.drawable.myorderwithclick);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(R.drawable.pickup);
                    }
                },100);
                status = 1;
                break;
            case 1:
                imageView.setImageResource(R.drawable.pickupwithtick);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(R.drawable.confirmationtwo);
                    }
                },100);
        }
    }
}
