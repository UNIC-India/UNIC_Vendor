package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.ProductDetailsAdapter;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;

import java.util.List;
import java.util.Map;

public class DoubleProductAdapter extends RecyclerView.Adapter<DoubleProductAdapter.ViewHolder> {

private Context mContext;
private List<Map<String, Object>> products;
        int demo = 0;

public DoubleProductAdapter(Context context) {
        this.mContext = context;
        }

public DoubleProductAdapter(int demo) {

        this.demo = demo;

    class ProductDetailsListener implements View.OnClickListener {
        private  int position;

        ProductDetailsListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ((AppCompatActivity)mContext).getSupportFragmentManager()
                    .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();
        }
    }

        }

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvProductName;
    ImageView ivProductImage, noImage;
    TextView tvProductPrice, tvCompany, tvDiscount, tvWithoutDiscount, tv_no_image;
    Button addToCart;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvProductName = itemView.findViewById(R.id.double_product_name);
        ivProductImage = itemView.findViewById(R.id.double_product_view);
        tvProductPrice = itemView.findViewById(R.id.double_product_price);
        addToCart = itemView.findViewById(R.id.double_product_cart);
        tvCompany = itemView.findViewById(R.id.tvCompany);
        tvDiscount = itemView.findViewById(R.id.tvDiscount);
        tvWithoutDiscount = itemView.findViewById(R.id.tvWithoutDiscount);
        tv_no_image = itemView.findViewById(R.id.tv_no_image);
        noImage = itemView.findViewById(R.id.no_image);
    }
}

    @NonNull
    @Override
    public DoubleProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_product_view_item, parent, false);
        return new DoubleProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoubleProductAdapter.ViewHolder holder, int position) {
        if (demo == 0) {
            if (products.get(position).get("discount") != null&&Double.parseDouble(products.get(position).get("discount").toString())!=0.0) {
                holder.tvDiscount.setText(products.get(position).get("discount").toString() + "% OFF");
                holder.tvDiscount.setVisibility(View.VISIBLE);
                holder.tvWithoutDiscount.setText("\u20B9" + products.get(position).get("price").toString());
                holder.tvWithoutDiscount.setPaintFlags(holder.tvWithoutDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvProductPrice.setText("\u20B9" + (Double.parseDouble(products.get(position).get("price").toString()) * (100 - Double.parseDouble(products.get(position).get("discount").toString())) / 100) + "");
            } else {
                holder.tvDiscount.setText("");
                holder.tvDiscount.setVisibility(View.GONE);
                holder.tvProductPrice.setText("\u20B9" + products.get(position).get("price").toString());
            }
            holder.tvProductName.setText(products.get(position).get("name").toString());
            if (products.get(position).get("name").toString().length() > 20) {
                holder.tvProductName.setTextSize(12);
                holder.tvProductName.setMaxLines(2);
                holder.ivProductImage.getLayoutParams().height = 330;
            }
            holder.tvCompany.setText(products.get(position).get("company").toString());
            if (products.get(position).get("imageId").toString().length() >= 3) {
                holder.noImage.setVisibility(View.GONE);
                holder.tv_no_image.setVisibility(View.GONE);
                holder.ivProductImage.setVisibility(View.VISIBLE);
                Glide
                        .with(mContext)
                        .load(products.get(position).get("imageId"))
                        .into(holder.ivProductImage);
            } else {
                int p = position;
                holder.ivProductImage.setVisibility(View.GONE);
                holder.noImage.setVisibility(View.VISIBLE);
                holder.tv_no_image.setVisibility(View.VISIBLE);
                holder.tv_no_image.setText(products.get(position).get("name").toString().substring(0, 1).toUpperCase());
                switch (p % 3) {
                    case 0:
                        holder.noImage.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorTertiary)));
                        holder.tv_no_image.setTextColor(mContext.getResources().getColor(R.color.white));

                        break;
                    case 1:
                        holder.noImage.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorSecondary)));
                        holder.tv_no_image.setTextColor(mContext.getResources().getColor(R.color.black));
                        break;

                    case 2:
                        holder.noImage.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorPrimary)));
                        holder.tv_no_image.setTextColor(mContext.getResources().getColor(R.color.white));

                        break;
                }
            }

            holder.tvProductName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader, new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.tv_no_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader, new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.noImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader, new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.tvCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader, new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.ivProductImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader, new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.tvProductPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader, new ProductDescriptionFragment(products.get(position)))
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
        } else {
            holder.tvProductName.setText("Product Name" + position);
            holder.tvProductPrice.setText("\u20B92104");
            holder.tv_no_image.setVisibility(View.GONE);
            holder.noImage.setVisibility(View.GONE);
            holder.ivProductImage.setImageResource(R.drawable.demo_product);
            holder.tvCompany.setText("Company" + position);
            holder.tvDiscount.setText("20% OFF");
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvWithoutDiscount.setText("\u20B92524.8");
            holder.tvWithoutDiscount.setPaintFlags(holder.tvWithoutDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


    }

    @Override
    public int getItemCount() {
        if (demo == 0)
            if (products != null)
                return products==null?0:products.size();
            else
                return 0;
        else
            return 4;
    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
    }
}

