package com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;

import java.util.List;
import java.util.Map;

public class CategorySelectionAdapter extends RecyclerView.Adapter<com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategorySelectionAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> categories;
    private List<Map<String,Object>> checkedCategories;
    private List<Map<String,Object>> products;
    int demo=0;

    public CategorySelectionAdapter(Context context){
        this.mContext = context;
    }
    public CategorySelectionAdapter(int demo){

        this.demo=demo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory;
        CheckBox cbCheck;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            cbCheck=itemView.findViewById(R.id.checkBox);

        }
    }

    @NonNull
    @Override
    public com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategorySelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_selector_view_item,parent,false);
        return new com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategorySelectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategorySelectionAdapter.ViewHolder holder, int position) {
        if(demo==0){
            holder.tvCategory.setText(categories.get(position).get("cname").toString());
            if(checkedCategories.contains(products.get(position)))
                holder.cbCheck.setChecked(true);

            else
                holder.cbCheck.setChecked(false);

            holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                        checkedCategories.add(categories.get(position));
                    else
                        checkedCategories.remove(categories.get(position));
                }
            });

        }
        else{
            holder.tvCategory.setText("Cat1"+position);

        }


    }

    @Override
    public int getItemCount() {
        if(demo==0)
            return categories.size();
        else
            return 6;
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }
    public void setCategories(List<Map<String,Object>> categories){
        this.categories=categories;

    }
    public List<Map<String,Object>> returnSelectedCategories(){
        return checkedCategories;
    }


    public void  setSelectedCategories(List<Map<String,Object>> categories){
        this.checkedCategories = categories;
    }

}
