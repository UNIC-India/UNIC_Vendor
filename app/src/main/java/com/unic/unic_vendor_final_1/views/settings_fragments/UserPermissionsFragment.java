package com.unic.unic_vendor_final_1.views.settings_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentUserPermissionsBinding;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.helpers.UserRequests;

public class UserPermissionsFragment extends Fragment {

    private String shopId,name;
    private boolean isPrivate;
    private FragmentUserPermissionsBinding userPermissionsBinding;
    private UserShopsViewModel userShopsViewModel;

    public UserPermissionsFragment() {
        // Required empty public constructor
    }

    public UserPermissionsFragment(String shopId,String name,boolean isPrivate) {
        this.shopId = shopId;
        this.name = name;
        this.isPrivate = isPrivate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        userPermissionsBinding = FragmentUserPermissionsBinding.inflate(inflater,container,false);

        userPermissionsBinding.cdApprovedUsers.setVisibility(View.GONE);
        userPermissionsBinding.cdPendingRequests.setVisibility(View.GONE);

        userPermissionsBinding.tvUserPermissionsHeader.setText(name);

        userShopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.getUserPermissions(shopId);

        userPermissionsBinding.shopPrivacySpinner.setChecked(isPrivate);
        userPermissionsBinding.shopPrivacySpinner.setText(isPrivate?"Private":"Public");

        userShopsViewModel.getUserRequests().observe(getViewLifecycleOwner(),stringListMap -> {
            if(stringListMap==null){
                //TODO Add a no requests/approved image
                return;
            }

            if(stringListMap.containsKey("pending")&&stringListMap.get("pending").size()>0)
                userPermissionsBinding.cdPendingRequests.setVisibility(View.VISIBLE);

            if(stringListMap.containsKey("approved")&&stringListMap.get("approved").size()>0)
                userPermissionsBinding.cdApprovedUsers.setVisibility(View.VISIBLE);
        });

        userPermissionsBinding.cdPendingRequests.setOnClickListener(v ->
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment,new UserRequests(0,shopId))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()
        );

        userPermissionsBinding.cdApprovedUsers.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new UserRequests(1,shopId))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit()
        );

        userPermissionsBinding.shopPrivacySpinner.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userShopsViewModel.setShopPrivacy(shopId,isChecked);
            userPermissionsBinding.shopPrivacySpinner.setText(isChecked?"Private":"Public");
        });

        return userPermissionsBinding.getRoot();
    }
}