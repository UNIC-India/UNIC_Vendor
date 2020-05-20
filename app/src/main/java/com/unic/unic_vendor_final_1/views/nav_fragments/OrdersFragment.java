package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.AllOrdersAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentMyOrdersBinding;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

import java.util.List;


public class OrdersFragment extends Fragment {
    private UserShopsViewModel userShopsViewModel;
    private FragmentMyOrdersBinding myOrdersBinding;
    private AllOrdersAdapter allOrdersAdapter;

    RecyclerView rvOrders;


    public OrdersFragment() {
        // Required empty public constructor
    }
    int i=0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_my_orders, container, false);
        rvOrders=view.findViewById(R.id.rvAllOrders);
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        allOrdersAdapter=new AllOrdersAdapter(getActivity());
        rvOrders.setAdapter(allOrdersAdapter);
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));

        userShopsViewModel.getOrders().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {

                        allOrdersAdapter.setShops(orders);


            }
        });



        // Inflate the layout for this fragment
        return view;
    }


}
