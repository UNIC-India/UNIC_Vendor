package com.unic.unic_vendor_final_1.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.ActivitySetShopStructureBinding;
import com.unic.unic_vendor_final_1.datamodels.Page;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.commons.StructureTemplates;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.CategorySelector;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ImagePicker;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.NoProductsFragment;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductSelector;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ViewSelector;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ShopPageFragment;

import java.util.List;
import java.util.Map;

public class SetShopStructure extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Fragment currentFragment;
    public Structure structure;
    private Shop shop;
    Toolbar toolbar;
    public int currentPage=1001;
    private List<Map<String,Object>> products;

    public int currentPageId = 0;
    public int currentViewCode = 0;

    private int status,structureStatus;

    private String shopId;
    private int option;

    private SetStructureViewModel setStructureViewModel;
    public ActivitySetShopStructureBinding setShopStructureBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStructureViewModel = new ViewModelProvider(this).get(SetStructureViewModel.class);

        setShopStructureBinding = ActivitySetShopStructureBinding.inflate(getLayoutInflater());
        setContentView(setShopStructureBinding.getRoot());

        shopId = getIntent().getStringExtra("shopId");
        setStructureViewModel.getShop().observe(this, shop -> {
            setShop(shop);
            setButtonsAndToolBar(currentFragment);


        });
        setStructureViewModel.getCurrentFrag().observe(this, fragment -> {
            setButtonsAndToolBar(fragment);
            setCurrentFragment(fragment);
        });

        setStructureViewModel.getStructure().observe(this, this::setStructure);
        setStructureViewModel.getProducts().observe(this, this::setProducts);

        setStructureViewModel.getStructureStatus().observe(this, this::setStructureStatus);

        setStructureViewModel.getStatus().observe(this, this::setStatus);

        setStructureViewModel.getShopExtras(shopId);

        toolbar = setShopStructureBinding.setStructureToolbar;
        setSupportActionBar(toolbar);


        DrawerLayout drawer = setShopStructureBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setShopStructureBinding.setStructureNavView.setNavigationItemSelectedListener(this);
        setShopStructureBinding.btnleft.setOnClickListener(this);
        setShopStructureBinding.btnRight.setOnClickListener(this);
        setShopStructureBinding.confirmShopStructure.setOnClickListener(this);

        option = getIntent().getIntExtra("template", 0);

        updateStatus(0);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnleft:

                if(currentFragment.getClass()==ShopPageFragment.class){
                    addPage();
                }
                else if(currentFragment.getClass()== ProductSelector.class){
                    getSupportFragmentManager().popBackStack();

                }
                else if(currentFragment.getClass()== CategorySelector.class){
                    getSupportFragmentManager().popBackStack();
                }
                else if(currentFragment.getClass()== ViewSelector.class){
                    ((ViewSelector)currentFragment).onClick(v);
                }
                break;
            case R.id.confirm_shop_structure:
                setStructureViewModel.saveShopStructure();
                break;
            case R.id.btnRight:
                if(currentFragment.getClass()==ShopPageFragment.class){
                    ((ShopPageFragment)currentFragment).onClick(v);
                }
                else if(currentFragment.getClass()== ViewSelector.class){
                    ((ViewSelector)currentFragment).onClick(v);
                }
                else if(currentFragment.getClass()== ProductSelector.class){
                    ((ProductSelector)currentFragment).onClick(v);

                }
                else if(currentFragment.getClass()== CategorySelector.class){
                    ((CategorySelector)currentFragment).onClick(v);
                }
                else if(currentFragment.getClass()==ImagePicker.class){
                    ((ImagePicker)currentFragment).onClick(v);
                }
                break;


                /*
            case R.id.btn_add_products:
                Toast.makeText(this, Integer.valueOf(((View)v.getParent()).getId()).toString(), Toast.LENGTH_SHORT).show();
                break;
                 */
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        for(Page page : structure.getPages()){
            if(page.getPageId() == item.getItemId()){
                currentPage=page.getPageId();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.shop_pages_loader,new ShopPageFragment(page),page.getPageName())
                        .commit();
                setShopStructureBinding.setStructureNavView.setCheckedItem(item);
                setShopStructureBinding.drawerLayout.closeDrawers();
                setShopStructureBinding.setStructureNavView.setCheckedItem(item);
                return true;
            }
        }

        return false;
    }

    public void setButtonsAndToolBar(Fragment currentFragment){
        if(currentFragment!=null){
            if(currentFragment.getClass()==NoProductsFragment.class){
                if(shop!=null)
                    setShopStructureBinding.tvTitle.setText(shop.getName());
                setShopStructureBinding.btnleft.setVisibility(View.GONE);
                setShopStructureBinding.btnRight.setVisibility(View.GONE);
            }
            if(currentFragment.getClass()==ShopPageFragment.class){
                if(shop!=null)
                    setShopStructureBinding.tvTitle.setText(shop.getName());
                setShopStructureBinding.confirmShopStructure.setVisibility(View.VISIBLE);
                setShopStructureBinding.btnRight.setText("Add View");
                setShopStructureBinding.btnleft.setText("Add Page");
                setShopStructureBinding.btnRight.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
            else if(currentFragment.getClass()== ViewSelector.class){
                setShopStructureBinding.tvTitle.setText("Select a View");
                setShopStructureBinding.confirmShopStructure.setVisibility(View.GONE);
                setShopStructureBinding.btnRight.setText("Confirm");
                setShopStructureBinding.btnleft.setText("Cancel");
                setShopStructureBinding.btnRight.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            }
            else if(currentFragment.getClass()== ProductSelector.class){
                setShopStructureBinding.tvTitle.setText("Select Products");
                setShopStructureBinding.confirmShopStructure.setVisibility(View.GONE);
                setShopStructureBinding.btnRight.setText("Confirm");
                setShopStructureBinding.btnleft.setText("Cancel");
                setShopStructureBinding.btnRight.setBackgroundColor(getResources().getColor(R.color.colorSecondary));

            }
            else if(currentFragment.getClass()== CategorySelector.class){
                setShopStructureBinding.tvTitle.setText("Select Categories");
                setShopStructureBinding.confirmShopStructure.setVisibility(View.GONE);
                setShopStructureBinding.btnRight.setText("Confirm");
                setShopStructureBinding.btnleft.setText("Cancel");
                setShopStructureBinding.btnRight.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            }
            else if(currentFragment.getClass()==ImagePicker.class){
                setShopStructureBinding.tvTitle.setText("Select Images");
                setShopStructureBinding.confirmShopStructure.setVisibility(View.GONE);
                setShopStructureBinding.btnRight.setText("Confirm");
                setShopStructureBinding.btnleft.setText("Cancel");
                setShopStructureBinding.btnRight.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            }

        }

    }

    void populateHeader(){
        View header = setShopStructureBinding.setStructureNavView.getHeaderView(0);
        ((TextView)header.findViewById(R.id.header_shop_name)).setText(shop.getName());
    }

    void setTemplate(int template){
        switch (template){
            case 0:
                status = 2;
                updateStatus(status);
                break;
            case 1:

                if(shop.getNoOfProducts()==0){
                    setShopStructureBinding.confirmShopStructure.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.shop_pages_loader,new NoProductsFragment())
                            .commit();
                }

                else {
                    structure = StructureTemplates.getTemplate1(shopId);
                    setStructureViewModel.getStructure().setValue(structure);
                    updateMenu();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.shop_pages_loader, new ShopPageFragment(structure.getPage(1001)), structure.getPage(1001).getPageName())
                            .commit();
                    status = 3;
                    updateStatus(status);
                }
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
                populateHeader();
                setTemplate(option);
                break;
            case 2:
                setStructureViewModel.getStructureData(shopId);
            case 3:
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
        builder.setPositiveButton("DONE", (dialog, which) -> {
            structure.addPage(etPageName.getText().toString());
            setStructureViewModel.setStructure(structure);
            Toast.makeText(SetShopStructure.this, "Page Added!", Toast.LENGTH_SHORT).show();
            updateMenu();
        });
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void setShop(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    void setStatus(int status) {
        this.status = status;
        updateStatus(status);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    void setStructureStatus(int structureStatus) {

        this.structureStatus = structureStatus;

        if(structureStatus==1||structureStatus==0){
            if(structureStatus==0){
                structure = new Structure(shopId);
                setStructureViewModel.setStructure(structure);
            }
            updateMenu();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.shop_pages_loader,new ShopPageFragment(structure.getPage(1001)),structure.getPage(1001).getPageName())
                    .commit();
            setShopStructureBinding.setStructureNavView.setCheckedItem(setShopStructureBinding.setStructureNavView.getMenu().getItem(0));
        }
    }

    public List<Map<String, Object>> getProducts() {
        return products;
    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
    }

    public void selectProducts(int pageId, int viewCode){
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.shop_pages_loader,new ProductSelector(pageId,viewCode))
                .addToBackStack(null)
                .commit();
    }
    public void selectCategories(int pageId, int viewCode){
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.shop_pages_loader,new CategorySelector(pageId,viewCode))
                .addToBackStack(null)
                .commit();
    }
    public void selectView(int pageId,int code){

        currentPageId = pageId;
        currentViewCode = code;

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.shop_pages_loader,new ViewSelector(pageId,code))
                .addToBackStack(null)
                .commit();

    }

    public void selectImages(int pageId,int code){

        currentViewCode = code;
        currentPageId = pageId;

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.shop_pages_loader,new ImagePicker(pageId,code))
                .addToBackStack(null)
                .commit();
    }




    public void addView(int pageId, com.unic.unic_vendor_final_1.datamodels.View view, int code){

        structure.getPage(pageId).addView(view,code);
        setStructureViewModel.setStructure(structure);
        if(code/10==4)
        selectProducts(pageId,view.getViewCode());
        else if(code/10==2)
            selectCategories(pageId,view.getViewCode());
        else if (code/10==1)
            selectImages(pageId,view.getViewCode());
    }

    public void returnToPage(int pageId){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.shop_pages_loader,new ShopPageFragment(structure.getPage(pageId)),structure.getPage(pageId).getPageName())
                .commit();
        setShopStructureBinding.setStructureNavView.setCheckedItem(pageId);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {

        if(currentPageId==0||currentViewCode==0)
            return;

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.shop_pages_loader,currentFragment)
                .addToBackStack(null)
                .commit();

        super.onRestart();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        if(currentFragment.getClass()==ImagePicker.class){
            outState.putString("fragment","Image Picker");
            if(currentPageId!=0)
                outState.putInt("pageId",currentPageId);
            if(currentViewCode!=0)
                outState.putInt("viewCode",currentViewCode);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        if(savedInstanceState.containsKey("fragment")&&savedInstanceState.getString("fragment").equals("Image Picker")){
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.shop_pages_loader,currentFragment)
                    .addToBackStack(null)
                    .commit();

        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
