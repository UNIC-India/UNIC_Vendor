package com.unic.unic_vendor_final_1.adapters.shop_view_components.ImageViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.commons.BlurBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoubleImageAdapter extends RecyclerView.Adapter<DoubleImageAdapter.ViewHolder> {

    private List<Map<String,Object>> data = new ArrayList<>();
    private Context mContext;
    private int demo = 0;

    public DoubleImageAdapter(Context mContext){
        this.mContext = mContext;
        this.demo = 0;
    }

    public DoubleImageAdapter(int demo){
        this.demo = demo;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvTag;
        ImageView ivBackground;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            ivImage = itemView.findViewById(R.id.double_image_view);
            tvTag = itemView.findViewById(R.id.double_image_view_name);
            ivBackground = itemView.findViewById(R.id.double_image_view_background);
        }
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_image_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(demo==0) {

            if (data.get(position).get("tag") != null)
                holder.tvTag.setText(data.get(position).get("tag").toString());
            else holder.tvTag.setHint(" ");

            Glide
                    .with(mContext)
                    .load(data.get(position).get("imageLink").toString())
                    .into(holder.ivImage);

            Glide
                    .with(mContext)
                    .load(data.get(position).get("imageLink").toString())
                    .into(holder.ivBackground);

            class BitmapDownloadTask extends AsyncTask<String,Void, Bitmap> {
                @Override
                protected Bitmap doInBackground(String... strings) {

                    try{
                        URL url = new URL(strings[0]);

                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();

                        return BitmapFactory.decodeStream(input);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {

                    if(bitmap==null)
                        return;

                    Bitmap stretchedBitmap = BlurBuilder.blur(mContext,bitmap);

                    holder.ivBackground.setImageBitmap(stretchedBitmap);
                }
            }

            new BitmapDownloadTask().execute(data.get(position).get("imageLink").toString());
        }
        else if (demo==1){
            holder.ivImage.setImageResource(R.drawable.prouctdemo2);
            holder.tvTag.setText("Tag"+ position);
        }

    }

    @Override
    public int getItemCount() {
        if (demo==0)
            return data.size();
        return 3;
    }
}
