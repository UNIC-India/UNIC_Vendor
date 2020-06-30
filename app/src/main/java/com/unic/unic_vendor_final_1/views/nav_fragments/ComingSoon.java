package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentComingSoonBinding;
import com.unic.unic_vendor_final_1.datamodels.User;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComingSoon extends Fragment {
    private FragmentComingSoonBinding fragmentComingSoonBinding;
    UserShopsViewModel userShopsViewModel;

    public ComingSoon() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.titleSetter.setValue(7);
        return inflater.inflate(R.layout.fragment_coming_soon, container, false);
    }
}
