package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String,Object>> products;
    private ArrayList<String> checkedProducts = new ArrayList<>();

    public ProductListAdapter(Context context){
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        ImageView ivProductPhoto;
        CheckBox cbCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCheck = itemView.findViewById(R.id.product_checkbox);
            tvProductName = itemView.findViewById(R.id.product_name);;
            ivProductPhoto = itemView.findViewById(R.id.product_image_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_details_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvProductName.setText(products.get(position).get("name").toString());
        Glide
                .with(mContext)
                .load(products.get(position).get("imageId").toString())
                .into(holder.ivProductPhoto);

        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                    checkedProducts.add(products.get(position).get("id").toString());
                else
                    checkedProducts.remove(products.get(position).get("id").toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }

    public ArrayList<String> returnSelectedProducts(){
        return checkedProducts;
    }
}
