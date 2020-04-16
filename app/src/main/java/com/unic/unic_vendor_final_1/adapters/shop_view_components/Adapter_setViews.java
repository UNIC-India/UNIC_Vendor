package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Product;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ShopPageFragment;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Adapter_setViews extends RecyclerView.Adapter<Adapter_setViews.ViewHolder> {

    public int lastchecked=-1;
    int code=0;
    Context mContext;
    Activity mActivity;

    public Adapter_setViews(Context mContext){
        this.mContext=mContext;
        mActivity=(Activity)mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView rv;
        RadioButton rdbtn;
        TextView tvViewTitle;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            rv=itemView.findViewById(R.id.rvDemo);
            rdbtn=itemView.findViewById(R.id.rdBtn);
            tvViewTitle=itemView.findViewById(R.id.tvViewTitle);
            rdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastchecked=getAdapterPosition();
                    notifyDataSetChanged();
                    Button donebtn=mActivity.findViewById(R.id.done_view);
                    donebtn.setVisibility(View.VISIBLE);

                }
            });

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_selector_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        switch(code){
            case 4:

                if(position==0) {

                    DoubleProductAdapter doubleProductAdapter = new DoubleProductAdapter(1);
                    LinearLayoutManager doubleProductLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);

                    holder.rv.setLayoutManager(doubleProductLayoutManager);
                    holder.rv.setAdapter(doubleProductAdapter);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Triple Products");
                }
                if(position==1){
                    TripleImageAdapter tripleImageAdapter=new TripleImageAdapter(1);
                    LinearLayoutManager tripleProductLayoutManager=new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                    holder.rv.setLayoutManager(tripleProductLayoutManager);
                    holder.rv.setAdapter(tripleImageAdapter);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Double Products");
                }
                if(position==2){
                    ProductListWithoutImagesAdapter productListWithoutImagesAdapter=new ProductListWithoutImagesAdapter(1);
                    LinearLayoutManager ProductLayoutManager=new LinearLayoutManager(mContext);
                    holder.rv.setLayoutManager(ProductLayoutManager);
                    holder.rv.setAdapter(productListWithoutImagesAdapter);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Product List Without Image");
                }
                if(position==3){
                    ProductListAdapter productListWithoutImagesAdapter=new ProductListAdapter(1);
                    LinearLayoutManager ProductLayoutManager=new LinearLayoutManager(mContext);
                    holder.rv.setLayoutManager(ProductLayoutManager);
                    holder.rv.setAdapter(productListWithoutImagesAdapter);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Product List with Images");
                }


        }



    }

    @Override
    public int getItemCount() {
        if(code==4)
            return 4;
        return 0;
    }

    public void setCode(int code){
        this.code = code;
    }

}
