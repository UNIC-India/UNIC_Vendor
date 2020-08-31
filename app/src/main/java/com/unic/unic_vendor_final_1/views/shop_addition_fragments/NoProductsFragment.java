package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentNoProductsBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.AddNewProduct;


public class NoProductsFragment extends Fragment {

    FragmentNoProductsBinding fragmentNoProductsBinding;
    SetStructureViewModel setStructureViewModel;

    public NoProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setStructureViewModel = new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        setStructureViewModel.setCurrentFrag(getActivity().getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader));

        fragmentNoProductsBinding = FragmentNoProductsBinding.inflate(inflater,container,false);

        fragmentNoProductsBinding.noProductsAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddNewProduct.class);
            intent.putExtra("shopId",setStructureViewModel.getShop().getValue().getId());
            getActivity().startActivityForResult(intent,AddNewProduct.ADD_PRODUCTS);
        });

        return fragmentNoProductsBinding.getRoot();
    }

}