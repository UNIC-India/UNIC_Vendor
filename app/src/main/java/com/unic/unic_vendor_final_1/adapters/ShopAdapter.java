package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.commons.BlurTransformation;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.views.activities.AddNewProduct;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;
import com.unic.unic_vendor_final_1.views.activities.UserHome;
import com.unic.unic_vendor_final_1.views.nav_fragments.MyAppsFragment;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Shop> shops;
    private Context context;
    private int from;

    public ShopAdapter(Context context,int from){
        this.context = context;
        this.from=from;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName,tvShopName2,tvLocality,tvSubscribers,tvProducts,tv_no_image;
        ImageView ivShopPhoto,ivShopBackground,no_image;
        LinearLayout Edit;
        CardView cdShop;
        ImageButton ibDeleteShop;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvShopName = itemView.findViewById(R.id.shop_name);
            ivShopPhoto = itemView.findViewById(R.id.shop_photo);
            ibDeleteShop = itemView.findViewById(R.id.delete_shop);
            ivShopBackground = itemView.findViewById(R.id.shop_background_photo);
            tvLocality=itemView.findViewById(R.id.shop_locality);
            Edit = itemView.findViewById(R.id.edit_shop);
            tvShopName2=itemView.findViewById(R.id.tvshop);
            tvSubscribers=itemView.findViewById(R.id.tvSubscribers);
            tvProducts=itemView.findViewById(R.id.tvProducts);
            cdShop=itemView.findViewById(R.id.cdshop);
            no_image=itemView.findViewById(R.id.no_image);
            tv_no_image=itemView.findViewById(R.id.tv_no_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(from==0)
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_list_item_modified, parent, false);
        else
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, final int position) {

        if(from==0){
            holder.tvShopName.setText(shops.get(position).getName());
            holder.tvLocality.setText(shops.get(position).getLocality());
            holder.tvProducts.setText("Products: "+shops.get(position).getNoOfProducts());
            holder.tvSubscribers.setText("Subscribers: "+shops.get(position).getNoOfSubscribers());
            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, SetShopStructure.class);
                    intent.putExtra("shopId",shops.get(position).getId());
                }
            });

            if(shops.get(position).getImageLink().length()>=3) {
                holder.no_image.setVisibility(View.GONE);
                holder.tv_no_image.setVisibility(View.GONE);
                holder.ivShopPhoto.setVisibility(View.VISIBLE);
                Glide
                        .with(context)
                        .load(shops.get(position).getImageLink())
                        .into(holder.ivShopPhoto);
            }
            else{
                Glide
                        .with(context)
                        .load(shops.get(position).getImageLink())
                        .into(holder.ivShopPhoto);

                Glide
                        .with(context)
                        .load(shops.get(position).getImageLink())
                        .transform(new BlurTransformation(context))
                        .into(holder.ivShopBackground);

                holder.no_image.setVisibility(View.VISIBLE);
                holder.tv_no_image.setVisibility(View.VISIBLE);
                holder.tv_no_image.bringToFront();
                holder.tv_no_image.setText(shops.get(position).getName().substring(0,1).toUpperCase());
                switch (position %3){
                    case 0:
                        holder.no_image.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorTertiary)));
                        holder.tv_no_image.setTextColor(context.getResources().getColor(R.color.white));

                        break;
                    case 1:
                        holder.no_image.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorSecondary)));
                        holder.tv_no_image.setTextColor(context.getResources().getColor(R.color.black));
                        break;

                    case 2:
                        holder.no_image.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
                        holder.tv_no_image.setTextColor(context.getResources().getColor(R.color.white));

                        break;
                }
            }

            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,SetShopStructure.class);
                    intent.putExtra("shopId",shops.get(position).getId());
                    intent.putExtra("template",Integer.valueOf(0));
                    intent.putExtra("shopName",shops.get(position).getName());
                    intent.putExtra("NoOfProducts",Integer.valueOf(shops.get(position).getNoOfProducts()));
                    context.startActivity(intent);
                }
            });
            holder.cdShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,SetShopStructure.class);
                    intent.putExtra("shopId",shops.get(position).getId());
                    intent.putExtra("template",Integer.valueOf(0));
                    intent.putExtra("NoOfProducts",Integer.valueOf(shops.get(position).getNoOfProducts()));
                    context.startActivity(intent);
                }
            });
        }
        else{
            holder.tvShopName2.setText(shops.get(position).getName());
            holder.tvShopName2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context, AddNewProduct.class);
                    intent.putExtra("shopId",shops.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }

        holder.ibDeleteShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("CONFIRM YOUR ACTIONS")
                        .setMessage("Are you sure you want to delete " + shops.get(position).getName())
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((UserHome)context).deleteShop(shops.get(position).getId());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return shops==null?0:shops.size();
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}