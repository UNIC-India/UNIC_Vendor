package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Notification;

import java.util.List;
import java.util.Map;

public class TeamAdapter  extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private List<Map<String, Object>> data;
    private Context context;


    public TeamAdapter(Context context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRole,tvNumber;
        ImageView delete_member;


        ViewHolder(@NonNull View itemView) {
            super(itemView);


            delete_member=itemView.findViewById(R.id.delete_member);
            tvRole=itemView.findViewById(R.id.tvRole);
            tvNumber=itemView.findViewById(R.id.tvNumber);

        }
    }

    @NonNull
    @Override
    public TeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.team_member_list_item, parent, false);
        return new TeamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ViewHolder holder, final int position) {
        holder.tvNumber.setText(data.get(position).get("phoneNo").toString());
        holder.tvRole.setText(data.get(position).get("role").toString());



    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public void setData(List<Map<String,Object>> data){
        this.data=data;
    }


}