package com.unic.unic_vendor_final_1.views.helpers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySplashScreenBinding;
import com.unic.unic_vendor_final_1.messaging_service.MyFirebaseMessagingService;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "Splash Screen";
    ActivitySplashScreenBinding splashScreenBinding;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(splashScreenBinding.getRoot());

        final FirestoreDataViewModel vm = new ViewModelProvider(this).get(FirestoreDataViewModel.class);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/splashimage.jpg");

        final boolean isUserOnline = mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().isAnonymous();



        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if (isUserOnline)
                intent = new Intent(SplashScreen.this, UserHome.class);
            else
                intent = new Intent(SplashScreen.this, Welcome.class);
            startActivity(intent);
            finish();
        }, 1500);
    }
}
