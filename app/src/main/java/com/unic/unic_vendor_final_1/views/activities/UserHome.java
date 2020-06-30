package com.unic.unic_vendor_final_1.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivityUserHomeBinding;
import com.unic.unic_vendor_final_1.datamodels.Notification;
import com.unic.unic_vendor_final_1.datamodels.User;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.helpers.OrderItems;
import com.unic.unic_vendor_final_1.views.nav_fragments.AboutUsFragment;
import com.unic.unic_vendor_final_1.views.nav_fragments.ComingSoon;
import com.unic.unic_vendor_final_1.views.nav_fragments.HomeFragment;
import com.unic.unic_vendor_final_1.views.nav_fragments.MyAppsFragment;
import com.unic.unic_vendor_final_1.views.nav_fragments.OrdersFragment;
import com.unic.unic_vendor_final_1.views.helpers.IntermidiateShopList;
import com.unic.unic_vendor_final_1.views.nav_fragments.QRFragment;
import com.unic.unic_vendor_final_1.views.nav_fragments.SettingsFragment;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;

import java.util.Objects;

public class UserHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    boolean doubleBackToExitPressedOnce = false;
    public User user;
    private UserShopsViewModel userShopsViewModel;
    private com.unic.unic_vendor_final_1.databinding.ActivityUserHomeBinding userHomeBinding;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userHomeBinding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(userHomeBinding.getRoot());

        prefs = getSharedPreferences("com.unic.unic_vendor_final_1", MODE_PRIVATE);

        FirestoreDataViewModel firestoreDataViewModel = new ViewModelProvider(this).get(FirestoreDataViewModel.class);
        firestoreDataViewModel.getUserData();
        firestoreDataViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setUser(user);
            }
        });

        userShopsViewModel = new ViewModelProvider(this).get(UserShopsViewModel.class);
        userShopsViewModel.titleSetter.setValue(0);

        userShopsViewModel.getAllShops();
        userShopsViewModel.titleSetter.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setTitle(integer);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        DrawerLayout drawer = userHomeBinding.drawerLayout;
        navigationView = userHomeBinding.navView;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        if (getIntent().hasExtra("load")){
            if(getIntent().getStringExtra("load").equals("order")){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.home_fragment,new OrdersFragment());
                ft.commit();
            }
        }
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.home_fragment,new HomeFragment());
            ft.commit();
        }
    }

    public void setTitle(int titleSetter){
        switch(titleSetter){
            case 0:
                userHomeBinding.titleBar.ivLogo.setVisibility(View.VISIBLE);
                userHomeBinding.titleBar.tvTitle.setVisibility(View.GONE);
                break;
            case 1:
                userHomeBinding.titleBar.ivLogo.setVisibility(View.GONE);
                userHomeBinding.titleBar.tvTitle.setVisibility(View.VISIBLE);
                userHomeBinding.titleBar.tvTitle.setText("My Orders");
                break;
            case 2:
                userHomeBinding.titleBar.ivLogo.setVisibility(View.GONE);
                userHomeBinding.titleBar.tvTitle.setVisibility(View.VISIBLE);
                userHomeBinding.titleBar.tvTitle.setText("My Shops");
                break;
            case 3:
                userHomeBinding.titleBar.ivLogo.setVisibility(View.GONE);
                userHomeBinding.titleBar.tvTitle.setVisibility(View.VISIBLE);
                userHomeBinding.titleBar.tvTitle.setText("Settings");
                break;
            case 4:
                userHomeBinding.titleBar.ivLogo.setVisibility(View.GONE);
                userHomeBinding.titleBar.tvTitle.setVisibility(View.VISIBLE);
                userHomeBinding.titleBar.tvTitle.setText("My QR");
                break;
            case 5:
                userHomeBinding.titleBar.ivLogo.setVisibility(View.GONE);
                userHomeBinding.titleBar.tvTitle.setVisibility(View.VISIBLE);
                userHomeBinding.titleBar.tvTitle.setText("Notifications");
                break;
            case 6:
                userHomeBinding.titleBar.ivLogo.setVisibility(View.GONE);
                userHomeBinding.titleBar.tvTitle.setVisibility(View.VISIBLE);
                userHomeBinding.titleBar.tvTitle.setText("My Products");
                break;
            case 7:
                userHomeBinding.titleBar.ivLogo.setVisibility(View.GONE);
                userHomeBinding.titleBar.tvTitle.setVisibility(View.VISIBLE);
                userHomeBinding.titleBar.tvTitle.setText("My Report");
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateHeader();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int id = menuItem.getItemId();
        Fragment homeFragment = new HomeFragment();
        Fragment appsFragment = new MyAppsFragment();
        Fragment ordersFragment = new OrdersFragment();
        Fragment qrFragment = new QRFragment();
        Fragment productsFragment=new IntermidiateShopList();
        switch (id){
            case R.id.nav_home:
                fragment = homeFragment;
                userShopsViewModel.titleSetter.setValue(0);
                break;
            case R.id.nav_my_apps:
                fragment = appsFragment;
                break;
            case R.id.mav_my_orders:
                fragment = ordersFragment;
                break;
            case R.id.nav_help:
               fragment=new ComingSoon();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_my_qr:
                fragment =qrFragment;
                break;
            case R.id.nav_about_us:
                fragment= new AboutUsFragment();
                break;
            case R.id.logout:
                new AlertDialog.Builder(this).setMessage("Are you sure you want to Sign Out?")
                        .setPositiveButton("Yes",((dialog, which) -> {
                            prefs.edit().putString("phone",user.getPhoneNo()).apply();
                            mAuth.signOut();
                            startActivity(new Intent(UserHome.this,Login.class));
                            finish();}))
                        .setNegativeButton("No",((dialog, which) ->{
                            dialog.dismiss();
                            userHomeBinding.drawerLayout.closeDrawers();
                        } ))
                        .create()
                        .show();

                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        if(fragment!=null){
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawers();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.home_fragment,fragment);
            ft.commit();

        }
        return id != R.id.logout;
    }

    public void setUser(User user) {
        this.user = user;
        populateHeader();
    }

    private void populateHeader(){

        View header =navigationView.getHeaderView(0);

        TextView tvFullName = header.findViewById(R.id.user_FullName);
        TextView tvEmail = header.findViewById(R.id.user_Email);

        tvFullName.setText(user!=null?user.getFullName():"Not received yet");
        tvEmail.setText(user!=null?user.getEmail():"Not received yet");

    }

    public void deleteShop(String shopId){
        userShopsViewModel.isMyAppsLoading.setValue(true);
        userShopsViewModel.deleteShop(shopId);
    }
    public void deleteMember(String phone,String role, String shopId){
        userShopsViewModel.deleteMember(phone, role, shopId);
    }

    @Override
    public void onBackPressed() {
        Fragment fg=getSupportFragmentManager().findFragmentById(R.id.home_fragment);
        if(fg.getClass()==HomeFragment.class){
            if(doubleBackToExitPressedOnce){
                moveTaskToBack(true);
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
        else if(fg.getClass()== OrderItems.class||fg.getClass()== ProductDescriptionFragment.class){
            getSupportFragmentManager().popBackStack();
        }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_fragment,new HomeFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            userHomeBinding.navView.setCheckedItem(R.id.nav_home);
            userShopsViewModel.titleSetter.setValue(0);

        }
       /* */

    }
    public void sendNotification(String title, String shopId, String message){
        userShopsViewModel.sendNotification(new Notification(title,shopId,message));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
