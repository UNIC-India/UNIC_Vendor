package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ProductDetailsAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductDescriptionImageAdapter;
import com.unic.unic_vendor_final_1.commons.BlurTransformation;
import com.unic.unic_vendor_final_1.databinding.FragmentProductDescriptionBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDescriptionFragment extends Fragment implements View.OnClickListener{

    Map<String,Object> product;
    FragmentProductDescriptionBinding fragmentProductDescriptionBinding;
    ProductDetailsAdapter productDetailsAdapter;
    SetStructureViewModel setStructureViewModel;
    private boolean isReverting = false;
    public ProductDescriptionFragment() {
        // Required empty public constructor
    }

    public ProductDescriptionFragment(Map<String,Object> product){
        this.product=product;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProductDescriptionBinding=FragmentProductDescriptionBinding.inflate(getLayoutInflater(),container,false);
        setStructureViewModel= new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        setStructureViewModel.setCurrentFrag(getActivity().getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader));

        if(product.get("availability")!=null&&Integer.parseInt(product.get("availability").toString())==-1) {
            fragmentProductDescriptionBinding.productAvailabilitySwitch.setText("Unavailable");
            fragmentProductDescriptionBinding.productAvailabilitySwitch.setChecked(true);
        }
        else {
            fragmentProductDescriptionBinding.productAvailabilitySwitch.setText("Available");
            fragmentProductDescriptionBinding.productAvailabilitySwitch.setChecked(false);
        }



        productDetailsAdapter=new ProductDetailsAdapter(getContext());
        productDetailsAdapter.setData(product);
        fragmentProductDescriptionBinding.rvDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentProductDescriptionBinding.rvDetails.setAdapter(productDetailsAdapter);
        fragmentProductDescriptionBinding.btnAddToCart.setOnClickListener(this);
        fragmentProductDescriptionBinding.btnEditProductDetails.setOnClickListener(this);
        fragmentProductDescriptionBinding.btnFinishEditProductDetails.setOnClickListener(this);
        fragmentProductDescriptionBinding.tvEditProductDetails.setOnClickListener(this);

        fragmentProductDescriptionBinding.productAvailabilitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isReverting) {
                isReverting = false;
                return;
            }

            new AlertDialog.Builder(getContext())
                    .setMessage("Set " + product.get("name").toString() + "'s status to " + (isChecked?"unavailable":"available") + "?")
                    .setPositiveButton("YES",((dialog, which) -> {
                        fragmentProductDescriptionBinding.productAvailabilitySwitch.setText(isChecked?"Unavailable":"Available");
                        setStructureViewModel.setProductAvailability(product.get("firestoreId").toString(),!isChecked);
                    }))
                    .setNegativeButton("NO", ((dialog, which) -> {
                        isReverting = true;
                        fragmentProductDescriptionBinding.productAvailabilitySwitch.setChecked(!isChecked);
                    }))
                    .setCancelable(false)
                    .create().show();
        });

        ViewPager2.PageTransformer pageTransformer = (page, position) -> {

            page.setTranslationX(- dpToPx(30)*position);

            page.setScaleY(1-(0.25f*Math.abs(position)));

            page.setAlpha(0.25f + (1-Math.abs(position)));
        };

        List<String> imageLinks;
        try {
            if (product.get("imageLinks")!=null||product.containsKey("imageLinks")) {
                imageLinks = (List<String>) product.get("imageLinks");

                if(imageLinks.size() ==1) {
                    fragmentProductDescriptionBinding.ivProductDetailsPhotoSlider.setVisibility(View.INVISIBLE);
                    fragmentProductDescriptionBinding.ivProductDetailsPhoto.setVisibility(View.VISIBLE);
                    fragmentProductDescriptionBinding.ivProductDetailsBackgroundPhoto.setVisibility(View.VISIBLE);
                    fragmentProductDescriptionBinding.sliderImagesTab.setVisibility(View.GONE);
                    Glide
                            .with(getContext())
                            .load(imageLinks.get(0))
                            .into(fragmentProductDescriptionBinding.ivProductDetailsPhoto);
                    Glide
                            .with(getContext())
                            .load(imageLinks.get(0))
                            .transform(new BlurTransformation(getContext()))
                            .into(fragmentProductDescriptionBinding.ivProductDetailsBackgroundPhoto);
                }
                else if(imageLinks.size()>1){

                    fragmentProductDescriptionBinding.ivProductDetailsPhotoSlider.setVisibility(View.VISIBLE);
                    fragmentProductDescriptionBinding.ivProductDetailsPhoto.setVisibility(View.INVISIBLE);
                    fragmentProductDescriptionBinding.ivProductDetailsBackgroundPhoto.setVisibility(View.INVISIBLE);
                    TabLayout sliderTabs = fragmentProductDescriptionBinding.sliderImagesTab;

                    ProductDescriptionImageAdapter productDescriptionImageAdapter = new ProductDescriptionImageAdapter(getContext(), imageLinks);
                    fragmentProductDescriptionBinding.ivProductDetailsPhotoSlider.setAdapter(productDescriptionImageAdapter);
                    fragmentProductDescriptionBinding.ivProductDetailsPhotoSlider.setPageTransformer(pageTransformer);
                    fragmentProductDescriptionBinding.ivProductDetailsPhotoSlider.setOffscreenPageLimit(1);
                    fragmentProductDescriptionBinding.ivProductDetailsPhotoSlider.addItemDecoration(new ShopPageFragment.SpacesItemDecoration((int) dpToPx(45)));

                    TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(sliderTabs,fragmentProductDescriptionBinding.ivProductDetailsPhotoSlider,true,((tab, position) -> {}));
                    tabLayoutMediator.attach();

                }
            }
            else if(product.containsKey("imageId")) {
                fragmentProductDescriptionBinding.ivProductDetailsPhotoSlider.setVisibility(View.INVISIBLE);
                fragmentProductDescriptionBinding.ivProductDetailsPhoto.setVisibility(View.VISIBLE);
                fragmentProductDescriptionBinding.ivProductDetailsBackgroundPhoto.setVisibility(View.VISIBLE);
                fragmentProductDescriptionBinding.sliderImagesTab.setVisibility(View.GONE);

                Glide
                        .with(getContext())
                        .load(product.get("imageId").toString())
                        .into(fragmentProductDescriptionBinding.ivProductDetailsPhoto);
                Glide
                        .with(getContext())
                        .load(product.get("imageId").toString())
                        .transform(new BlurTransformation(getContext()))
                        .into(fragmentProductDescriptionBinding.ivProductDetailsBackgroundPhoto);
            }

            else {

                fragmentProductDescriptionBinding.ivProductDetailsPhoto.setVisibility(View.INVISIBLE);
                fragmentProductDescriptionBinding.ivProductDetailsBackgroundPhoto.setVisibility(View.INVISIBLE);

            }
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }



        return fragmentProductDescriptionBinding.getRoot();
    }

    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == fragmentProductDescriptionBinding.btnAddToCart.getId()) {
            Toast.makeText(getActivity(), "Cannot add to cart on Vendor App", Toast.LENGTH_SHORT).show();
            return;
        }

        if (v.getId() == fragmentProductDescriptionBinding.btnEditProductDetails.getId()) {
            fragmentProductDescriptionBinding.btnFinishEditProductDetails.setVisibility(View.VISIBLE);
            fragmentProductDescriptionBinding.btnEditProductDetails.setVisibility(View.INVISIBLE);
            fragmentProductDescriptionBinding.tvEditProductDetails.setText("Confirm Edit");
            productDetailsAdapter.beginEditMode();
        }

        if(v.getId() == fragmentProductDescriptionBinding.btnFinishEditProductDetails.getId()) {
            fragmentProductDescriptionBinding.btnFinishEditProductDetails.setVisibility(View.INVISIBLE);
            fragmentProductDescriptionBinding.btnEditProductDetails.setVisibility(View.VISIBLE);
            fragmentProductDescriptionBinding.tvEditProductDetails.setText("Edit Details");
            updateProduct(productDetailsAdapter.endEditMode());
        }

        if(v.getId() == fragmentProductDescriptionBinding.tvEditProductDetails.getId()) {
            if(fragmentProductDescriptionBinding.tvEditProductDetails.getText().toString().equals("Edit Details")){
                fragmentProductDescriptionBinding.btnFinishEditProductDetails.setVisibility(View.VISIBLE);
                fragmentProductDescriptionBinding.btnEditProductDetails.setVisibility(View.INVISIBLE);
                fragmentProductDescriptionBinding.tvEditProductDetails.setText("Confirm Edit");
                productDetailsAdapter.beginEditMode();
            }
            else {
                fragmentProductDescriptionBinding.btnFinishEditProductDetails.setVisibility(View.INVISIBLE);
                fragmentProductDescriptionBinding.btnEditProductDetails.setVisibility(View.VISIBLE);
                fragmentProductDescriptionBinding.tvEditProductDetails.setText("Edit Details");
                updateProduct(productDetailsAdapter.endEditMode());
            }
        }

    }

    private void updateProduct(Map<String ,Object> modifiedData) {
        if(modifiedData == null)
            return;

        setStructureViewModel.updateProductData(product.get("firestoreId").toString(),modifiedData);
    }
}