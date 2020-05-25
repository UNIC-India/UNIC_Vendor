package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

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

public class TripleImageAdapter extends RecyclerView.Adapter<TripleImageAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> products;
    int demo=0;

    public TripleImageAdapter(Context context){
        this.mContext = context;
    }
    public TripleImageAdapter(int demo){
        this.demo=demo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvProductName;
        ImageView ivProductImage;
        TextView tvProductPrice,tvCompany;
        ImageView addToCart;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductPhoto);
            addToCart = itemView.findViewById(R.id.ivCart);
            tvCompany=itemView.findViewById(R.id.tvCompany);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.triple_image_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(demo==0){
            holder.tvProductName.setText(products.get(position).get("name").toString());
            holder.tvProductPrice.setText(products.get(position).get("price").toString());
            Glide
                    .with(mContext)
                    .load(products.get(position).get("imageid").toString())
                    .into(holder.ivProductImage);
        }
        else{
            holder.tvProductName.setText("Product Name"+position);
            holder.tvProductPrice.setText("Rs:2104");
            holder.ivProductImage.setImageResource(R.drawable.prouctdemo2);
        }
        }


    @Override
    public int getItemCount() {
        if(demo==0)
        return products.size();
        else
            return 4;
    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
    }

}