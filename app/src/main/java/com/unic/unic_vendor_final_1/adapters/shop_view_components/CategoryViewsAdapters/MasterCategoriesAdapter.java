package com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MasterCategoriesAdapter extends RecyclerView.Adapter<MasterCategoriesAdapter.ViewHolder> {

    private Context mContext;

    private List<Map<String,Object>> products;
    private List<String> categories;



    public MasterCategoriesAdapter(Context context){
        this.mContext = context;
        categories=new ArrayList<>();

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
    public MasterCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.master_layout_categories_item,parent,false);
        return new MasterCategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterCategoriesAdapter.ViewHolder holder, int position) {

        holder.tvCategory.setText(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<String> categories){
       this.categories = categories;
    }

}