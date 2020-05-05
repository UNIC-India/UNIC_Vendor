package com.unic.unic_vendor_final_1.views.helpers;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.OrderItemsAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentOrderItemsBinding;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.datamodels.User;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderItems extends Fragment implements View.OnClickListener {
    public  Order order;
    private User customer;
    private FragmentOrderItemsBinding fragmentOrderItemsBinding;
    private UserShopsViewModel userShopsViewModel;
    OrderItemsAdapter orderItemsAdapter;
    private Context context;

    public OrderItems(Order order,Context context){
        this.order=order;
        this.context=context;
    }

    public OrderItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOrderItemsBinding=FragmentOrderItemsBinding.inflate(getLayoutInflater());
        orderItemsAdapter=new OrderItemsAdapter(getContext());
        orderItemsAdapter.setProducts(order.getItems(),order.getQuantity());
        setStatus(order.getOrderStatus());
        fragmentOrderItemsBinding.tvCustomerName.setText(order.getOwnerId());
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.getCustomerData(order.getOwnerId()).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setCustomer(user);
                fragmentOrderItemsBinding.tvCustomerName.setText(user.getFullName());;
            }
        });
        userShopsViewModel.listenToOrder(order).observe(getActivity(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                setOrder(order);
                setStatus(order.getOrderStatus());

            }
        });
        fragmentOrderItemsBinding.rvOrderItems.setAdapter(orderItemsAdapter);
        fragmentOrderItemsBinding.rvOrderItems.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentOrderItemsBinding.iv1.setOnClickListener(this);
        fragmentOrderItemsBinding.iv2.setOnClickListener(this);
        fragmentOrderItemsBinding.iv3.setOnClickListener(this);
        fragmentOrderItemsBinding.iv4.setOnClickListener(this);


        return fragmentOrderItemsBinding.getRoot();
    }

    public void setStatus(int orderStatus){
        switch(orderStatus){
            case -1:
                fragmentOrderItemsBinding.iv1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
                fragmentOrderItemsBinding.tv1.setText("Rejected");
                fragmentOrderItemsBinding.tv1.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case 0:
                fragmentOrderItemsBinding.iv1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                fragmentOrderItemsBinding.tv1.setText("Pending");
                fragmentOrderItemsBinding.tv1.setTextColor(context.getResources().getColor(R.color.green));

                break;
            case 1:
                fragmentOrderItemsBinding.iv1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv1.setText("Accepted");
                fragmentOrderItemsBinding.tv1.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v1.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                fragmentOrderItemsBinding.tv2.setText("Preparing");
                fragmentOrderItemsBinding.tv2.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 2:
                fragmentOrderItemsBinding.iv1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv1.setText("Accepted");
                fragmentOrderItemsBinding.tv1.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v1.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv2.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv2.setText("Prepared");
                fragmentOrderItemsBinding.tv2.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v2.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                fragmentOrderItemsBinding.tv3.setText("Dispatching");
                fragmentOrderItemsBinding.tv3.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 3:
                fragmentOrderItemsBinding.iv1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv1.setText("Accepted");
                fragmentOrderItemsBinding.tv1.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v1.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv2.setText("Prepared");
                fragmentOrderItemsBinding.tv2.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v2.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv3.setText("Dispatched");
                fragmentOrderItemsBinding.tv3.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v3.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv4.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                fragmentOrderItemsBinding.tv4.setText("Delivering");
                fragmentOrderItemsBinding.tv4.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 4:
                fragmentOrderItemsBinding.iv1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv1.setText("Accepted");
                fragmentOrderItemsBinding.tv1.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v1.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv2.setText("Prepared");
                fragmentOrderItemsBinding.tv2.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v2.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv3.setText("Dispatched");
                fragmentOrderItemsBinding.tv3.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v3.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv4.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv4.setText("Delivered");
                fragmentOrderItemsBinding.tv4.setTextColor(context.getResources().getColor(R.color.green2));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv1:
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                userShopsViewModel.setOrderStatus(order,1);
                break;
            case R.id.iv2:
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                fragmentOrderItemsBinding.iv2.setEnabled(false);
                userShopsViewModel.setOrderStatus(order,2);
                break;
            case R.id.iv3:
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                fragmentOrderItemsBinding.iv2.setEnabled(false);
                fragmentOrderItemsBinding.iv3.setEnabled(false);
                userShopsViewModel.setOrderStatus(order,3);
                break;
            case R.id.iv4:
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                fragmentOrderItemsBinding.iv2.setEnabled(false);
                fragmentOrderItemsBinding.iv3.setEnabled(false);
                fragmentOrderItemsBinding.iv4.setEnabled(false);
                userShopsViewModel.setOrderStatus(order,4);
                break;
        }
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }
}
