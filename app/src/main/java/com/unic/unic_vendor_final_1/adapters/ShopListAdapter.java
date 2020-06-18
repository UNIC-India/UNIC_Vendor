package com.unic.unic_vendor_final_1.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.commons.Helpers;
import com.unic.unic_vendor_final_1.databinding.FragmentWriteNoficationBinding;
import com.unic.unic_vendor_final_1.datamodels.Notification;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.activities.UserHome;
import com.unic.unic_vendor_final_1.views.helpers.ProductViewFragment;
import com.unic.unic_vendor_final_1.views.nav_fragments.NotificationsFragment;
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
                if(shops.size()==1){
                    ((AppCompatActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.home_fragment,new ProductViewFragment(shops.get(position).getId()))
                            .addToBackStack(null)
                            .commit();
                }
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
                if(shops.size()==1){
                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.fragment_write_nofication);
                    dialog.findViewById(R.id.loading).setVisibility(View.GONE);
                    ((TextView)dialog.findViewById(R.id.tvShopName)).setText("Name: "+shops.get(position).getName());
                    ((Button)dialog.findViewById(R.id.btncancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    ((Button)dialog.findViewById(R.id.btnsend)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title=((EditText)dialog.findViewById(R.id.etTitle)).getText().toString();
                            String message=((EditText)dialog.findViewById(R.id.etMessage)).getText().toString();
                            ((Button)dialog.findViewById(R.id.btnsend)).setEnabled(false);
                            ((UserHome)context).sendNotification(title,shops.get(position).getId(),message);
                            dialog.dismiss();


                        }
                    });
                    Helpers.buttonEffect((Button)dialog.findViewById(R.id.btnsend));
                    Helpers.buttonEffect((Button)dialog.findViewById(R.id.btncancel));
                    dialog.show();
                    Window window= dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);


                }
                holder.tvShopName2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog=new Dialog(context);
                        dialog.setContentView(R.layout.fragment_write_nofication);
                        dialog.findViewById(R.id.loading).setVisibility(View.GONE);
                        ((TextView)dialog.findViewById(R.id.tvShopName)).setText("Name: "+shops.get(position).getName());
                        ((Button)dialog.findViewById(R.id.btncancel)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        ((Button)dialog.findViewById(R.id.btnsend)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String title=((EditText)dialog.findViewById(R.id.etTitle)).getText().toString();
                                String message=((EditText)dialog.findViewById(R.id.etMessage)).getText().toString();
                                ((Button)dialog.findViewById(R.id.btnsend)).setEnabled(false);
                                ((UserHome)context).sendNotification(title,shops.get(position).getId(),message);
                                dialog.dismiss();


                            }
                        });
                        Helpers.buttonEffect((Button)dialog.findViewById(R.id.btnsend));
                        Helpers.buttonEffect((Button)dialog.findViewById(R.id.btncancel));
                        dialog.show();
                        Window window= dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);



                    }
                });
            }
            else if(from==2){
                if(shops.size()==1){
                    ((AppCompatActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.home_fragment,new TeamFragment(shops.get(position).getId(),shops.get(position).getName()))
                            .addToBackStack(null)
                            .commit();
                }
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
                if(shops.size()==1){
                    ((AppCompatActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.home_fragment,new LogoFragment(shops.get(position).getId(),shops.get(position).getLogoLink()))
                            .addToBackStack(null)
                            .commit();
                }
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