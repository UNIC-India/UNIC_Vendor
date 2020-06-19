package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ProductDetailsAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentProductDescriptionBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

import java.util.HashMap;
import java.util.Map;


public class ProductDescriptionFragment extends Fragment {

    Map<String,Object> product;
    FragmentProductDescriptionBinding fragmentProductDescriptionBinding;
    ProductDetailsAdapter productDetailsAdapter;
    SetStructureViewModel setStructureViewModel;
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
        if(product.get("imageId").toString().length()<=2){
            fragmentProductDescriptionBinding.ivProdutcPhoto.setVisibility(View.GONE);
        }
        else{
            Glide
                    .with(getContext())
                    .load(product.get("imageId"))
                    .into(fragmentProductDescriptionBinding.ivProdutcPhoto);
        }

        Map<String,Object> load = new HashMap<>();
        product.keySet().forEach(key -> {
            if(product.get(key)!=null&&!product.get(key).toString().equals("null")&&!key.equals("firestoreId")&&!key.equals("id"))
                load.put(key,product.get(key));
        });

        productDetailsAdapter=new ProductDetailsAdapter(getContext());
        productDetailsAdapter.setData(load);
        fragmentProductDescriptionBinding.rvDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentProductDescriptionBinding.rvDetails.setAdapter(productDetailsAdapter);
        fragmentProductDescriptionBinding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Cannot add to cart on Vendor App", Toast.LENGTH_SHORT).show();
            }
        });



        return fragmentProductDescriptionBinding.getRoot();
    }
}