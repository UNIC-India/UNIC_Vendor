package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;

import java.util.List;
import java.util.Map;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> products;
    private List<Integer> qty;


    public OrderItemsAdapter(Context context){
        this.mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName,tvCompany,tvPrice, tvTotal,tvQty;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvProductName = itemView.findViewById(R.id.product_name);
            tvCompany=itemView.findViewById(R.id.product_company_name);
            tvPrice=itemView.findViewById(R.id.product_price);
            tvTotal=itemView.findViewById(R.id.tvTotal);
            tvQty=itemView.findViewById(R.id.tvQty);

        }
    }

    @NonNull
    @Override
    public OrderItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_list_item,parent,false);
        return new OrderItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsAdapter.ViewHolder holder, int position) {

        holder.tvProductName.setText(products.get(position).get("name").toString());
        holder.tvCompany.setText(products.get(position).get("company").toString());
        holder.tvPrice.setText(products.get(position).get("price").toString()+"Rs");
        holder.tvTotal.setText(Double.parseDouble(products.get(position).get("price").toString())*qty.get(position)+"");
        holder.tvQty.setText(""+qty.get(position));




    }

    @Override
    public int getItemCount() {
        return products.size();

    }

    public void setProducts(List<Map<String,Object>> products,List<Integer> qty){
        this.products = products;
        this.qty=qty;

    }

}

