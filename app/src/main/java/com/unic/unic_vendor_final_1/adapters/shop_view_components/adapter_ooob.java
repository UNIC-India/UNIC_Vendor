package com.unic.unic_vendor_final_1.adapters.shop_view_components;

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

import java.util.List;
import java.util.Map;

public class adapter_ooob extends RecyclerView.Adapter<adapter_ooob.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> products;
    private List<String> categories;

    public adapter_ooob(Context context){
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategoryName;
        ImageView ivProductImage;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivProductImage = itemView.findViewById(R.id.ivCategItem);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_product_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvCategoryName.setText(categories.get(position));

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
    public void setCategories(List<String> categories){
        this.categories = categories;
    }


}
