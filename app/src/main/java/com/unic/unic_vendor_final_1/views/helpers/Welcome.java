package com.unic.unic_vendor_final_1.views.helpers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.commons.Helpers;
import com.unic.unic_vendor_final_1.databinding.ActivityWelcomeBinding;
import com.unic.unic_vendor_final_1.views.activities.Login;
import com.unic.unic_vendor_final_1.views.activities.SignUp;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    private boolean doubleBackToExitPressedOnce = false;
    private ActivityWelcomeBinding welcomeBinding;

    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener = state -> {
        if(state.installStatus() == InstallStatus.DOWNLOADED) {
            createAppUpdateReadySnackbar();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        welcomeBinding = ActivityWelcomeBinding.inflate(getLayoutInflater());

        setContentView(welcomeBinding.getRoot());

        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.registerListener(installStateUpdatedListener);

        Button btnSignUp = welcomeBinding.btnSignUp;
        btnSignUp.setOnClickListener(this);

        Button btnLogin = welcomeBinding.btnLogin;
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSignUp:
                startActivity(new Intent(Welcome.this, SignUp.class));
                break;
            case R.id.btnLogin:
                startActivity(new Intent(Welcome.this, Login.class));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void createAppUpdateReadySnackbar() {
        Snackbar.make(welcomeBinding.getRoot(),"New update downloaded",Snackbar.LENGTH_INDEFINITE)
                .setAction("INSTALL",v -> {
                    if(appUpdateManager!=null)
                        appUpdateManager.completeUpdate();
                })
                .setActionTextColor(getResources().getColor(R.color.green))
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(appUpdateManager!=null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(appUpdateManager!=null)
            appUpdateManager.registerListener(installStateUpdatedListener);
    }
}
