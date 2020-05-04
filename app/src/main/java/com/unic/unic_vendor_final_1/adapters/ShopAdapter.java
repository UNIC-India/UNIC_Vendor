package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Shop> shops;
    private Context context;

    public ShopAdapter(Context context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName;
        ImageView ivShopPhoto;
        Button btnPreview,btnEdit;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.shop_name);
            ivShopPhoto = itemView.findViewById(R.id.shop_photo);
            btnPreview = itemView.findViewById(R.id.btn_preview_shop);
            btnEdit = itemView.findViewById(R.id.btn_edit_shop);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, final int position) {

        holder.tvShopName.setText(shops.get(position).getName());

        Glide
                .with(context)
                .load(shops.get(position).getImageLink())
                .into(holder.ivShopPhoto);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SetShopStructure.class);
                intent.putExtra("shopId",shops.get(position).getId());
                intent.putExtra("template",Integer.valueOf(0));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return shops==null?0:shops.size();
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}
