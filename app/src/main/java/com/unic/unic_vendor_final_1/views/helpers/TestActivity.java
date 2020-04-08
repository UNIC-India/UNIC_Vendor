package com.unic.unic_vendor_final_1.views.helpers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.unic.unic_vendor_final_1.R;

public class TestActivity extends AppCompatActivity {

    private static final String address = "Vikram Nagar, Ahmedabad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = new Intent(this,LocationSelector.class);
        intent.putExtra("address",address);
        startActivity(intent);


    }
}
