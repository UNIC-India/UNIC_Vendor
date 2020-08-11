package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;


public class AboutUsFragment extends Fragment {

    private UserShopsViewModel userShopsViewModel;


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.titleSetter.setValue(8);
        return inflater.inflate(R.layout.about_us_fragment, container, false);
    }
}