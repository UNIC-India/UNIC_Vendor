package com.unic.unic_vendor_final_1.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySplashScreenBinding;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;
import com.unic.unic_vendor_final_1.views.activities.UserHome;
import com.unic.unic_vendor_final_1.views.activities.Welcome;

import java.io.File;
import java.io.IOException;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding splashScreenBinding;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(splashScreenBinding.getRoot());

        final FirestoreDataViewModel vm = ViewModelProviders.of(this).get(FirestoreDataViewModel.class);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/splashimage.jpg");



        final boolean isUserOnline = mAuth.getCurrentUser()!=null&&!mAuth.getCurrentUser().isAnonymous();

        if(isUserOnline){

            try{
                splashScreenBinding.icon.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            vm.getUserSplashStatus(mAuth.getUid()).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                //    if(integer==null)
                //        return;
                    if(!file.exists()||integer==1){
                        boolean bool = file.delete();
                        //deleteFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/splashimage.jpg");
                        FirebaseStorage.getInstance().getReference().child("splashimage.jpg").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(SplashScreen.this, "New image received", Toast.LENGTH_SHORT).show();
                                try{
                                    splashScreenBinding.icon.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        vm.setUserSplashStatus(mAuth.getUid(),0,false);
                    }
                    else if(integer==0){
                        try{
                            splashScreenBinding.icon.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        splashScreenBinding.icon.setImageResource(R.drawable.logonotext);
                }
            });
        }
        else {
            splashScreenBinding.icon.setImageResource(R.drawable.logonotext);

        }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(isUserOnline)
                    intent = new Intent(SplashScreen.this, UserHome.class);
                else
                    intent = new Intent(SplashScreen.this, Welcome.class);
                startActivity(intent);
                finish();
            }
        },2500);
    }
}
