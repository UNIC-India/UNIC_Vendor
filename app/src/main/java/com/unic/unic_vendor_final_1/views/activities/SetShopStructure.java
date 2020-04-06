package com.unic.unic_vendor_final_1.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySetShopStructureBinding;
import com.unic.unic_vendor_final_1.databinding.ActivitySplashScreenBinding;
import com.unic.unic_vendor_final_1.datamodels.Page;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.helper_classes.StructureTemplates;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.helpers.ShopPageFragment;

public class SetShopStructure extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Structure structure;
    private Shop shop;

    private int status,productStatus,structureStatus;

    private String shopId;
    private int option;

    private SetStructureViewModel setStructureViewModel;
    private ActivitySetShopStructureBinding setShopStructureBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStructureViewModel = ViewModelProviders.of(this).get(SetStructureViewModel.class);

        setShopStructureBinding = ActivitySetShopStructureBinding.inflate(getLayoutInflater());
        setContentView(setShopStructureBinding.getRoot());

        shopId = getIntent().getStringExtra("shopId");
        assert shopId != null;

        setStructureViewModel.getShop().observe(this, new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {
                setShop(shop);
            }
        });

        setStructureViewModel.getStructure().observe(this, new Observer<Structure>() {
            @Override
            public void onChanged(Structure structure) {
                setStructure(structure);
            }
        });

        setStructureViewModel.getStructureStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setStructureStatus(integer);
            }
        });

        setStructureViewModel.getStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setStatus(integer);
            }
        });

        setStructureViewModel.getProductStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setProductStatus(integer);
            }
        });

        Toolbar toolbar = setShopStructureBinding.setStructureToolbar;
        setSupportActionBar(toolbar);

        DrawerLayout drawer = setShopStructureBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setShopStructureBinding.setStructureNavView.setNavigationItemSelectedListener(this);
        setShopStructureBinding.shopAddPage.setOnClickListener(this);
        setShopStructureBinding.confirmShopStructure.setOnClickListener(this);

        option = getIntent().getIntExtra("template",0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        status = 0;
        updateStatus(status);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.shop_add_page:
                addPage();
                break;
            case R.id.confirm_shop_structure:
                setStructureViewModel.saveShopStructure();
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        for(Page page : structure.getPages()){
            if(page.getPageId() == item.getItemId()){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.shop_pages_loader,new ShopPageFragment(page),page.getPageName())
                        .commit();
                setShopStructureBinding.setStructureNavView.setCheckedItem(item);
                return true;
            }
        }

        return false;
    }

    void setTemplate(int template){
        switch (template){
            case 0:
                status = 2;
                updateStatus(status);
                break;
            case 1:
                structure = StructureTemplates.getTemplate1(shopId);
                setStructureViewModel.getStructure().setValue(structure);
                status = 3;
                updateStatus(status);
                break;
        }
    }

    void updateStatus(int code){
        switch (code)
        {
            case 0:
                setStructureViewModel.getShopData(shopId);
                break;
            case 1:
                setTemplate(option);
                break;
            case 2:
                setStructureViewModel.getStructureData(shopId);
            case 3:
                setStructureViewModel.getProductData(shopId);
                break;
            case 4:
                Toast.makeText(this, "Structure saved successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,UserHome.class));
                finish();
                break;
        }
    }

    void updateMenu(){
        Menu menu = setShopStructureBinding.setStructureNavView.getMenu();
        menu.clear();
        for(Page page : structure.getPages())
            menu.add(0,page.getPageId(),Menu.NONE,page.getPageName());
        setShopStructureBinding.setStructureNavView.invalidate();
        setShopStructureBinding.setStructureNavView.setNavigationItemSelectedListener(this);
    }

    void addPage(){
        final EditText etPageName = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Page Name");
        builder.setMessage("");
        builder.setView(etPageName);
        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                structure.addPage(etPageName.getText().toString());
                setStructureViewModel.setStructure(structure);
                Toast.makeText(SetShopStructure.this, "Page Added!", Toast.LENGTH_SHORT).show();
                updateMenu();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void setShop(Shop shop) {
        this.shop = shop;
    }

    void setStructure(Structure structure) {
        this.structure = structure;
    }

    void setStatus(int status) {
        this.status = status;
        updateStatus(status);
    }

    void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    void setStructureStatus(int structureStatus) {

        this.structureStatus = structureStatus;

        if(structureStatus==1||structureStatus==0){
            updateMenu();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.shop_pages_loader,new ShopPageFragment(structure.getPage(1001)),structure.getPage(1001).getPageName())
                    .commit();
            setShopStructureBinding.setStructureNavView.setCheckedItem(setShopStructureBinding.setStructureNavView.getMenu().getItem(0));
        }
    }
}
