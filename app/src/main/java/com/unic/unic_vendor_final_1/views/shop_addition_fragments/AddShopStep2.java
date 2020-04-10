package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentAddShopStep1Binding;
import com.unic.unic_vendor_final_1.databinding.FragmentAddShopStep2Binding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.AddNewShopViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopStep2 extends Fragment implements View.OnClickListener{

    private FragmentAddShopStep2Binding addShopStep2Binding;
    private AddNewShopViewModel vm;

    public AddShopStep2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addShopStep2Binding = FragmentAddShopStep2Binding.inflate(inflater,container, false);
        vm = new ViewModelProvider(getActivity()).get(AddNewShopViewModel.class);
        return addShopStep2Binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addShopStep2Binding.shopAddStep3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
