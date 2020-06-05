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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MasterProductAdapter extends RecyclerView.Adapter<MasterProductAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> products = new ArrayList<>();


    public MasterProductAdapter(Context context) {
        this.mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvCompany, tvPrice, tvCategory,tvExtrainfo1,tvExtrainfo2;
        ImageView ivProductPhoto;
        CardView cdProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.product_name);
            ivProductPhoto = itemView.findViewById(R.id.product_image);
            tvCompany = itemView.findViewById(R.id.product_company_name);
            tvPrice = itemView.findViewById(R.id.product_price);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            cdProduct=itemView.findViewById(R.id.cdProduct);
            tvExtrainfo1=itemView.findViewById(R.id.tvExtrainfo1);
            tvExtrainfo2=itemView.findViewById(R.id.tvExtrainfo2);


        }
    }

    @NonNull
    @Override
    public MasterProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.master_layout_product_item, parent, false);
        return new MasterProductAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MasterProductAdapter.ViewHolder holder, final int position) {


        holder.tvProductName.setText(products.get(position).get("name").toString());
        holder.tvPrice.setText("Rs " + products.get(position).get("price").toString());
        holder.tvCompany.setText(products.get(position).get("category").toString());
        holder.tvCategory.setText(products.get(position).get("company").toString());
        holder.tvExtrainfo1.setText(products.get(position).get("ExtraInfo1")!=null?products.get(position).get("ExtraInfo1").toString():"");
        holder.tvExtrainfo2.setText(products.get(position).get("ExtraInfo2")!=null?products.get(position).get("ExtraInfo2").toString():"");



        if (((products.get(position).get("imageId").toString().length()) >= 2)) {
            holder.ivProductPhoto.setVisibility(View.VISIBLE);
            Glide
                    .with(mContext)
                    .load(products.get(position).get("imageId").toString())
                    .into(holder.ivProductPhoto);
        } else
            holder.ivProductPhoto.setVisibility(View.GONE);

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
        holder.ivProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });
        holder.tvCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });
        holder.tvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.cdProduct.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        if (products == null)
            return 0;
        return products.size();

    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
    }

}
