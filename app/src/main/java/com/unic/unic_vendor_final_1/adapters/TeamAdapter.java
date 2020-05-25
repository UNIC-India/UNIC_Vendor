package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Notification;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import java.util.List;
import java.util.Map;

public class TeamAdapter  extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private List<Map<String, String>> data;
    private Context context;
    String shopId;



    public TeamAdapter(Context context, String shopId){
        this.context = context;
        this.shopId=shopId;
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
        holder.delete_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("CONFIRM YOUR ACTIONS")
                        .setMessage("Are you sure you want to remove" + data.get(position).get("phoneNo")+" from this team?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((UserHome)context).deleteMember(data.get(position).get("phoneNo").toString(),data.get(position).get("role").toString(),shopId);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public void setData(List<Map<String,String>> data){
        this.data=data;
    }


}
