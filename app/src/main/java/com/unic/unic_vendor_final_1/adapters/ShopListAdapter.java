package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.views.helpers.ProductViewFragment;
import com.unic.unic_vendor_final_1.views.settings_fragments.LogoFragment;
import com.unic.unic_vendor_final_1.views.settings_fragments.TeamFragment;
import com.unic.unic_vendor_final_1.views.helpers.WriteNofication;

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
                holder.tvShopName2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((AppCompatActivity)context).getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .replace(R.id.home_fragment,new LogoFragment(shops.get(position).getId(),shops.get(position).getLogoLink()))
                                .addToBackStack(null)
                                .commit();

                    }
                });

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