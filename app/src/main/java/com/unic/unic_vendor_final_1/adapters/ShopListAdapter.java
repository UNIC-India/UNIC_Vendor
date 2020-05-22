package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.views.activities.AddNewProduct;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;
import com.unic.unic_vendor_final_1.views.activities.UserHome;
import com.unic.unic_vendor_final_1.views.nav_fragments.MyAppsFragment;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {

    private List<Shop> shops;
    private Context context;


    public ShopListAdapter(Context context){
        this.context = context;


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName2;


        ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvShopName2=itemView.findViewById(R.id.tvshop);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListAdapter.ViewHolder holder, final int position) {


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

    @Override
    public int getItemCount() {
        return shops==null?0:shops.size();
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}
