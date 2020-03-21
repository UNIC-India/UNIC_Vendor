package com.unic.unic_vendor_final_1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Shop;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Shop> shops;

    public ShopAdapter(){}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName;
        ImageView ivShopPhoto,ivPreview,ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}
