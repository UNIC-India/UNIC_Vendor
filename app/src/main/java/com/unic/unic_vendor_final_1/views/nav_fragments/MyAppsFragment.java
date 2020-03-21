package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ShopAdapter;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

import java.util.List;

public class MyAppsFragment extends Fragment {

    private UserShopsViewModel shopsViewModel;

    private ShopAdapter adapter;
    public MyAppsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_apps, container, false);

        LinearLayoutManager layoutManager =new LinearLayoutManager(getContext());
        RecyclerView recyclerView =view.findViewById(R.id.my_shops);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopAdapter();
        shopsViewModel = ViewModelProviders.of(this).get(UserShopsViewModel.class);
        shopsViewModel.getAllShops().observe(this, new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                adapter.setShops(shops);
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
