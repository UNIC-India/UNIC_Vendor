package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Shop;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Shop> shops;
    private Context context;

    public ShopAdapter(Context context){
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName;
        ImageView ivShopPhoto,ivPreview,ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.shop_name);
            ivShopPhoto = itemView.findViewById(R.id.shop_photo);
            ivPreview = itemView.findViewById(R.id.btn_preview_shop);
            ivEdit = itemView.findViewById(R.id.btn_edit_shop);
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

        holder.tvShopName.setText(shops.get(position).getName());

        Glide
                .with(context)
                .load(shops.get(position).getImageLink())
                .into(holder.ivShopPhoto);

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}
