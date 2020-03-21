package com.unic.unic_vendor_final_1.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.unic.unic_vendor_final_1.R;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        final boolean isUserOnline = mAuth.getCurrentUser()!=null&&!mAuth.getCurrentUser().isAnonymous();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(isUserOnline)
                    intent = new Intent(SplashScreen.this,Welcome.class);
                else
                    intent = new Intent(SplashScreen.this,Welcome.class);
                startActivity(intent);
                finish();
            }
        },2500);
    }
}
