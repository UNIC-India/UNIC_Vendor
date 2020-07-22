package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.databinding.FragmentOrderDetailsBinding;
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetails extends Fragment {
    Order order;
    FragmentOrderDetailsBinding fragmentOrderDetailsBinding;


    public OrderDetails() {
        // Required empty public constructor
    }
    public OrderDetails(Order order){
         this.order=order;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentOrderDetailsBinding = FragmentOrderDetailsBinding.inflate(inflater,container,false);

        fragmentOrderDetailsBinding.tvPhone.setText(order.getPhoneNo());
        if(order.isReported())
            fragmentOrderDetailsBinding.reportUser.setText("Reported");
        else {
            fragmentOrderDetailsBinding.reportUser.setText("Report User?");
            fragmentOrderDetailsBinding.reportUser.setOnClickListener(v -> {
                fragmentOrderDetailsBinding.reportUser.setEnabled(false);
                UserShopsViewModel userShopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
                userShopsViewModel.reportUser(order.getShopId(),order.getOwnerId(),order.getId());
            });
        }
        fragmentOrderDetailsBinding.tvGST.setText("Not Specified");
        if(order.getPickUp()==1){
            fragmentOrderDetailsBinding.tvOrgName.setText("Personal");

            fragmentOrderDetailsBinding.tvCity.setText("Pick Up");
            fragmentOrderDetailsBinding.textCity.setText("Mode:");
            fragmentOrderDetailsBinding.tvPincode.setVisibility(View.GONE);
            fragmentOrderDetailsBinding.textPincode.setVisibility(View.GONE);
            fragmentOrderDetailsBinding.textAddress.setVisibility(View.GONE);
            fragmentOrderDetailsBinding.tvAddress.setVisibility(View.GONE);
            fragmentOrderDetailsBinding.tvAddress.setText("Pick Up");
        }
        else{
            fragmentOrderDetailsBinding.tvOrgName.setText(order.getAddress().getOrgName().length()<=1?"Not specified":order.getAddress().getOrgName());
            fragmentOrderDetailsBinding.tvCity.setText(order.getAddress().getCity()+", ");
            fragmentOrderDetailsBinding.tvPincode.setText(order.getAddress().getPincode());

            fragmentOrderDetailsBinding.tvAddress.setText(order.getAddress().getAddressLine1()+'\n'+order.getAddress().getAddressLine2()+'\n'+order.getAddress().getLandmark());
        }


        return fragmentOrderDetailsBinding.getRoot();
    }
}
