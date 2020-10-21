package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.UserRequestsAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentUserRequestsBinding;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRequests extends Fragment {

    int type;
    UserShopsViewModel userShopsViewModel;
    FragmentUserRequestsBinding userRequestsBinding;
    List<Map<String,String>> users;
    UserRequestsAdapter adapter;
    String shopId;

    public UserRequests() {
        // Required empty public constructor
    }

    public UserRequests(int type,String shopId){
        this.type = type;
        this.shopId = shopId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userRequestsBinding = FragmentUserRequestsBinding.inflate(inflater,container,false);

        adapter = new UserRequestsAdapter(getContext(),type,shopId);
        userRequestsBinding.rvUserNames.setAdapter(adapter);
        userRequestsBinding.rvUserNames.setLayoutManager(new LinearLayoutManager(getContext()));

        userShopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.getUserRequests().observe(getViewLifecycleOwner(),stringListMap -> setUsers(type==0?stringListMap.get("pending"):stringListMap.get("approved")));

        return userRequestsBinding.getRoot();
    }

    public void setUsers(List<Map<String, String>> users) {
        this.users = users;
        adapter.setUsers(users);
        adapter.notifyDataSetChanged();
    }
}