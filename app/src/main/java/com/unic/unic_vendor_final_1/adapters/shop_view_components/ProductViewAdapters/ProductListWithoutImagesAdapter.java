package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;

import java.util.List;
import java.util.Map;

public class ProductListWithoutImagesAdapter extends RecyclerView.Adapter<ProductListWithoutImagesAdapter.ViewHolder> {

    int demo=0;
    private Context mContext;
    private List<Map<String,Object>> products;

    public ProductListWithoutImagesAdapter(Context context){
        this.mContext = context;
    }
    public ProductListWithoutImagesAdapter(int demo){
        this.demo=demo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_without_images_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(demo==0){
            holder.tvProductName.setText(products.get(position).get("name").toString());
            holder.tvProductPrice.setText("\u20B9"+products.get(position).get("price").toString());
            holder.tvCategory.setText(products.get(position).get("category").toString());
            holder.tvCompany.setText(products.get(position).get("company").toString());
            holder.extraInfo2.setVisibility(View.INVISIBLE);


        }
        else{
            holder.tvProductName.setText("Product Name " + (position+1));
            holder.tvProductPrice.setText("\u20B9 2104");
            holder.tvCompany.setText("Company");

        }


    }

    @Override
    public int getItemCount() {
        if(demo==0)
            return products==null?0:products.size();
        else
            return 3;
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName,tvProductPrice,tvCompany,tvCategory,extraInfo2;
        Button addToCart;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvProductName = itemView.findViewById(R.id.product_name);
            tvProductPrice = itemView.findViewById(R.id.tvPrice);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            tvCompany=itemView.findViewById(R.id.company);
            tvCategory = itemView.findViewById(R.id.category);
            extraInfo2 = itemView.findViewById(R.id.textView4);
        }
    }

}