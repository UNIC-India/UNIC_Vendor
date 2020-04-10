package com.unic.unic_vendor_final_1.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unic.unic_vendor_final_1.datamodels.Shop;

public class AddNewShopViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();
    private MutableLiveData<Integer> status = new MutableLiveData<>();

}
