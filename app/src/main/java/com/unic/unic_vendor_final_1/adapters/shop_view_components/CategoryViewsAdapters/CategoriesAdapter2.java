package com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;

import java.util.List;
import java.util.Map;

public class CategoriesAdapter2 extends RecyclerView.Adapter<CategoriesAdapter2.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> categories;
    private List<Map<String,Object>> products;
    int demo=0;
    int width=0;

    public CategoriesAdapter2(Context context){
        this.mContext = context;
    }
    public CategoriesAdapter2(int demo){

        this.demo=demo;
    }
    public CategoriesAdapter2(Context context, int width){
        this.mContext = context;
        this.width=width;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory;
        CardView cdCat;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            cdCat=itemView.findViewById(R.id.cdCat);

        }
    }

    @NonNull
    @Override
    public CategoriesAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item2,parent,false);
        return new CategoriesAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter2.ViewHolder holder, int position) {

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