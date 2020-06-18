package com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters;

import android.content.Context;
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

import java.util.List;
import java.util.Map;

public class CategoriesAdapter extends RecyclerView.Adapter<com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> categories;
    private List<Map<String,Object>> products;
    int demo=0;

    public CategoriesAdapter(Context context){
        this.mContext = context;
    }
    public CategoriesAdapter(int demo){

        this.demo=demo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);

        }
    }

    @NonNull
    @Override
    public com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter.ViewHolder holder, int position) {
        if(demo==0){
            holder.tvCategory.setText((categories.get(position).get("cname")!=null?categories.get(position).get("cname").toString():categories.get(position).get("compname").toString()));
        }
        else{
            holder.tvCategory.setText("Category"+position);

        }


    }

    @Override
    public int getItemCount() {
        if(demo==0)
            return categories==null?0:categories.size();
        else
            return 9;
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }
    public void setCategories(List<Map<String,Object>> categories){
        this.categories=categories;

    }

}