package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ShopAdapter;
import com.unic.unic_vendor_final_1.commons.Helpers;
import com.unic.unic_vendor_final_1.databinding.FragmentMyAppsBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.activities.AddShop;

import java.util.List;

public class MyAppsFragment extends Fragment implements View.OnClickListener{

    private UserShopsViewModel shopsViewModel;
    private FragmentMyAppsBinding myAppsBinding;

    private ShopAdapter adapter;
    public MyAppsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myAppsBinding=FragmentMyAppsBinding.inflate(inflater,container,false);

        // Inflate the layout for this fragment
        LinearLayoutManager layoutManager =new LinearLayoutManager(getContext());
        RecyclerView recyclerView =myAppsBinding.myShops;
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopAdapter(getContext(),0);
        shopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        shopsViewModel.getIsMyAppsLoading().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean){
                myAppsBinding.myAppsCoverView.setVisibility(View.VISIBLE);
                myAppsBinding.myAppsLoading.setVisibility(View.VISIBLE);
                myAppsBinding.myAppsLoading.playAnimation();
            }
        });
        shopsViewModel.titleSetter.setValue(2);
        shopsViewModel.getShops().observe(getViewLifecycleOwner(), shops -> {
            adapter.setShops(shops);
            adapter.notifyDataSetChanged();
            myAppsBinding.myAppsCoverView.setVisibility(View.GONE);
            myAppsBinding.myAppsLoading.setVisibility(View.GONE);
            if(shops==null||shops.size()==0){
                myAppsBinding.noshops.setVisibility(View.VISIBLE);
                myAppsBinding.tvnoshops.setVisibility(View.VISIBLE);
            }
            else{
                myAppsBinding.noshops.setVisibility(View.GONE);
                myAppsBinding.tvnoshops.setVisibility(View.GONE);
            }
        });

        recyclerView.setAdapter(adapter);

        myAppsBinding.btnAddShop.setOnClickListener(this);
        return myAppsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add_shop) {
            startActivity(new Intent(getContext(), AddShop.class));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
