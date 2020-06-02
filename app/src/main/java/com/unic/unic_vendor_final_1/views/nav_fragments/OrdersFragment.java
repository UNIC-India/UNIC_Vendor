package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
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
    private boolean isFirst = true;
    private boolean isAtBottom = false;
    private DocumentSnapshot lastDoc;
    private Boolean isUpdating=false;
    int updatingPosition=-1;
    int newStatus=0;

    RecyclerView rvOrders;


    public OrdersFragment() {
        // Required empty public constructor
    }
    int i=0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myOrdersBinding=FragmentMyOrdersBinding.inflate(getLayoutInflater(),container,false);
        rvOrders=myOrdersBinding.rvAllOrders;
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        allOrdersAdapter=new AllOrdersAdapter(getActivity(),this);
        rvOrders.setAdapter(allOrdersAdapter);
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));

        myOrdersBinding.orderRefresh.setColorScheme(R.color.colorPrimary,R.color.colorSecondary,R.color.colorTertiary);

        userShopsViewModel.getPaginatedOrders(true,null);

        userShopsViewModel.isOrderUpdating.observe(getViewLifecycleOwner(),aBoolean -> {
            isUpdating=aBoolean;
        if(updatingPosition>=0&&isUpdating==false) {
            allOrdersAdapter.updateOrder(updatingPosition, newStatus);
            updatingPosition = -1;
        }
        });

        userShopsViewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders==null||orders.size()==0){
                myOrdersBinding.ivNoOrder.setVisibility(View.VISIBLE);
                myOrdersBinding.tvNoOrders.setVisibility(View.VISIBLE);
                return;
            }
            myOrdersBinding.ivNoOrder.setVisibility(View.GONE);
            myOrdersBinding.tvNoOrders.setVisibility(View.GONE);
            setOrders(orders);
        });
        myOrdersBinding.orderRefresh.setOnRefreshListener(() -> {

            isFirst = true;
            lastDoc = null;
            userShopsViewModel.getIsFirstOrder().setValue(Boolean.TRUE);
            userShopsViewModel.getLastOrderDoc().setValue(null);
            userShopsViewModel.getOrders().setValue(null);
            userShopsViewModel.getPaginatedOrders(isFirst,lastDoc);

            Handler handler = new Handler();

            handler.postDelayed(() -> myOrdersBinding.orderRefresh.setRefreshing(false),5000);
        });

        myOrdersBinding.rvAllOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE&&!isAtBottom&&recyclerView.canScrollVertically(-1)&&!recyclerView.canScrollVertically(1)){
                    userShopsViewModel.getPaginatedOrders(isFirst,lastDoc);
                    isAtBottom = true;
                }
                else
                    isAtBottom = false;
            }
        });

        userShopsViewModel.getLastOrderDoc().observe(getViewLifecycleOwner(), this::setLastDoc);

        userShopsViewModel.getIsFirstOrder().observe(getViewLifecycleOwner(),this::setFirst);

        // Inflate the layout for this fragment
        return myOrdersBinding.getRoot();
    }

    private void setOrders(List<Order> orders){
        allOrdersAdapter.setOrders(orders);
        allOrdersAdapter.notifyDataSetChanged();
    }

    private void setFirst(boolean first) {
        isFirst = first;
    }

    private void setLastDoc(DocumentSnapshot lastDoc) {
        this.lastDoc = lastDoc;
    }

    public void setOrderStatus(int position,String orderId, int status){
        newStatus=status;
        updatingPosition=position;
        userShopsViewModel.isOrderUpdating.setValue(Boolean.TRUE);
        userShopsViewModel.setOrderStatus(orderId,status);
    }
}
