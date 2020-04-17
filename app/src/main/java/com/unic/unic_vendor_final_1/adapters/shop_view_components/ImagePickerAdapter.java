package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {

    private List<Map<String,String>> data;
    private Context context;

    public ImagePickerAdapter(Context context){
        data = new ArrayList<>();
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivDisplayImage;
        TextView tvDisplayTitle;


        ViewHolder(View itemView){
            super(itemView);

            ivDisplayImage = itemView.findViewById(R.id.double_image_view);
            tvDisplayTitle = itemView.findViewById(R.id.double_image_view_name);


        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_image_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDisplayTitle.setText(data.get(position).get("TAG"));
        Glide
                .with(context)
                .load(data.get(position).get("imageId"))
                .into(holder.ivDisplayImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
