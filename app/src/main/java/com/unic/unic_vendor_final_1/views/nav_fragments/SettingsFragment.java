package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentSettingsBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.helpers.IntermediateShopList;
import com.unic.unic_vendor_final_1.views.settings_fragments.UserPermissionsFragment;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding fragmentSettingsBinding;
    UserShopsViewModel userShopsViewModel;
    String requestedShop = "";
    boolean requestCompleted = false;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public SettingsFragment(String requestedShop){
        this.requestedShop = requestedShop;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSettingsBinding= FragmentSettingsBinding.inflate(inflater, container,false);
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.titleSetter.setValue(3);
        fragmentSettingsBinding.cdManageTeam.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new IntermediateShopList(2))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit());

        fragmentSettingsBinding.cdAddLogo.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new IntermediateShopList(3))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit());

        fragmentSettingsBinding.cdManagePermissions.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new IntermediateShopList(4))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit());

        userShopsViewModel.getShops().observe(getViewLifecycleOwner(),shops -> {
            if(shops!=null&&requestedShop.length()!=0&&!requestCompleted) {
                requestCompleted = true;
                userShopsViewModel.getShops().getValue().forEach(shop -> {
                    if(shop.getId().equals(requestedShop)){
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.home_fragment,new UserPermissionsFragment(shop.getId(),shop.getName(),shop.getIsPrivate()))
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        });

    return fragmentSettingsBinding.getRoot();
    }

}

