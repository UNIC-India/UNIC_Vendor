package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.DoubleProductAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListWithoutImagesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.TripleImageAdapter;

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
        CardView cvDemo;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cvDemo=itemView.findViewById(R.id.cvDemo);
            rv=itemView.findViewById(R.id.rvDemo);
            rdbtn=itemView.findViewById(R.id.rdBtn);
            tvViewTitle=itemView.findViewById(R.id.tvViewTitle);
            rdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastchecked=getAdapterPosition();
                    notifyDataSetChanged();


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
            case 2:
                if(position==0){
                    CategoriesAdapter categoriesAdapter=new CategoriesAdapter(1);
                    LinearLayoutManager categoriesLayoutManager= new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);
                    holder.rv.setLayoutManager(categoriesLayoutManager);
                    holder.rv.setAdapter(categoriesAdapter);
                    holder.rv.setNestedScrollingEnabled(false);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Simple Categories Display");
                }
                break;
            case 4:

                if(position==0) {

                    DoubleProductAdapter doubleProductAdapter = new DoubleProductAdapter(1);
                    LinearLayoutManager doubleProductLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);

                    holder.rv.setLayoutManager(doubleProductLayoutManager);
                    holder.rv.setAdapter(doubleProductAdapter);
                    holder.rv.setNestedScrollingEnabled(false);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Triple Products");
                }
                if(position==1){
                    TripleImageAdapter tripleImageAdapter=new TripleImageAdapter(1);
                    LinearLayoutManager tripleProductLayoutManager=new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                    holder.rv.setLayoutManager(tripleProductLayoutManager);
                    holder.rv.setAdapter(tripleImageAdapter);
                    holder.rv.setNestedScrollingEnabled(false);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Double Products");
                }
                if(position==2){
                    ProductListWithoutImagesAdapter productListWithoutImagesAdapter=new ProductListWithoutImagesAdapter(1);
                    LinearLayoutManager ProductLayoutManager=new LinearLayoutManager(mContext);
                    holder.rv.setLayoutManager(ProductLayoutManager);
                    holder.rv.setNestedScrollingEnabled(false);
                    holder.rv.setAdapter(productListWithoutImagesAdapter);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Product List Without Image");
                }
                if(position==3){
                    ProductListAdapter productListWithoutImagesAdapter=new ProductListAdapter(1);
                    LinearLayoutManager ProductLayoutManager=new LinearLayoutManager(mContext);
                    holder.rv.setLayoutManager(ProductLayoutManager);
                    holder.rv.setNestedScrollingEnabled(false);
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
        else if(code==2)
            return 1;
        return 0;
    }

    public void setCode(int code){
        this.code = code;
    }

}