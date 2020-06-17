package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.helpers.OrderItems;
import com.unic.unic_vendor_final_1.views.nav_fragments.OrdersFragment;

import java.util.List;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.ViewHolder> {

    private List<Order> orders;
    private  Context context;
    private Fragment fragment;



    public AllOrdersAdapter(Context context, Fragment fragment){
        this.context = context;
        this.fragment=fragment;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPhone,tvDate,tvOrderId,tvStatus,tvShopName,tvNoOfItems,tvTime,tvCreatedBy,tvTotal,tvGST,tvCustomer;
        CardView cdOrder;
        View line;
        ImageView ivAccept,ivReject;
        LottieAnimationView loading;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvPhone=itemView.findViewById(R.id.tvPhoneNo);
            tvOrderId=itemView.findViewById(R.id.tvOrderId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            cdOrder=itemView.findViewById(R.id.cdOrder);
            tvShopName=itemView.findViewById(R.id.tvShopName);
            tvTime=itemView.findViewById(R.id.tvTime);
            line=itemView.findViewById(R.id.view2);
            ivAccept=itemView.findViewById(R.id.ivAccept);
            ivReject=itemView.findViewById(R.id.ivReject);
            loading=itemView.findViewById(R.id.loading);
            tvNoOfItems=itemView.findViewById(R.id.tvNoOfItems);
            tvCreatedBy=itemView.findViewById(R.id.textView9);
            tvTotal=itemView.findViewById(R.id.tvAmount);
            tvGST=itemView.findViewById(R.id.tvGST);
            tvCustomer=itemView.findViewById(R.id.tvCustomer);




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
        holder.tvCreatedBy.setText("");
        holder.tvTotal.setText("");
        holder.tvNoOfItems.setText("");
        if(orders.get(position).getPickUp()==1)
            holder.tvCustomer.setText("Personal/Pick Up");
        else
            holder.tvCustomer.setText(orders.get(position).getAddress().getOrgName().length()<=1?"Personal":orders.get(position).getAddress().getOrgName());
        new FirebaseRepository().db.collection("users").document(orders.get(position).getOwnerId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.tvCreatedBy.setText(documentSnapshot.getString("fullName"));
              /*  if(orders.get(position).getOrderAddress().get.length()<=1){
                    holder.tvCustomer.setText(holder.tvCreatedBy.getText());
                }
                else
                    holder.tvCustomer.setText(orders.get(position).getOrgName());*/
            }
        });

        new FirebaseRepository().db.collection("shops").document(orders.get(position).getShopId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.tvShopName.setText(documentSnapshot.getString("name"));
            }
        });
        holder.ivAccept.setVisibility(View.GONE);
        holder.ivReject.setVisibility(View.GONE);
        holder.tvStatus.setVisibility(View.GONE);
        holder.cdOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).replace(R.id.home_fragment,new OrderItems(orders.get(position),context)).commit();
            }
        });

        String date = orders.get(position).getTime().toString().substring(8,10)+" "+orders.get(position).getTime().toString().substring(4,7)+orders.get(position).getTime().toString().substring(29,34);
        holder.tvDate.setText(date);
        holder.tvTime.setText(orders.get(position).getTime().toString().substring(11,16));
        holder.tvNoOfItems.setText(Integer.valueOf(orders.get(position).getNo_of_items()).toString());
       // holder.tvCustomer.setText((orders.get(position).getOrgName()==null||orders.get(position).toString().equals(" "))||orders.get(position).getOrgName().toString().length()==0?"Personal":orders.get(position).getOrgName().toString());
        holder.tvGST.setText((orders.get(position).getGSTIN()==null||orders.get(position).getGSTIN().toString().equals(" "))?"GSTIN:"+"Not Specified":"GSTIN: "+orders.get(position).getGSTIN().toString());

        String total = "Rs "+Double.valueOf(orders.get(position).getTotal()).toString();
        holder.tvTotal.setText(total);
        holder.ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivAccept.setVisibility(View.GONE);
                holder.ivReject.setVisibility(View.GONE);
                holder.loading.setVisibility(View.VISIBLE);
                ((OrdersFragment)fragment).setOrderStatus(position,orders.get(position).getId(),1);
            }
        });
        holder.ivReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivAccept.setVisibility(View.GONE);
                holder.ivReject.setVisibility(View.GONE);
                holder.loading.setVisibility(View.VISIBLE);
                ((OrdersFragment)fragment).setOrderStatus(position,orders.get(position).getId(),-1);

            }
        });
        switch(orders.get(position).getOrderStatus()){
            case 0:
                holder.ivAccept.setVisibility(View.VISIBLE);
                holder.ivReject.setVisibility(View.VISIBLE);

                break;
            case 1:
                holder.loading.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Accepted");
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                holder.line.setBackgroundColor(context.getResources().getColor(R.color.green2));
                break;
            case 2:
                holder.loading.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Prepared");
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                holder.line.setBackgroundColor(context.getResources().getColor(R.color.green2));
                break;
            case 3:
                holder.loading.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Dispatched");
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                holder.line.setBackgroundColor(context.getResources().getColor(R.color.green2));
                break;
            case 4:
                holder.loading.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Delivered");
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green2)));
                holder.line.setBackgroundColor(context.getResources().getColor(R.color.green2));
                break;
            case -1:
                holder.loading.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Denied");
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
                holder.line.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                break;
        }

        holder.tvPhone.setText(""+orders.get(position).getPhoneNo());
        holder.tvOrderId.setText(" "+orders.get(position).getId());



    }

    @Override
    public int getItemCount() {
        return orders==null?0:orders.size();
    }

    public void setOrders(List<Order> orders) {
        this.orders=orders;
        notifyDataSetChanged();
    }

    public void updateOrder(int position, int status){
        orders.get(position).setOrderStatus(status);
        notifyItemChanged(position);
    }

}