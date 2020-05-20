package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ShopQRAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentQrBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class QRFragment extends Fragment {

    private UserShopsViewModel shopsViewModel;
    private FragmentQrBinding fragmentQrBinding;
    private ShopQRAdapter qrAdapter;


    public QRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        qrAdapter = new ShopQRAdapter(getContext());
        shopsViewModel.getShops().observe(getViewLifecycleOwner(), new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                qrAdapter.setShops(shops);
                qrAdapter.notifyDataSetChanged();
            }
        });
        fragmentQrBinding = FragmentQrBinding.inflate(inflater,container,false);
        fragmentQrBinding.shopQrRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentQrBinding.shopQrRecyclerView.setAdapter(qrAdapter);

        return fragmentQrBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void displayQRCode(String dynamicLink){

    }

    private void makeDynamicLink(String shopId,String shopName){
        String subscribeLink = shopsViewModel.buildSubscribeLink(shopId,shopName);
        Toast.makeText(getActivity(), subscribeLink, Toast.LENGTH_SHORT).show();
        Timber.w("Got link %s", subscribeLink);
    }


}
