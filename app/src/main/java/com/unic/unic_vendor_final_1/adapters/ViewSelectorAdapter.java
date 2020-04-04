package com.unic.unic_vendor_final_1.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class ViewSelectorAdapter extends RecyclerView.Adapter<ViewSelectorAdapter.ViewHolder>{

    private Activity activity;
    private List<String> viewTypes = new ArrayList<>();
    private List<String> viewImages = new ArrayList<>();
    private List<String> viewCategories = new ArrayList<>();
    private List<String> viewSliders = new ArrayList<>();
    private List<String> viewProducts = new ArrayList<>();
    private int status;
    private int returnValue;

    public ViewSelectorAdapter(Activity activity){
        this.activity = activity;
        status = 0;
        returnValue = 0;

        viewTypes.add("Image Views");
        viewTypes.add("Category Views");
        viewTypes.add("Sliders");
        viewTypes.add("Product Views");

        viewImages.add("Single image view");
        viewImages.add("Double images view");
        viewImages.add("Triple images view");

        viewCategories.add("Vertical list with images");
        viewCategories.add("Horizontal scroll with images");
        viewCategories.add("2 column grid with images");
        viewCategories.add("Simple vertical list");

        viewSliders.add("Simple slider");
        viewSliders.add("Slider with indicator");

        viewProducts.add("Horizontal scroll with images");
        viewProducts.add("2 column grid with images");
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvViewName;

        ViewHolder(@NonNull View itemView){
            super(itemView);
            tvViewName = itemView.findViewById(R.id.view_item_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_selector_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        switch (status){
            case 0:
                holder.tvViewName.setText(viewTypes.get(position));
                break;
            case 1:
                holder.tvViewName.setText(viewImages.get(position));
                break;
            case 2:
                holder.tvViewName.setText(viewCategories.get(position));
                break;
            case 3:
                holder.tvViewName.setText(viewSliders.get(position));
                break;
            case 4:
                holder.tvViewName.setText(viewProducts.get(position));
        }

        holder.tvViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==0) {
                    returnValue += ((position + 1) * 10);
                    changeStatus(position + 1);
                }
                else {
                    returnValue+= (position+1);
                    Intent intent = new Intent();
                    intent.putExtra("viewCode",returnValue);
                    intent.putExtra("tag",activity.getIntent().getStringExtra("tag"));
                    activity.setResult(Activity.RESULT_OK,intent);
                    activity.finish();
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        switch (status){
            case 0:
            case 2:
                return 4;
            case 1:
                return 3;
            case 3:
            case 4:
                return 2;
        }
        return 0;
    }

    private void changeStatus(int status){
        this.status = status;
        notifyDataSetChanged();
    }

}
