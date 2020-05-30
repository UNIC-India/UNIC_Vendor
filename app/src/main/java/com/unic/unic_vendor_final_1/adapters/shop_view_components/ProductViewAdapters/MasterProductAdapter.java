package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

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

public class MasterProductAdapter extends RecyclerView.Adapter<MasterProductAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String,Object>> products = new ArrayList<>();



    public MasterProductAdapter(Context context){
        this.mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName,tvCompany,tvPrice,tvCategory;
        ImageView ivProductPhoto;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.product_name);
            ivProductPhoto = itemView.findViewById(R.id.product_image);
            tvCompany=itemView.findViewById(R.id.product_company_name);
            tvPrice=itemView.findViewById(R.id.product_price);
            tvCategory=itemView.findViewById(R.id.tvCategory);
        }
    }

    @NonNull
    @Override
    public MasterProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.master_layout_product_item,parent,false);
        return new MasterProductAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MasterProductAdapter.ViewHolder holder, final int position) {


            holder.tvProductName.setText(products.get(position).get("name").toString());
            holder.tvPrice.setText("Rs "+products.get(position).get("price").toString());
            holder.tvCompany.setText(products.get(position).get("category").toString());
            holder.tvCategory.setText(products.get(position).get("company").toString());

            if(((products.get(position).get("imageid").toString().length())!=1)) {
                holder.ivProductPhoto.setVisibility(View.VISIBLE);
                Glide
                        .with(mContext)
                        .load(products.get(position).get("imageid").toString())
                        .into(holder.ivProductPhoto);
            }
            else
                holder.ivProductPhoto.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return products.size();

    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }

}