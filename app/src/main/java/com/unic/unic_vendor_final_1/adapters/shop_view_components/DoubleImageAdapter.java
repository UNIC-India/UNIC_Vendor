package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubleImageAdapter extends RecyclerView.Adapter<DoubleImageAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> products;

    public DoubleImageAdapter(Context context){
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName;
        ImageView ivProductImage;
        TextView tvProductPrice;
        Button addToCart;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvProductName = itemView.findViewById(R.id.double_image_name);
            ivProductImage = itemView.findViewById(R.id.double_image_view);
            tvProductPrice = itemView.findViewById(R.id.double_image_price);
            addToCart = itemView.findViewById(R.id.double_image_cart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_image_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvProductName.setText(products.get(position).get("name").toString());
        holder.tvProductPrice.setText(products.get(position).get("price").toString());
        Glide
                .with(mContext)
                .load(products.get(position).get("imageId").toString())
                .into(holder.ivProductImage);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }

}
