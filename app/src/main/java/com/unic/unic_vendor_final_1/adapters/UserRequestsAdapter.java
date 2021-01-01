package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

import java.util.List;
import java.util.Map;

public class UserRequestsAdapter extends RecyclerView.Adapter<UserRequestsAdapter.ViewHolder> {

    private List<Map<String,String>> users;
    private Context context;
    private UserShopsViewModel userShopsViewModel;
    private int type = 0;
    private String shopId;

    public UserRequestsAdapter(Context context, int type,String shopId) {
        this.context = context;
        userShopsViewModel = new ViewModelProvider((FragmentActivity)context).get(UserShopsViewModel.class);
        this.type = type;
        this.shopId = shopId;
    }

    class RequestListener implements View.OnClickListener {

        int position;

        public RequestListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            new AlertDialog.Builder(context)
                    .setMessage(type==0?"Do you wish to allow " + users.get(position).get("userName") + " to visit your shop?":"Do you wish to revoke " + users.get(position).get("userName") + "'s access to your shop?")
                    .setPositiveButton("YES",((dialog, which) -> {
                        if(type==0){
                            userShopsViewModel.allowUserAccess(shopId,users.get(position));
                        }
                        if(type==1){
                            userShopsViewModel.revokeUserAccess(shopId,users.get(position));
                        }
                        users.remove(position);
                        notifyItemRemoved(position);
                    }))
                    .setNegativeButton("NO",(dialog, which) -> {
                        if(type==0){
                            userShopsViewModel.rejectUserAccess(shopId,users.get(position));
                        }

                        dialog.dismiss();
                    })
                    .create().show();

        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvPhoneNo;
        CardView bounding;

        public ViewHolder(View itemView){
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNumber);
            tvPhoneNo = itemView.findViewById(R.id.tvRole);
            bounding = itemView.findViewById(R.id.cv_team_member);
            itemView.findViewById(R.id.delete_member).setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_member_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(users.get(position).get("userName"));
        holder.tvPhoneNo.setText(users.get(position).get("phoneNo"));
        holder.bounding.setOnClickListener(new RequestListener(position));
    }

    @Override
    public int getItemCount() {
        if(users==null)
            return 0;
        return users.size();
    }

    public void setUsers(List<Map<String, String>> users) {
        this.users = users;
    }
}
