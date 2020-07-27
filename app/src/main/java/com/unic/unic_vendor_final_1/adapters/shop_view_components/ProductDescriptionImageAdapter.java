package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.commons.BlurTransformation;

import java.util.List;

public class ProductDescriptionImageAdapter extends RecyclerView.Adapter<ProductDescriptionImageAdapter.ViewHolder> {

    private List<String> imageLinks;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, backgroundImage;

        public ViewHolder(View itemView){
            super(itemView);
            productImage = itemView.findViewById(R.id.product_display_image);
            backgroundImage = itemView.findViewById(R.id.product_display_background);
        }
    }

    public ProductDescriptionImageAdapter(Context context, List<String> imageLinks){
        this.context = context;
        this.imageLinks = imageLinks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_description_image_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide
                .with(context)
                .load(imageLinks.get(position))
                .into(holder.productImage);
        Glide
                .with(context)
                .load(imageLinks.get(position))
                .transform(new BlurTransformation(context))
                .into(holder.backgroundImage);
    }

    @Override
    public int getItemCount() {

        if(imageLinks==null)
            return 0;

        return imageLinks.size();

    }
}
