package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Order;

import java.util.List;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.ViewHolder> {

    private List<Order> orders;
    private Context context;



    public AllOrdersAdapter(Context context){
        this.context = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvPhone,tvDate,tvOrderId,tvStatus,tvshopname;
        CardView cdOrder;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvFirmName);
            tvDate=itemView.findViewById(R.id.datenTime);
            tvPhone=itemView.findViewById(R.id.phoneNo);
            tvOrderId=itemView.findViewById(R.id.orderId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            cdOrder=itemView.findViewById(R.id.cdOrder);
            tvshopname=itemView.findViewById(R.id.tvShopName);
        }
    }

    @NonNull
    @Override
    public AllOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_orders_list_item, parent, false);
        return new AllOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrdersAdapter.ViewHolder holder, final int position) {
        holder.tvDate.setText((orders.get(position).getTime().toString()).substring(0,20));
        holder.tvStatus.setText(orders.get(position).getStatus());
        holder.tvPhone.setText("No."+orders.get(position).getPhoneNo());
        holder.tvOrderId.setText("OrderID"+orders.get(position).getId());
        new FirebaseRepository().db.collection("shops").document(orders.get(position).getShopId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.tvshopname.setText(documentSnapshot.getString("name"));
            }
        });


    }

    @Override
    public int getItemCount() {
        return orders==null?0:orders.size();
    }

    public void setShops(List<Order> shops) {
        this.orders=shops;
        notifyDataSetChanged();
    }
}
