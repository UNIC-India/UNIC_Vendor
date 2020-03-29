package com.unic.unic_vendor_final_1.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivityUserHomeBinding;
import com.unic.unic_vendor_final_1.databinding.AppBarHomeBinding;
import com.unic.unic_vendor_final_1.datamodels.User;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;
import com.unic.unic_vendor_final_1.views.nav_fragments.HomeFragment;
import com.unic.unic_vendor_final_1.views.nav_fragments.MyAppsFragment;

public class UserHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    boolean doubleBackToExitPressedOnce = false;
    private User user;

    private ActivityUserHomeBinding userHomeBinding;

    private FirestoreDataViewModel firestoreDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userHomeBinding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(userHomeBinding.getRoot());

        firestoreDataViewModel = ViewModelProviders.of(this).get(FirestoreDataViewModel.class);
        firestoreDataViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setUser(user);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        firestoreDataViewModel.getUserData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int id = menuItem.getItemId();
        Fragment homeFragment = new HomeFragment();
        Fragment appsFragment = new MyAppsFragment();
        switch (id){
            case R.id.nav_home:
                fragment = homeFragment;
                break;
            case R.id.nav_my_apps:
                fragment = appsFragment;
                break;
            case R.id.nav_help:
                break;

            case R.id.nav_settings:
                fragment = homeFragment;
                break;
            case R.id.nav_my_qr:
                break;
            case R.id.logout:
                String Phone = mAuth.getCurrentUser().getPhoneNumber()!=null?mAuth.getCurrentUser().getPhoneNumber():" ";
                mAuth.signOut();
                Intent intent = new Intent(UserHome.this,Login.class);
                intent.putExtra("Phone",Phone);
                startActivity(intent);
                finish();
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
        return true;
    }

    public void setUser(User user) {
        this.user = user;
        populateHeader();
    }

    private void populateHeader(){

        ((TextView)findViewById(R.id.user_FullName)).setText(user.getFullName()==null?"Not received yet":user.getFullName());
        ((TextView)findViewById(R.id.user_Email)).setText(user.getEmail()==null?"Not received yet":user.getEmail());

    }
}
