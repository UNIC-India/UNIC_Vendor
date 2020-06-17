package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String,Object>> products = new ArrayList<>();
    private List<Map<String,Object>> checkedProducts = new ArrayList<>();
    int demo;
    int which;

    public ProductListAdapter(Context context,int which){
        this.mContext = context;
        demo=0;
        this.which = which;
    }
    public ProductListAdapter(int demo){
        this.demo=demo;
        which = 1;
    }

    class CheckBoxListener implements View.OnClickListener {

        int position;

        CheckBoxListener(int position) {
            this.position = position;
        }

        public void onClick(View v) {



            if (((CheckBox) v).isChecked()) {
                checkedProducts.add(products.get(position));
            } else {
                if(checkedProducts.contains(products.get(position)))
                    checkedProducts.remove(products.get(position));            }
            notifyDataSetChanged();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName,tvCompany,tvPrice,tvCategory;;
        ImageView ivProductPhoto,imageView2;
        CheckBox cbCheck;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCheck = itemView.findViewById(R.id.product_checkbox);
            tvProductName = itemView.findViewById(R.id.product_name);
            ivProductPhoto = itemView.findViewById(R.id.product_image);
            tvCompany=itemView.findViewById(R.id.product_company_name);
            tvPrice=itemView.findViewById(R.id.product_price);
            tvCategory=itemView.findViewById(R.id.product_category);
            imageView2=itemView.findViewById(R.id.product_image);
            cbCheck.setChecked(false);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_details_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if(demo==0){
            holder.tvProductName.setText(products.get(position).get("name").toString());
            holder.tvPrice.setText("\u20B9"+products.get(position).get("price").toString());
            holder.tvCompany.setText(products.get(position).get("category").toString());
            holder.tvCategory.setText(products.get(position).get("company").toString());
           if(products.get(position).get("imageId").toString().length()<=3)
               holder.imageView2.setVisibility(View.GONE);
            Glide
                    .with(mContext)
                    .load(products.get(position).get("imageId").toString())
                    .into(holder.ivProductPhoto);

            switch (which){
                case 1:
                    if(checkedProducts!=null&&checkedProducts.contains(products.get(position)))
                        holder.cbCheck.setChecked(true);
                    else
                        holder.cbCheck.setChecked(false);

                    holder.cbCheck.setOnClickListener(new CheckBoxListener(position));
                    break;
                case 2:
                    holder.cbCheck.setVisibility(View.GONE);
                    holder.imageView2.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.cbCheck.setVisibility(View.GONE);
                    break;
            }
        }
        else{
            holder.tvProductName.setText("Demo Product");
            holder.ivProductPhoto.setImageResource(R.drawable.demo_product);
            holder.cbCheck.setVisibility(View.GONE);
            holder.imageView2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(demo==0)
            if(products!=null)
                return products.size();
            else
                return 0;
        else
            return 3;
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }

    public List<Map<String,Object>> returnSelectedProducts(){
        return checkedProducts;
    }


    public void  setSelectedProducts(List<Map<String,Object>> productIDs){
        if(productIDs!=null)
            this.checkedProducts = productIDs;
    }
}