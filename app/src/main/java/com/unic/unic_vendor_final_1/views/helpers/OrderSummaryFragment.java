package com.unic.unic_vendor_final_1.views.helpers;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.OrderReportItemsAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentOrderSummaryBinding;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSummaryFragment extends Fragment {


    private Map<String,Object> data;
    private FragmentOrderSummaryBinding orderSummaryBinding;
    private UserShopsViewModel userShopsViewModel;
    private List<Order> orders;
    private Map<String,Integer> orderTypes;

    public OrderSummaryFragment() {

    }

    public OrderSummaryFragment(Map<String,Object> data) {
        this.data = data;
    }

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, @NotNull View view,
                                   RecyclerView parent, @NotNull RecyclerView.State state) {

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = 0;
            } else {
                outRect.top = space;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        orderSummaryBinding = FragmentOrderSummaryBinding.inflate(inflater,container,false);
        userShopsViewModel = new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);

        if((Boolean)data.get("allShops")) {
            if ((Boolean) data.get("allTypes"))
                userShopsViewModel.getAllOrdersReportByDate(((Date) data.get("startDate")), ((Date) data.get("endDate")));
            else {
                userShopsViewModel.getAllOrdersReportByDateAndStatus(((int) data.get("type")), ((Date) data.get("startDate")), ((Date) data.get("endDate")));
            }
        }
        else {

            if ((Boolean) data.get("allTypes"))
                userShopsViewModel.getOrderReportByDate(data.get("shopId").toString(), ((Date) data.get("startDate")), ((Date) data.get("endDate")));
            else {
                userShopsViewModel.getOrderReportByDateAndStatus(data.get("shopId").toString(), ((int) data.get("type")), ((Date) data.get("startDate")), ((Date) data.get("endDate")));
            }
        }

        userShopsViewModel.getOrderReport().observe(getViewLifecycleOwner(),this::setOrders);

        return orderSummaryBinding.getRoot();
    }

    public void setOrders(List<Order> orders) {

        if (orders == null)
            return;

        if(orders.size()==0) {
            //TODO

            orderSummaryBinding.orderSummaryItemsCard.setVisibility(View.GONE);
            orderSummaryBinding.orderSummaryCard.setVisibility(View.GONE);
            orderSummaryBinding.tvNoOrders.setVisibility(View.VISIBLE);
            orderSummaryBinding.ivNoOrder.setVisibility(View.VISIBLE);

            return;
        }

        orderSummaryBinding.orderSummaryItemsCard.setVisibility(View.VISIBLE);
        orderSummaryBinding.orderSummaryCard.setVisibility(View.VISIBLE);
        orderSummaryBinding.tvNoOrders.setVisibility(View.GONE);
        orderSummaryBinding.ivNoOrder.setVisibility(View.GONE);

        this.orders = orders;
        orderTypes = new HashMap<>();
        int[] numArr = {0,0,0,0};
        orderSummaryBinding.tvTotalOrders.setText(Integer.valueOf(orders.size()).toString());
        Double total = orders.stream().mapToDouble(order -> order.getTotal()).sum();

        orderSummaryBinding.tvTotalSales.setText("\u20b9 " + Double.valueOf(total).toString());

        if(!((Boolean) data.get("allTypes"))) {
            orderSummaryBinding.orderSummaryTypes.setVisibility(View.GONE);
        }
        else {

            orderSummaryBinding.orderSummaryTypes.setVisibility(View.VISIBLE);

            orders.forEach(order -> {
                if(order.getOrderStatus() == -1)
                    numArr[0]++;
                if(order.getOrderStatus() == 0)
                    numArr[1]++;
                if(order.getOrderStatus() == 1 || order.getOrderStatus() == 2 || order.getOrderStatus() == 3 || order.getOrderStatus() == 5)
                    numArr[2]++;
                if(order.getOrderStatus() == 4)
                    numArr[3]++;
            });

            orderSummaryBinding.tvRejectedOrders.setText(Integer.valueOf(numArr[0]).toString());
            orderSummaryBinding.tvPendingOrders.setText(Integer.valueOf(numArr[1]).toString());
            orderSummaryBinding.tvApprovedOrders.setText(Integer.valueOf(numArr[2]).toString());
            orderSummaryBinding.tvDeliveredOrders.setText(Integer.valueOf(numArr[3]).toString());

        }

        Map<String,Map<String,Object>> products = new HashMap<>();

        orders.forEach(order ->
                order.getItems().forEach(product -> {
                    if(products.containsKey(product.get("firestoreId").toString())) {
                        Map<String,Object> oldProduct = products.get(product.get("firestoreId").toString());
                        oldProduct.put("orderQuantity",Integer.parseInt(oldProduct.get("orderQuantity").toString()) + Integer.parseInt(product.get("orderQuantity").toString()));
                        oldProduct.put("subtotal",Double.parseDouble(oldProduct.get("subtotal").toString())+Double.parseDouble(product.get("price").toString())*Integer.parseInt(product.get("orderQuantity").toString()));
                        oldProduct.put("price",product.get("price").toString());
                        products.put(product.get("firestoreId").toString(),oldProduct);
                    }

                    else {
                        product.put("subtotal",Double.parseDouble(product.get("price").toString())*Integer.parseInt(product.get("orderQuantity").toString()));
                        products.put(product.get("firestoreId").toString(), product);
                    }
        }));

        List<Map<String,Object>> allProducts = new ArrayList<>();
        products.forEach((s, map) -> allProducts.add(map));

        OrderReportItemsAdapter orderReportItemsAdapter = new OrderReportItemsAdapter(getContext());
        orderReportItemsAdapter.setProducts(allProducts);

        orderSummaryBinding.rvOrderSummaryItems.setLayoutManager(new LinearLayoutManager(getContext()));
        orderSummaryBinding.rvOrderSummaryItems.setAdapter(orderReportItemsAdapter);
        orderSummaryBinding.rvOrderSummaryItems.addItemDecoration(new SpacesItemDecoration((int)dpToPx(2)));

        orderReportItemsAdapter.notifyDataSetChanged();

    }

    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}