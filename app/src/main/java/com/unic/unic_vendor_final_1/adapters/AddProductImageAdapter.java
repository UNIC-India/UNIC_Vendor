package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.activities.AddNewProduct;

import java.io.IOException;
import java.util.List;

public class AddProductImageAdapter extends RecyclerView.Adapter<AddProductImageAdapter.ViewHolder> {

    private List<Uri> imageUris;
    private Context context;

    public AddProductImageAdapter(List<Uri> imageUris, Context context) {
        this.imageUris = imageUris;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;

        public ViewHolder(View itemView){
            super(itemView);
            productImage = itemView.findViewById(R.id.fixed_image_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fixed_image_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(position == imageUris.size()){
            holder.productImage.setImageResource(R.drawable.ic_add_a_photo_black_80dp);
            holder.productImage.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.light_gray)));
            holder.productImage.setBackground(context.getResources().getDrawable(R.drawable.round_border));
            holder.productImage.setOnClickListener(v -> ((AddNewProduct)context).addImage());
            holder.productImage.setScaleType(ImageView.ScaleType.CENTER);
        }

        else {

            holder.productImage.setBackgroundTintList(null);
            holder.productImage.setBackgroundResource(0);

            holder.productImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = new ImageView(context);
                }
            });

            Glide
                    .with(context)
                    .load(imageUris.get(position))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .into(holder.productImage);

        }

    }

    @Override
    public int getItemCount() {
        return imageUris==null?0:(imageUris.size()+1);
    }

    public void setImageUris(List<Uri> imageUris) {
        this.imageUris = imageUris;
        notifyDataSetChanged();
    }
}
