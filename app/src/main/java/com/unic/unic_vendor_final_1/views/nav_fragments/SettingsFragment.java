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
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.helpers.IntermidiateShopList;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding fragmentSettingsBinding;
    UserShopsViewModel userShopsViewModel;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSettingsBinding= FragmentSettingsBinding.inflate(getLayoutInflater(), container,false);
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.titleSetter.setValue(3);
        fragmentSettingsBinding.cdManageTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new IntermidiateShopList(2))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
            }
        });

        fragmentSettingsBinding.cdAddLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return fragmentSettingsBinding.getRoot();

    }
}
