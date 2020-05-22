package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ShopAdapter;
import com.unic.unic_vendor_final_1.adapters.ShopListAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentMyProductsBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.activities.AddShop;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProducts extends Fragment {
    private UserShopsViewModel shopsViewModel;

    private ShopListAdapter adapter;
    FragmentMyProductsBinding myProductsBinding;

    public MyProducts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myProductsBinding= FragmentMyProductsBinding.inflate( getLayoutInflater(),container, false);




        adapter = new ShopListAdapter(getContext());
        shopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        adapter.setShops(shopsViewModel.getShops().getValue());
        shopsViewModel.getShops().observe(getViewLifecycleOwner(), new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                adapter.setShops(shops);
                adapter.notifyDataSetChanged();
            }
        });
        myProductsBinding.rvShops.setAdapter(adapter);
        myProductsBinding.rvShops.setLayoutManager(new LinearLayoutManager(getContext()));



        return myProductsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
