package com.unic.unic_vendor_final_1.adapters.shop_view_components.ImageViewAdapters;

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

public class DoubleImageAdapter extends RecyclerView.Adapter<DoubleImageAdapter.ViewHolder> {

    private List<Map<String,Object>> data = new ArrayList<>();
    private Context mContext;

    public DoubleImageAdapter(Context mContext){
        this.mContext = mContext;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvTag;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            ivImage = itemView.findViewById(R.id.double_image_view);
            tvTag = itemView.findViewById(R.id.double_image_view_name);
        }
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_image_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(data.get(position).get("tag")!=null)
            holder.tvTag.setText(data.get(position).get("tag").toString());

        Glide
                .with(mContext)
                .load(data.get(position).get("imageLink").toString())
                .into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
