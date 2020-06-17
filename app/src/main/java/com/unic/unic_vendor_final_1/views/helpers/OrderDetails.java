package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.databinding.FragmentOrderDetailsBinding;
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Order;

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
         new FirebaseRepository().db.collection("users").document(order.getOwnerId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fragmentOrderDetailsBinding.tvPhone.setText(documentSnapshot.get("phoneNo").toString());
            }
        });
        fragmentOrderDetailsBinding=FragmentOrderDetailsBinding.inflate(getLayoutInflater());
        fragmentOrderDetailsBinding.tvGST.setText("Not Specified");
        if(order.getPickUp()==1){
            fragmentOrderDetailsBinding.tvOrgName.setText("Personal/Pick Up");
            fragmentOrderDetailsBinding.tvCity.setText("Pick Up");
            fragmentOrderDetailsBinding.tvPincode.setText("Pick Up");

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
