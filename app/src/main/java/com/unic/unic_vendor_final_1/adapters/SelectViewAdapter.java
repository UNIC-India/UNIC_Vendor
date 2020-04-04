package com.unic.unic_vendor_final_1.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Shop;

import java.util.List;

public class SelectViewAdapter extends RecyclerView.Adapter<SelectViewAdapter.ViewHolder>  {
    private List<String> viewCategories;
    private Activity context;

    public  int mPosition;

    public SelectViewAdapter(Activity context){
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvViewName;
        ImageView ivViewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvViewName = itemView.findViewById(R.id.view_name);
            ivViewImage = itemView.findViewById(R.id.view_image);

        }
        @Override
        public void onClick(View view) {
            mPosition = getLayoutPosition();
            Intent replyIntent=new Intent();
            replyIntent.putExtra("ID", mPosition);
            context.setResult(Activity.RESULT_OK,replyIntent);
            context.finish();


        }
    }

    @NonNull
    @Override
    public SelectViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_view_list_item, parent, false);
        return new SelectViewAdapter.ViewHolder(view);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return viewCategories==null?0:viewCategories.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SelectViewAdapter.ViewHolder holder, int position) {

        holder.tvViewName.setText(viewCategories.get(position));
    }



    public void setViewCategories(List<String> viewCategories) {
        this.viewCategories = viewCategories;
    }
}


