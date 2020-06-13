package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {

    private List<Map<String,Object>> data;
    private Context context;

    public static final int CROP_IMAGE = 2005;

    public ImagePickerAdapter(Context context){
        data = new ArrayList<>();
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivDisplayImage;
        TextView tvDisplayTitle;
        ImageButton ibDeleteImage;


        ViewHolder(View itemView){
            super(itemView);

            ivDisplayImage = itemView.findViewById(R.id.picked_image_view);
            tvDisplayTitle = itemView.findViewById(R.id.picked_image_tag);
            ibDeleteImage = itemView.findViewById(R.id.picked_image_delete);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_picker_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(data.get(position).get("tag")!=null)
            holder.tvDisplayTitle.setText(data.get(position).get("tag").toString());
        Glide
                .with(context)
                .load(data.get(position).get("imageUri"))
                .into(holder.ivDisplayImage);
        holder.ibDeleteImage.setOnClickListener(v -> {
            data.remove(position);
            notifyDataSetChanged();
        });
        holder.tvDisplayTitle.setOnClickListener(v -> {
            final EditText etTagHeader = new EditText(context);
            if(data.get(position).get("tag")!=null)
                etTagHeader.setText(data.get(position).get("tag").toString());
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter Image Tag");
            builder.setMessage("");
            builder.setView(etTagHeader);
            builder.setPositiveButton("DONE",((dialog, which) -> {
                if(etTagHeader.getText().toString().length()>0) {
                    data.get(position).put("tag", etTagHeader.getText().toString());
                    notifyItemChanged(position);
                }

            }));
            builder.setNegativeButton("CANCEL",((dialog, which) -> dialog.dismiss()));

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

}