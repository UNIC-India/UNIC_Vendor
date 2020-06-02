package com.unic.unic_vendor_final_1.adapters;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.views.activities.AddNewProduct;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;
import com.unic.unic_vendor_final_1.views.activities.UserHome;
import com.unic.unic_vendor_final_1.views.helpers.ProductViewFragment;
import com.unic.unic_vendor_final_1.views.helpers.TeamFragment;
import com.unic.unic_vendor_final_1.views.helpers.WriteNofication;
import com.unic.unic_vendor_final_1.views.nav_fragments.MyAppsFragment;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {

    private List<Shop> shops;
    private Context context;
    int from=0;

    public ShopListAdapter(Context context, int from){
        this.context = context;
        this.from=from;

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
            if(from==0){
                holder.tvShopName2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((AppCompatActivity)context).getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .replace(R.id.home_fragment,new ProductViewFragment(shops.get(position).getId()))
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
            else if(from==1){
                holder.tvShopName2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((AppCompatActivity)context).getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .replace(R.id.home_fragment,new WriteNofication(shops.get(position).getId(),shops.get(position).getName()))
                                .addToBackStack(null)
                                .commit();

                    }
                });
            }
            else if(from==2){
                holder.tvShopName2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((AppCompatActivity)context).getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .replace(R.id.home_fragment,new TeamFragment(shops.get(position).getId(),shops.get(position).getName()))
                                .addToBackStack(null)
                                .commit();

                    }
                });
            }
            else if(from==3){

            }

    }

    @Override
    public int getItemCount() {
        return shops==null?0:shops.size();
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}