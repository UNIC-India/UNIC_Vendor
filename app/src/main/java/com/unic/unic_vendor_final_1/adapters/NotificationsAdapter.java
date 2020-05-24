package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Notification;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.views.activities.AddNewProduct;
import com.unic.unic_vendor_final_1.views.helpers.WriteNofication;

import java.util.List;

public class NotificationsAdapter  extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<Notification> notifications;
    private Context context;


    public NotificationsAdapter(Context context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvTime, tvShopName;


        ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvMessage=itemView.findViewById(R.id.tvMessage);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvTime=itemView.findViewById(R.id.tvDate);
            tvShopName=itemView.findViewById(R.id.tvShopName);

        }
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, final int position) {


        new FirebaseRepository().db.collection("shops").document(notifications.get(position).getShopId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.tvShopName.setText(documentSnapshot.getData().get("name").toString());
            }
        });
        holder.tvTime.setText(notifications.get(position).getTime().toString().substring(11,16)+" "+notifications.get(position).getTime().toString().substring(8,10)+" "+notifications.get(position).getTime().toString().substring(4,7)+notifications.get(position).getTime().toString().substring(29,34));
        holder.tvTitle.setText(notifications.get(position).getTitle());
        holder.tvMessage.setText(notifications.get(position).getMessage());



    }

    @Override
    public int getItemCount() {
        return notifications==null?0:notifications.size();
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}