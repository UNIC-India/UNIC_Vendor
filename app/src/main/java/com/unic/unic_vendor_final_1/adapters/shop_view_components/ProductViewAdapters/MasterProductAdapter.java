package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageView addToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.master_product_name);
            ivProductPhoto = itemView.findViewById(R.id.master_product_image);
            tvCompany = itemView.findViewById(R.id.master_product_company);
            tvPrice = itemView.findViewById(R.id.master_product_price);
            tvCategory = itemView.findViewById(R.id.master_product_category);
            cdProduct=itemView.findViewById(R.id.master_product_card);
            tvExtrainfo1=itemView.findViewById(R.id.master_extra_info1);
            tvExtrainfo2=itemView.findViewById(R.id.master_extra_info2);
            addToCart=itemView.findViewById(R.id.master_product_cart);


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
        holder.tvPrice.setText("\u20B9" + products.get(position).get("price").toString());
        holder.tvCompany.setText(products.get(position).get("category").toString());
        holder.tvCategory.setText(products.get(position).get("company").toString());
        holder.tvExtrainfo1.setText((products.get(position).get("extraInfo1")!=null&&!products.get(position).get("extraInfo1").toString().equals("null"))?products.get(position).get("extraInfo1").toString():"");
        holder.tvExtrainfo2.setText((products.get(position).get("extraInfo2")!=null&&!products.get(position).get("extraInfo2").toString().equals("null"))?products.get(position).get("extraInfo2").toString():"");



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
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Product can only be added by a customer.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        if (products == null)
            return 0;
        return products==null?0:products.size();

    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
    }

}
