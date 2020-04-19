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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.views.activities.AddNewProduct;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Shop> shops;
    private Context context;
    private int from;

    public ShopAdapter(Context context,int from){
        this.context = context;
        this.from=from;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvShopName,tvShopName2;
        ImageView ivShopPhoto;
        Button Preview,Edit;
        CardView cdShop;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvShopName = itemView.findViewById(R.id.shop_name);
            ivShopPhoto = itemView.findViewById(R.id.shop_photo);
            Preview = itemView.findViewById(R.id.button);
            Edit = itemView.findViewById(R.id.button2);
            tvShopName2=itemView.findViewById(R.id.tvshop);
            cdShop=itemView.findViewById(R.id.cdshop);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(from==0)
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_list_item, parent, false);
        else
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, final int position) {
        if(from==0){
            holder.tvShopName.setText(shops.get(position).getName());
            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, SetShopStructure.class);
                    intent.putExtra("shopId",shops.get(position).getId());
                }
            });

            Glide
                    .with(context)
                    .load(shops.get(position).getImageLink())
                    .into(holder.ivShopPhoto);

            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,SetShopStructure.class);
                    intent.putExtra("shopId",shops.get(position).getId());
                    intent.putExtra("template",Integer.valueOf(0));
                    context.startActivity(intent);
                }
            });
        }
        else{
            holder.tvShopName2.setText(shops.get(position).getName());
            holder.tvShopName2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context, AddNewProduct.class);
                    intent.putExtra("shopId",shops.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return shops==null?0:shops.size();
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}
