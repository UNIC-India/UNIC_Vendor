package com.unic.unic_vendor_final_1.views.helpers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySplashScreenBinding;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import java.io.File;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "Splash Screen";
    ActivitySplashScreenBinding splashScreenBinding;

    private File file;

    private static final int UPDATE_REQUEST = 1001;

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


            AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                    builder.setTitle("App update required")
                            .setMessage(getResources().getString(R.string.app_name)+ " has to update to the latest version in order for it to function properly")
                            .setPositiveButton("OKAY", (dialog, which) -> {
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                            appUpdateInfo,
                                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                            AppUpdateType.IMMEDIATE,
                                            // The current activity making the update request.
                                            SplashScreen.this,
                                            // Include a request code to later monitor this update request.
                                            UPDATE_REQUEST);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }

                                dialog.dismiss();
                            })
                            .setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                }
                else {
                    Intent intent;
                    if (isUserOnline)
                        intent = new Intent(SplashScreen.this, UserHome.class);
                    else
                        intent = new Intent(SplashScreen.this, Welcome.class);
                    startActivity(intent);
                    finish();
                }
            });




        }, 1500);
    }
}
