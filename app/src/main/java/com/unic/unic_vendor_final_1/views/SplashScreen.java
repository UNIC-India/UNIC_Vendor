package com.unic.unic_vendor_final_1.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySplashScreenBinding;

import java.io.File;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ActivitySplashScreenBinding splashScreenBinding;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(splashScreenBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        file = new File(getCacheDir() + "splashimage.jpg");
        if (file.exists()){
            Glide
                    .with(this)
                    .load(file)
                    .into(splashScreenBinding.icon);
        }

        else{
            FirebaseStorage.getInstance().getReference().child("/splashimage.jpg").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    if(taskSnapshot!=null) {

                        Glide
                                .with(SplashScreen.this)
                                .load(file)
                                .into(splashScreenBinding.icon);
                    }
                }
            });
        }

        final boolean isUserOnline = mAuth.getCurrentUser()!=null&&!mAuth.getCurrentUser().isAnonymous();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(isUserOnline)
                    intent = new Intent(SplashScreen.this,UserHome.class);
                else
                    intent = new Intent(SplashScreen.this,Welcome.class);
                startActivity(intent);
                finish();
            }
        },2500);
    }
}
