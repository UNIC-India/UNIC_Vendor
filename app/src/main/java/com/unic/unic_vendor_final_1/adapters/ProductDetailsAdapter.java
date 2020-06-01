package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {

    private Map<String, Object> data;
    private ArrayList<String> keys=new ArrayList<>();
    private ArrayList<Object> values=new ArrayList<>();
    private Context context;
    private final static ArrayList<String> keysToOmit=new ArrayList<>(Arrays.asList("name","company","category","price","firestoreId","imageId","nameKeywords","shopId"));


    public ProductDetailsAdapter(Context context) {
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKey, tvValue;
        CardView cdDetail;
        public LinearLayout.LayoutParams params;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKey = itemView.findViewById(R.id.tvKey);
            tvValue = itemView.findViewById(R.id.tvValue);
            cdDetail=itemView.findViewById(R.id.cdDetail);
            params=new LinearLayout.LayoutParams(0,0);

        }
    }

    @NonNull
    @Override
    public ProductDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_details_item, parent, false);
        return new ProductDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsAdapter.ViewHolder holder, final int position) {
        if (position <= 3) {
            switch (position) {
                case 0:
                    holder.tvKey.setText("Name:");
                    holder.tvValue.setText(data.get("name").toString());
                    break;
                case 1:
                    holder.tvKey.setText("Company:");
                    holder.tvValue.setText(data.get("company").toString());
                    break;
                case 2:
                    holder.tvKey.setText("Price:");
                    holder.tvValue.setText(data.get("price").toString());
                    break;
                case 3:
                    holder.tvKey.setText("Category:");
                    holder.tvValue.setText(data.get("category").toString());
                    break;
            }
        } else {
            if (keysToOmit.contains(keys.get(position>=data.size()?(position-data.size()):position))) {
                holder.cdDetail.setLayoutParams(holder.params);
            } else {
                holder.tvKey.setText(keys.get(position>=data.size()?(position-data.size()):position) + ":");
                holder.tvValue.setText(values.get(position>=data.size()?(position-data.size()):position).toString());
            }

        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.keySet().size()+4;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
        this.keys.addAll(data.keySet());
        this.values.addAll(data.values());
    }
}

