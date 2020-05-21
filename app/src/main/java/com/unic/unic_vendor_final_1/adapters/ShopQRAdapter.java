package com.unic.unic_vendor_final_1.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.nav_fragments.QRFragment;

import java.util.ArrayList;
import java.util.List;

public class ShopQRAdapter  extends RecyclerView.Adapter<ShopQRAdapter.ViewHolder>{

    private List<Shop> shops = new ArrayList<>();
    private UserShopsViewModel userShopsViewModel;
    private Context context;
    private Fragment fragment;

    public ShopQRAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        userShopsViewModel=new ViewModelProvider(((FragmentActivity)context)).get(UserShopsViewModel.class);

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivShopImage;
        TextView tvShopName;
        ImageButton ibShopQR;
        Button btnGenerateQR;

        ViewHolder(View itemView){
            super(itemView);
            ivShopImage = itemView.findViewById(R.id.qr_shop_image);
            tvShopName = itemView.findViewById(R.id.qr_shop_name);
            ibShopQR = itemView.findViewById(R.id.ib_qr_dialog);
            btnGenerateQR = itemView.findViewById(R.id.btn_shop_generate_qr);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_qr_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvShopName.setText(shops.get(position).getName());
        Glide
                .with(context)
                .load(shops.get(position).getImageLink())
                .into(holder.ivShopImage);
        holder.btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userShopsViewModel.buildSubscribeLink(shops.get(position).getId(),shops.get(position).getName());
            }
        });
        holder.ibShopQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shops.get(position).getDynSubscribeLink()!=null){
                    Toast.makeText(context, shops.get(position).getDynSubscribeLink(), Toast.LENGTH_SHORT).show();
                }

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.qr_dialog_layout);
                try {
                    ((ImageView)dialog.findViewById(R.id.qr_dialog_qr_code)).setImageBitmap(((QRFragment)fragment).generateQRCode(shops.get(position).getDynSubscribeLink().toString(),300));
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                ((Button)dialog.findViewById(R.id.qr_dialog_done)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ((Button)dialog.findViewById(R.id.qr_dialog_share_link)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        context.startActivity(shareIntent);
                    }
                });

                ((Button)dialog.findViewById(R.id.dialog_share_qr)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}
