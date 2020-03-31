package com.unic.unic_vendor_final_1.views.activities;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.DoubleImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductListAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.TripleImageAdapter;
import com.unic.unic_vendor_final_1.databinding.ActivitySetShopStructureBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetShopStructure extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

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
    private List<Map<String,Object>> products;

    private ActivitySetShopStructureBinding setStructureBinding;
    private boolean isDataAcquired = false;
    private int checkedId;

    private SetStructureViewModel setStructureViewModel;
    ArrayList<String> categories = new ArrayList<String>();
    ArrayList<String> selectedProductIDs = new ArrayList<>();
    ArrayList<Map<String,Object>> selectedProducts = new ArrayList<>();
    ArrayList<String> selectedImages = new ArrayList<>();
    ProductListAdapter productListAdapter = new ProductListAdapter(this);
    private Structure structure;

    ViewGroup parent;
    ArrayList<View> views = new ArrayList<>();
    int prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStructureBinding = ActivitySetShopStructureBinding.inflate(getLayoutInflater());
        setContentView(setStructureBinding.getRoot());

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
            }
        });
        setStructureViewModel.getStructure().observe(this, new Observer<Structure>() {
            @Override
            public void onChanged(Structure structure) {
                setStructure(structure);
            }
        });
        setStructureBinding.addView.setOnClickListener(this);
        setStructureBinding.finishAddingProduct.setOnClickListener(this);
        parent = findViewById(R.id.view_inflater);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateShop(Shop shop){
        this.shop = shop;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_view:
                final View popupView = LayoutInflater.from(this).inflate(R.layout.view_selector,null);
                final PopupWindow popupWindow =new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
                RadioGroup radioGroup = popupView.findViewById(R.id.rg_selector);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        isDataAcquired = false;
                        int children = radioGroup.getChildCount();
                        for(int anInt=0;anInt<children;anInt++){
                            if(radioGroup.getChildAt(anInt).getId()==i){
                                getDisplayData(anInt);
                                checkedId = anInt;
                            }
                        }
                        popupWindow.dismiss();


                    }
                });
                break;
            case R.id.finish_adding_product:
                selectedProductIDs =  productListAdapter.returnSelectedProductIDs();
                selectedProducts = productListAdapter.returnSelectedProducts();
                isDataAcquired = true;
                setStructureBinding.dataTableSelector.setVisibility(View.GONE);
                setStructureBinding.viewInflater.setVisibility(View.VISIBLE);
                addView(checkedId);
                break;
            case R.id.finish_shop_addition:
                setStructureViewModel.uploadShopStructure(structure);
                break;
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
                /*
                if(motionEvent.getRawX()>540){

                    deleteView(currView);
                    return true;
                }

                 */
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
                doubleImagesViewClass.setHeight(doubleItemView.getLayoutParams().height);
                doubleImagesViewClass.setPos(views.size()-1);
                doubleImagesViewClass.setyPos(params3.topMargin);

                structure.addView(doubleImagesViewClass);
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
                tripleImagesViewClass.setHeight(tripleItemView.getLayoutParams().height);
                tripleImagesViewClass.setPos(views.size()-1);
                tripleImagesViewClass.setyPos(params4.topMargin);

                structure.addView(tripleImagesViewClass);
        }

        updateParentHeight();
        setStructureViewModel.getStructure().setValue(structure);

    }

    private void getDisplayData(int id){
        selectedProductIDs.clear();
        selectedImages.clear();
        setStructureBinding.viewInflater.setVisibility(View.GONE);
        setStructureBinding.dataTableSelector.setVisibility(View.VISIBLE);
        switch(id){
            case 0:
                //TODO: Add image selector for slider
                break;
            case 1:
            case 2:
            case 3:
                selectProducts();
                break;
            case 4:
                isDataAcquired = true;
                break;

        }
    }
    private void selectProducts(){
        setStructureBinding.viewInflater.setVisibility(View.GONE);
        setStructureBinding.dataTableSelector.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        setStructureBinding.dataSelectorRecyclerView.setLayoutManager(layoutManager);
        productListAdapter.setProducts(products);
        setStructureBinding.dataSelectorRecyclerView.setAdapter(productListAdapter);
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
    }
}
