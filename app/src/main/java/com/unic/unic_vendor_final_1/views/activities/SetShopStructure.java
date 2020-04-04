package com.unic.unic_vendor_final_1.views.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.DoubleImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductListAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.TripleImageAdapter;
import com.unic.unic_vendor_final_1.databinding.ActivitySetShopStructureBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.shop_structure_fragments.ShopPageFragment;
import com.unic.unic_vendor_final_1.views.SelectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetShopStructure extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, NavigationView.OnNavigationItemSelectedListener {

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0;
            } else {
                outRect.left = space;
            }
        }
    }

    private Shop shop;
    private List<Map<String,Object>> products = new ArrayList<>();

    private ActivitySetShopStructureBinding setStructureBinding;
    private boolean isDataAcquired = false;
    private int checkedId;

    private SetStructureViewModel setStructureViewModel;
    private List<String> categories = new ArrayList<String>();
    private List<String> selectedProductIDs = new ArrayList<>();
    private List<Map<String,Object>> selectedProducts = new ArrayList<>();
    private List<String> selectedImages = new ArrayList<>();
    private ProductListAdapter productListAdapter = new ProductListAdapter(this);
    private Structure structure;
    private Menu shopPagesMenu;
    private Map<String,Object> viewAdditionData = new HashMap<>();

    private ViewGroup parent;
    private ArrayList<View> views = new ArrayList<>();
    private int prevY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStructureBinding = ActivitySetShopStructureBinding.inflate(getLayoutInflater());
        setContentView(setStructureBinding.getRoot());

        structure = new Structure(getIntent().getStringExtra("shopId"));

        setStructureBinding.productSelectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this);
        setStructureBinding.productSelectRecyclerView.setAdapter(productListAdapter);



        setStructureViewModel = ViewModelProviders.of(this).get(SetStructureViewModel.class);
        setStructureViewModel.getShopData(getIntent().getStringExtra("shopId"));
        setStructureViewModel.getShop().observe(this, new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {
                updateShop(shop);
            }
        });
        setStructureViewModel.getProducts().observe(this, new Observer<List<Map<String,Object>>>() {
            @Override
            public void onChanged(List<Map<String,Object>> objects) {
                products = objects;
                for(int i=0;i<products.size();i++){
                    String categ;
                    categ = products.get(i).get("category").toString();
                    if(categories.contains(categ))
                        continue;
                    categories.add(categ);
                }
                productListAdapter.setProducts(products);
                productListAdapter.notifyDataSetChanged();
            }
        });
        setStructureViewModel.getStructure().observe(this, new Observer<Structure>() {
            @Override
            public void onChanged(Structure structure) {
                if(structure!=null)
                    setStructure(structure);
            }
        });
        setStructureViewModel.getStructureStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateUI(integer);
            }
        });
        setStructureViewModel.getViewAdditionData().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> stringObjectMap) {
                setViewAdditionData(stringObjectMap);
            }
        });
        //setStructureBinding.addView.setOnClickListener(this);
        //setStructureBinding.finishAddingProduct.setOnClickListener(this);
        //parent = findViewById(R.id.view_inflater);

        shopPagesMenu = setStructureBinding.shopPagesNavView.getMenu();
        updateUI(0);
        setStructureBinding.shopNewPage.setOnClickListener(this);
        setStructureBinding.confirmProductSelection.setOnClickListener(this);
        setStructureBinding.shopPagesNavView.setNavigationItemSelectedListener(this);
        setStructureBinding.btnConfirmStructure.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ViewId_REQUEST){
            if(resultCode==RESULT_OK){
                checkedId=data.getIntExtra("ID",-1);
                getDisplayData(checkedId);
            }
        }
    }

     */

    private void updateShop(Shop shop){
        this.shop = shop;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.confirm_product_selection:
                selectedProductIDs = productListAdapter.returnSelectedProductIDs();
                confirmSelectedProducts();
                break;
            case R.id.shop_new_page:
                final View popupView = LayoutInflater.from(this).inflate(R.layout.page_title_selector,null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            case R.id.btn_confirm_structure:
                setStructureViewModel.uploadShopStructure(structure);
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        View parentView = (View)view.getParent();
        int currView = views.indexOf(parentView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)parentView.getLayoutParams();




        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                prevY = (int)motionEvent.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:

                if((currView!=views.size()-1)&&params.topMargin>((RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams()).topMargin){
                    RelativeLayout.LayoutParams lowerViewParams = (RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams();
                    lowerViewParams.topMargin-=params.height;
                    views.get(currView+1).setLayoutParams(lowerViewParams);
                    swapViews(views,currView,currView+1);
                    currView++;
                    break;
                }
                else if((currView!=0)&&params.topMargin<((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).topMargin){
                    RelativeLayout.LayoutParams lowerViewParams = (RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams();
                    lowerViewParams.topMargin+=params.height;
                    views.get(currView-1).setLayoutParams(lowerViewParams);
                    swapViews(views,currView,currView-1);
                    currView++;
                    break;
                }
                else{
                    params.topMargin+=((int)motionEvent.getRawY()-prevY);
                    parentView.setLayoutParams(params);
                    prevY = (int) motionEvent.getRawY();
                }

                return true;
            case MotionEvent.ACTION_UP:

                if(motionEvent.getRawX()>540){

                    deleteView(currView);
                    return true;
                }


                if((currView!=views.size()-1)&&params.topMargin+params.height>((RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams()).topMargin){
                    params.topMargin = ((RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams()).topMargin-params.height;
                }
                else if (currView!=0&&params.topMargin<((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).height){
                    params.topMargin = ((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).height;
                }
                else if(params.topMargin < 0){
                    params.topMargin = 0;
                }
                else if(params.topMargin+params.height>1920){
                    params.topMargin = 1920 - params.topMargin;
                }

                parentView.setLayoutParams(params);
                Toast.makeText(this, new Integer(view.getId()).toString(), Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }

    private void addView(int id){
        RelativeLayout.LayoutParams maxParams = views.size()>0? (RelativeLayout.LayoutParams)views.get(views.size()-1).getLayoutParams():null;
        switch(id){
            case 2:
                View doubleItemView = LayoutInflater.from(this).inflate(R.layout.double_image_view,null);
                doubleItemView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(360)));
                views.add(doubleItemView);

                RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams)doubleItemView.getLayoutParams();
                params3.topMargin = views.size()>1?maxParams.topMargin+maxParams.height:0;
                doubleItemView.setLayoutParams(params3);
                parent.addView(doubleItemView);

                doubleItemView.findViewById(R.id.double_image_header).setOnTouchListener(this);
                RecyclerView doubleItemRecyclerView = doubleItemView.findViewById(R.id.double_image_recycler_view);
                LinearLayoutManager layoutManager3= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
                doubleItemRecyclerView.setLayoutManager(layoutManager3);
                DoubleImageAdapter adapter3 = new DoubleImageAdapter(this);
                adapter3.setProducts(selectedProducts);
                doubleItemRecyclerView.setAdapter(adapter3);
                doubleItemRecyclerView.addItemDecoration(new SpacesItemDecoration((int) dpToPx(10)));

                com.unic.unic_vendor_final_1.datamodels.View doubleImagesViewClass = new com.unic.unic_vendor_final_1.datamodels.View();
                doubleImagesViewClass.setViewId("22"+Integer.valueOf(views.size()).toString());
                doubleImagesViewClass.setFields("imageId,name,price");
                doubleImagesViewClass.setProducts(selectedProductIDs);
                doubleImagesViewClass.setHeight(360);
                doubleImagesViewClass.setPos(views.size()-1);
                doubleImagesViewClass.setyPos(params3.topMargin);

                //structure.addView(doubleImagesViewClass);
                break;
            case 3:
                View tripleItemView = LayoutInflater.from(this).inflate(R.layout.triple_image_view,null);
                tripleItemView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(253)));
                views.add(tripleItemView);

                RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) tripleItemView.getLayoutParams();
                params4.topMargin = views.size()>1?maxParams.topMargin+maxParams.height:0;
                tripleItemView.setLayoutParams(params4);
                parent.addView(tripleItemView);

                tripleItemView.findViewById(R.id.triple_image_header).setOnTouchListener(this);
                RecyclerView tripleItemRecyclerView = tripleItemView.findViewById(R.id.triple_image_recycler_view);
                LinearLayoutManager layoutManager4 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
                tripleItemRecyclerView.setLayoutManager(layoutManager4);
                TripleImageAdapter adapter4 = new TripleImageAdapter(this);
                adapter4.setProducts(selectedProducts);
                tripleItemRecyclerView.setAdapter(adapter4);
                tripleItemRecyclerView.addItemDecoration(new SpacesItemDecoration((int) dpToPx(10)));

                com.unic.unic_vendor_final_1.datamodels.View tripleImagesViewClass = new com.unic.unic_vendor_final_1.datamodels.View();
                tripleImagesViewClass.setViewId("23"+Integer.valueOf(views.size()).toString());
                tripleImagesViewClass.setFields("imageId,name,price");
                tripleImagesViewClass.setProducts(selectedProductIDs);
                tripleImagesViewClass.setHeight(253);
                tripleImagesViewClass.setPos(views.size()-1);
                tripleImagesViewClass.setyPos(params4.topMargin);

                //structure.addView(tripleImagesViewClass);
        }

        updateParentHeight();
        setStructureViewModel.getStructure().setValue(structure);

    }


    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private ArrayList<View> swapViews(ArrayList<View> views,int v1, int v2){
        View v = views.get(v1);
        views.set(v1,views.get(v2));
        views.set(v2,v);
        return views;
    }

    private void deleteView(int id){
        parent.removeViewAt(id);
        views.remove(id);
    }

    private void updateParentHeight(){
        ViewGroup.LayoutParams params =  parent.getLayoutParams();
        int height = 0;
        for(int i=0;i<views.size();i++){
            height+=views.get(i).getLayoutParams().height;
        }
        params.height = height+(int)dpToPx(50);
    }

    private void setStructure(Structure structure){
        this.structure = structure;
        shopPagesMenu.clear();
        for(int i=0;i<structure.getPages().size();i++) {
            shopPagesMenu.add(0,structure.getPages().get(i).getPageId(),Menu.NONE,structure.getPages().get(i).getPageName());
        }
        setStructureBinding.shopPagesNavView.invalidate();
        setStructureBinding.shopPagesNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        int id = item.getItemId();
        String tag = null;
        setStructureBinding.shopPagesNavView.setCheckedItem(id);

        for(int i=0;i<structure.getPages().size();i++){
            if(structure.getPages().get(i).getPageId()==item.getItemId()){
                fragment = new ShopPageFragment(structure.getPages().get(i));
                tag = structure.getPages().get(i).getPageName();
                break;
            }

        }

        if(fragment!=null){
            setStructureBinding.shopStructureDrawer.closeDrawers();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.shop_pages_loader,fragment,tag)
                    .commit();
            return true;

        }
        return false;
    }

    private void updateUI(int code){
        switch(code){
            case 0:
                setStructureBinding.shopStructureDrawer.setVisibility(View.VISIBLE);
                setStructureBinding.productSelectParentView.setVisibility(View.GONE);
                setStructureBinding.shopPagesNavView.setCheckedItem(structure.getPages().get(0).getPageId());
                FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
                ft0.replace(setStructureBinding.shopPagesLoader.getId(),new ShopPageFragment(structure.getPages().get(0)),"Home");
                ft0.commit();
                if(getIntent().getIntExtra("stat",0)==1){
                    Structure structure2 = new Structure(getIntent().getStringExtra("shopId"));
                    com.unic.unic_vendor_final_1.datamodels.View view1 = new com.unic.unic_vendor_final_1.datamodels.View();
                    view1.setViewId("411");
                    view1.setHeader("Newest");
                    view1.setFields("name,imageId,price");
                    view1.setProducts(new ArrayList<String>());
                    view1.setHeight(360);
                    List<String> ids = new ArrayList<>();
                    ids.add("I6Wjq4NCjMVIrnv4vt4Q");
                    ids.add("IJp362oXZqv7wjzoOsLy");
                    ids.add("ovHw199qHqV7mPPsenPL");
                    view1.setProducts(ids);
                    structure2.addView(1001,view1);
                    com.unic.unic_vendor_final_1.datamodels.View view2 = new com.unic.unic_vendor_final_1.datamodels.View();
                    view2.setViewId("412");
                    view2.setHeader("Trending");
                    view2.setFields("name,imageId,price");
                    view2.setProducts(new ArrayList<String>());
                    view2.setHeight(360);
                    List<String> ids2 = new ArrayList<>();
                    ids2.add("MMFJM159h1xcDWoBvlj9");
                    ids2.add("XqvY0qxaGuRmfoZuQrsr");
                    ids2.add("Y6QSXRtpB171DWqweCQg");
                    view2.setProducts(ids2);
                    structure2.addView(1001,view2);
                    setStructureViewModel.getStructure().setValue(structure2);
                }
                break;
            case 1:
                setStructureBinding.shopStructureDrawer.setVisibility(View.GONE);
                setStructureBinding.productSelectParentView.setVisibility(View.VISIBLE);
                selectProducts();
                break;
            case 2:
                setStructureBinding.shopStructureDrawer.setVisibility(View.VISIBLE);
                setStructureBinding.productSelectParentView.setVisibility(View.GONE);
                break;
            case 5:
                Toast.makeText(this, "Structure saved successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,UserHome.class));
                finish();
        }
    }

    private void selectProducts(){
        selectedProductIDs.clear();
        productListAdapter.setSelectedProducts(selectedProductIDs);
        productListAdapter.notifyDataSetChanged();
    }

    private void confirmSelectedProducts(){
        structure.updateProductList(Integer.parseInt(viewAdditionData.get("pageId").toString()),viewAdditionData.get("viewCode").toString(),selectedProductIDs);
        setStructureViewModel.getStructure().setValue(structure);
        updateUI(2);

    }

    public void setViewAdditionData(Map<String, Object> viewAdditionData) {
        this.viewAdditionData = viewAdditionData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data!=null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(data.getStringExtra("tag"));
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }
}
