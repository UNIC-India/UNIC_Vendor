package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentOrderDetailsBinding;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
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
        fragmentOrderDetailsBinding.tvOrgName.setText(order.getOrgName());
        fragmentOrderDetailsBinding.tvAddress.setText(order.getAddress());
        fragmentOrderDetailsBinding.tvGST.setText("1234567890");


        return fragmentOrderDetailsBinding.getRoot();
    }
}
