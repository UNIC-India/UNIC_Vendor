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

public class MasterCompaniesAdapter extends RecyclerView.Adapter<MasterCompaniesAdapter.ViewHolder> {

    private Context mContext;

    private List<Map<String,Object>> products;
    private List<String> companySet;


    public MasterCompaniesAdapter(Context context){
        this.mContext = context;
        companySet=new ArrayList<>();
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
    public MasterCompaniesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.master_layout_categories_item,parent,false);
        return new MasterCompaniesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterCompaniesAdapter.ViewHolder holder, int position) {
        holder.tvCategory.setText(companySet.get(position));
    }

    @Override
    public int getItemCount() {
        return companySet.size();
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
        companySet.clear();
        setCompanySet(products);
    }



    public void setCompanySet(List<Map<String,Object>> products){
        if(!(products==null)) {
            for (Map<String, Object> i : products)
                if (!this.companySet.contains(i.get("company").toString()))
                    this.companySet.add(i.get("company").toString());
            Collections.sort(this.companySet);
        }


    }


}