package com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

import java.util.List;

public class CategoriesCompaniesAdapter extends RecyclerView.Adapter<CategoriesCompaniesAdapter.ViewHolder> {

    private List<String> subKeys;
    private String key;
    private int type;

    private SetStructureViewModel setStructureViewModel;

    private Context context;

    public CategoriesCompaniesAdapter(Context context, int type){
        this.context = context;
        this.type = type;
        setStructureViewModel = new ViewModelProvider((FragmentActivity)context).get(SetStructureViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.master_layout_categories_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSubKey.setText(subKeys.get(position).equals("null") ?"Others":subKeys.get(position));
        holder.cvSubKey.setOnClickListener(v -> {
            if(type==0)
                setStructureViewModel.searchProductsByCategoryRefineSubcategory(key,subKeys.get(position));
            if(type==1)
                setStructureViewModel.searchProductsByCompanyRefineCategory(key,subKeys.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return subKeys==null?0:subKeys.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubKey;
        CardView cvSubKey;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvSubKey = itemView.findViewById(R.id.tvCategory);
            cvSubKey = itemView.findViewById(R.id.master_category_card_view);
        }
    }

    public void setSubKeys(List<String> subKeys) {
        this.subKeys = subKeys;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
