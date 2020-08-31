package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.text.PrecomputedText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;

import java.util.List;
import java.util.Map;

public class OrderReportItemsAdapter extends RecyclerView.Adapter<OrderReportItemsAdapter.ViewHolder> {

    List<Map<String,Object>> products;

    private Context context;

    public OrderReportItemsAdapter(Context context) {
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName,productCompany,productCategory,productExtraInfo1,productIndividualPrice,productQuantity,productTotalPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.order_summary_item_image);
            productName = itemView.findViewById(R.id.order_summary_item_name);
            productCompany = itemView.findViewById(R.id.order_summary_item_company);
            productCategory = itemView.findViewById(R.id.order_summary_item_category);
            productExtraInfo1 = itemView.findViewById(R.id.order_summary_item_extra_info_1);
            productIndividualPrice = itemView.findViewById(R.id.order_summary_item_individual_price);
            productQuantity = itemView.findViewById(R.id.order_summary_item_quantity);
            productTotalPrice = itemView.findViewById(R.id.order_summary_item_total_price);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_summary_items_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide
                .with(context)
                .load(products.get(position).get("imageId").toString())
                .into(holder.productImage);

        holder.productName.setText(products.get(position).get("name").toString());
        holder.productCompany.setText(products.get(position).get("company").toString());

        holder.productCategory.setText(products.get(position).get("category").toString());
        if(products.get(position).containsKey("extraInfo1")&&products.get(position).get("extraInfo1").toString().length()>1&&!products.get(position).get("extraInfo1").toString().equals("null")) {
            holder.productExtraInfo1.setVisibility(View.VISIBLE);
            holder.productExtraInfo1.setText(products.get(position).get("extraInfo1").toString());
        }
        else
            holder.productExtraInfo1.setVisibility(View.GONE);

        holder.productIndividualPrice.setText("\u20B9 "+products.get(position).get("price").toString());
        holder.productQuantity.setText(products.get(position).get("orderQuantity").toString());
        holder.productTotalPrice.setText("\u20B9 "+ products.get(position).get("subtotal").toString());

    }

    @Override
    public int getItemCount() {
        return products==null?0:products.size();
    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
        notifyDataSetChanged();
    }
}
