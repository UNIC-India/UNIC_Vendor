package com.unic.unic_vendor_final_1.views.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    AlertDialog.Builder builder;

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
        fragmentOrderItemsBinding.tvTotalAmount.setText(order.getTotal()+"");
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.getCustomerData(order.getOwnerId()).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setCustomer(user);
                fragmentOrderItemsBinding.tvCustomerName.setText(user.getFullName());
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
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                fragmentOrderItemsBinding.iv2.setEnabled(false);
                fragmentOrderItemsBinding.iv3.setEnabled(false);
                fragmentOrderItemsBinding.iv4.setEnabled(false);
                fragmentOrderItemsBinding.textView5.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                fragmentOrderItemsBinding.textView5.setText("Order rejected at"+order.getUpdateTime().toString().substring(11,16)+" on "+order.getUpdateTime().toString().substring(8,10)+" "+order.getUpdateTime().toString().substring(4,7)+order.getUpdateTime().toString().substring(29,34));

                break;
            case 0:
                fragmentOrderItemsBinding.iv1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.yellow)));
                fragmentOrderItemsBinding.tv1.setText("Pending");
                fragmentOrderItemsBinding.tv1.setTextColor(context.getResources().getColor(R.color.yellow));
                fragmentOrderItemsBinding.textView5.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                fragmentOrderItemsBinding.textView5.setText("Order pending from"+order.getUpdateTime().toString().substring(11,16)+" on "+order.getUpdateTime().toString().substring(8,10)+" "+order.getUpdateTime().toString().substring(4,7)+order.getUpdateTime().toString().substring(29,34));


                break;
            case 1:
                fragmentOrderItemsBinding.iv1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                fragmentOrderItemsBinding.tv1.setText("Accepted");
                fragmentOrderItemsBinding.tv1.setTextColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.v1.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.iv2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.yellow)));
                fragmentOrderItemsBinding.tv2.setText("Preparing");
                fragmentOrderItemsBinding.tv2.setTextColor(context.getResources().getColor(R.color.yellow));
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                fragmentOrderItemsBinding.textView5.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.textView5.setText("Order accepted at "+order.getUpdateTime().toString().substring(11,16)+" on "+order.getUpdateTime().toString().substring(8,10)+" "+order.getUpdateTime().toString().substring(4,7)+order.getUpdateTime().toString().substring(29,34));
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
                fragmentOrderItemsBinding.iv3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.yellow)));
                fragmentOrderItemsBinding.tv3.setText("Dispatching");
                fragmentOrderItemsBinding.tv3.setTextColor(context.getResources().getColor(R.color.yellow));
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                fragmentOrderItemsBinding.iv2.setEnabled(false);
                fragmentOrderItemsBinding.textView5.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.textView5.setText("Order prepared at "+order.getUpdateTime().toString().substring(11,16)+" on "+order.getUpdateTime().toString().substring(8,10)+" "+order.getUpdateTime().toString().substring(4,7)+order.getUpdateTime().toString().substring(29,34));

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
                fragmentOrderItemsBinding.iv4.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.yellow)));
                fragmentOrderItemsBinding.tv4.setText("Delivering");
                fragmentOrderItemsBinding.tv4.setTextColor(context.getResources().getColor(R.color.yellow));
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                fragmentOrderItemsBinding.iv2.setEnabled(false);
                fragmentOrderItemsBinding.iv3.setEnabled(false);
                fragmentOrderItemsBinding.textView5.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.textView5.setText("Order Dispatched at "+order.getUpdateTime().toString().substring(11,16)+" on "+order.getUpdateTime().toString().substring(8,10)+" "+order.getUpdateTime().toString().substring(4,7)+order.getUpdateTime().toString().substring(29,34));
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
                fragmentOrderItemsBinding.iv1.setEnabled(false);
                fragmentOrderItemsBinding.iv2.setEnabled(false);
                fragmentOrderItemsBinding.iv3.setEnabled(false);
                fragmentOrderItemsBinding.iv4.setEnabled(false);
                fragmentOrderItemsBinding.textView5.setBackgroundColor(context.getResources().getColor(R.color.green2));
                fragmentOrderItemsBinding.textView5.setText("Order Delivered at "+order.getUpdateTime().toString().substring(11,16)+" on "+order.getUpdateTime().toString().substring(8,10)+" "+order.getUpdateTime().toString().substring(4,7)+order.getUpdateTime().toString().substring(29,34));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv1:
                builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Status Update");
                builder.setMessage("Change order status to Accepted?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentOrderItemsBinding.iv1.setEnabled(false);
                        userShopsViewModel.setOrderStatus(order.getId(),1);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case R.id.iv2:
                builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Status Update");
                builder.setMessage("Change order status to Prepared?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentOrderItemsBinding.iv1.setEnabled(false);
                        fragmentOrderItemsBinding.iv2.setEnabled(false);
                        userShopsViewModel.setOrderStatus(order.getId(),2);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;

            case R.id.iv3:
                builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Status Update");
                builder.setMessage("Change order status to Dispatched?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentOrderItemsBinding.iv1.setEnabled(false);
                        fragmentOrderItemsBinding.iv2.setEnabled(false);
                        fragmentOrderItemsBinding.iv3.setEnabled(false);
                        userShopsViewModel.setOrderStatus(order.getId(),3);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                break;
            case R.id.iv4:
                builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Status Update");
                builder.setMessage("Change order status to Delivered?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentOrderItemsBinding.iv1.setEnabled(false);
                        fragmentOrderItemsBinding.iv2.setEnabled(false);
                        fragmentOrderItemsBinding.iv3.setEnabled(false);
                        fragmentOrderItemsBinding.iv4.setEnabled(false);
                        userShopsViewModel.setOrderStatus(order.getId(),4);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

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
