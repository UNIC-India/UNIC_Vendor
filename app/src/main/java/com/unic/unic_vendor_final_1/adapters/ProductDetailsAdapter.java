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
    private List<String> stdKeys = new ArrayList<>(Arrays.asList("name","company","category","price"));
    private List<String> extraKeys = new ArrayList<>(Arrays.asList("subcategory","desc","tags","extraInfo1","extraInfo2"));
    private Context context;

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

        if(position<4){
            switch (position){
                case 0:
                    holder.tvKey.setText("Name: ");
                    break;
                case 1:
                    holder.tvKey.setText("Company: ");
                    break;
                case 2:
                    holder.tvKey.setText("Category: ");
                    break;
                case 3:
                    holder.tvKey.setText("Price: ");
            }
            holder.tvValue.setText(data.get(stdKeys.get(position)).toString());
        }
        else {
            if(data.keySet().contains(extraKeys.get(position-stdKeys.size()))){
                switch (extraKeys.get(position-stdKeys.size())){
                    case "subcategory":
                        holder.tvKey.setText("Subcategory: ");
                        holder.tvValue.setText(data.get(extraKeys.get(position-stdKeys.size())).toString());
                        break;
                    case "desc":
                        holder.tvKey.setText("Description: ");
                        holder.tvValue.setText(data.get(extraKeys.get(position-stdKeys.size())).toString());
                        break;
                    case "tags":
                        holder.tvKey.setText("Tags: ");
                        holder.tvValue.setText(data.get(extraKeys.get(position-stdKeys.size())).toString());
                        break;
                    default:
                        String[] temp = data.get(extraKeys.get(position-stdKeys.size())).toString().split(":");
                        if(temp.length>1) {
                            holder.tvKey.setText(temp[0].toUpperCase()+ ": ");
                            holder.tvValue.setText(temp[1]);
                        }
                        else {
                            holder.tvKey.setText("");
                            holder.tvValue.setText(temp[0]);
                        }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return stdKeys.size()+extraKeys.size();
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
        List<String> keys = new ArrayList<>(data.keySet());
        extraKeys.retainAll(keys);
    }
}