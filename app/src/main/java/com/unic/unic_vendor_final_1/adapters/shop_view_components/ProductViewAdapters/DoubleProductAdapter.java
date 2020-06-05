package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;

import java.util.List;
import java.util.Map;

public class DoubleProductAdapter extends RecyclerView.Adapter<DoubleProductAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> products;
    int demo=0;

    public DoubleProductAdapter(Context context){
        this.mContext = context;
    }
    public DoubleProductAdapter(int demo){

        this.demo=demo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName;
        ImageView ivProductImage;
        TextView tvProductPrice,tvCompany,tvDiscount;
        Button addToCart;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvProductName = itemView.findViewById(R.id.double_product_name);
            ivProductImage = itemView.findViewById(R.id.double_product_view);
            tvProductPrice = itemView.findViewById(R.id.double_product_price);
            addToCart = itemView.findViewById(R.id.double_product_cart);
            tvCompany=itemView.findViewById(R.id.tvCompany);
            tvDiscount=itemView.findViewById(R.id.tvDiscount);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_product_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(demo==0){
          holder.tvProductName.setText(products.get(position).get("name").toString());
          if(products.get(position).get("name").toString().length()>20) {
              holder.tvProductName.setTextSize(12);
              holder.tvProductName.setMaxLines(2);
              holder.ivProductImage.getLayoutParams().height=330;
          }
          holder.tvProductPrice.setText("Rs "+products.get(position).get("price").toString());
          holder.tvCompany.setText(products.get(position).get("company").toString());
          holder.tvDiscount.setText(products.get(position).get("discount")!=null?products.get(position).get("discount").toString()+" OFF":"20% OFF");
          Glide
                  .with(mContext)
                  .load(products.get(position).get("imageId"))
                  .into(holder.ivProductImage);
           holder.tvProductName.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   ((AppCompatActivity)mContext).getSupportFragmentManager()
                           .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                           .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                           .addToBackStack(null)
                           .commit();
               }
           });
           holder.tvCompany.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   ((AppCompatActivity)mContext).getSupportFragmentManager()
                           .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                           .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                           .addToBackStack(null)
                           .commit();
               }
           });
           holder.ivProductImage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   ((AppCompatActivity)mContext).getSupportFragmentManager()
                           .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                           .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                           .addToBackStack(null)
                           .commit();
               }
           });
           holder.tvProductPrice.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   ((AppCompatActivity)mContext).getSupportFragmentManager()
                           .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                           .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                           .addToBackStack(null)
                           .commit();
               }
           });
       }
       else{
           holder.tvProductName.setText("Product Name"+position);
           holder.tvProductPrice.setText("Rs:2104");
           holder.ivProductImage.setImageResource(R.drawable.demo_product);
       }



    }

    @Override
    public int getItemCount() {
        if(demo==0)
            if(products!=null)
                return products.size();
            else
                return 0;
        else
            return 4;
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }

}