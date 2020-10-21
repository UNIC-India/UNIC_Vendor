package com.unic.unic_vendor_final_1.views.helpers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySplashScreenBinding;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class SplashScreen extends AppCompatActivity implements LocationListener {

    ActivitySplashScreenBinding splashScreenBinding;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;
    private boolean isUserOnline;
    private boolean isUpdateQueued = false;

    private static final int UPDATE_REQUEST = 1001;

    AppUpdateManager appUpdateManager;

    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED){
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        if (appUpdateManager != null){
                            appUpdateManager.completeUpdate();
                        }                    } else if (state.installStatus() == InstallStatus.INSTALLED){
                        if (appUpdateManager != null){
                            appUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else if (state.installStatus() == InstallStatus.DOWNLOADING) {
                        if(!isUpdateQueued) {
                            getLocation();
                            isUpdateQueued = true;
                        }
                    }
                    else {
                        Timber.i("InstallStateUpdatedListener: state: %s", state.installStatus());
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(splashScreenBinding.getRoot());

        final FirestoreDataViewModel vm = new ViewModelProvider(this).get(FirestoreDataViewModel.class);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        isUserOnline = mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().isAnonymous();

        Handler handler = new Handler();
        handler.postDelayed(() -> {


            appUpdateManager = AppUpdateManagerFactory.create(this);
            appUpdateManager.registerListener(installStateUpdatedListener);
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE   ) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.IMMEDIATE, SplashScreen.this, UPDATE_REQUEST);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    /*if(appUpdateInfo.updatePriority() == 5 || (appUpdateInfo.updatePriority() > 2 && appUpdateInfo.updatePriority() < 5 && appUpdateInfo.clientVersionStalenessDays()!=null && appUpdateInfo.clientVersionStalenessDays() >= 7) || (appUpdateInfo.updatePriority() < 3 && appUpdateInfo.clientVersionStalenessDays()!=null && appUpdateInfo.clientVersionStalenessDays() >= 14)) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, AppUpdateType.IMMEDIATE, SplashScreen.this, UPDATE_REQUEST);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }

                    else if(appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                    }

                    else {
                        try {
                            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE , this, UPDATE_REQUEST);

                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }*/

                }
                else {
                    getLocation();
                }
            })
            .addOnFailureListener(e -> {
                e.printStackTrace();
                getLocation();
            });

        }, 1500);
    }

    public void getLocation() {
        if(checkLocationPermission()){

            final Looper looper = null;

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(true);
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            locationEnabled();

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            locationManager.requestSingleUpdate(criteria, this, looper);
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Give location access")
                        .setMessage("This app needs location access to display shops near you")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(SplashScreen.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void locationEnabled () {
        LocationManager lm = (LocationManager)
                getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (!gps_enabled && !network_enabled) {
            startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                final Looper looper = null;

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);
                criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                locationEnabled();

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                locationManager.requestSingleUpdate(criteria, this, looper);

            } else {

                finish();

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Intent intent;
        if (isUserOnline)
            intent = new Intent(SplashScreen.this, UserHome.class);
        else
            intent = new Intent(SplashScreen.this, Welcome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar = Snackbar.make(splashScreenBinding.getRoot(),"New update downloaded",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("INSTALL",v -> {
            if(appUpdateManager!=null)
                appUpdateManager.completeUpdate();
        });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    getLocation();
                }
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.green));

        snackbar.show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UPDATE_REQUEST) {
            if(resultCode!=RESULT_OK) {
                getLocation();
            }
        }
    }
}
