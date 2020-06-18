package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;

import java.util.List;
import java.util.Map;

public class DoubleProductwoImageAdapter extends RecyclerView.Adapter<DoubleProductwoImageAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> products;
    int demo=0;

    public DoubleProductwoImageAdapter(Context context){
        this.mContext = context;
    }
    public DoubleProductwoImageAdapter(int demo){

        this.demo=demo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName;
        TextView tvProductPrice,tvCompany,tvDiscount,tvWithoutDiscount,tvCategory, tvExtraInfo1;
        Button addToCart;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvProductName = itemView.findViewById(R.id.double_product_name);
            tvProductPrice = itemView.findViewById(R.id.double_product_price);
            addToCart = itemView.findViewById(R.id.double_product_cart);
            tvCompany=itemView.findViewById(R.id.tvCompany);
            tvDiscount=itemView.findViewById(R.id.tvDiscount);
            tvWithoutDiscount=itemView.findViewById(R.id.tvWithoutDiscount);
            tvCategory=itemView.findViewById(R.id.tvCategory);
            tvExtraInfo1=itemView.findViewById(R.id.tvExtraInfo1);




        }
    }

    @NonNull
    @Override
    public DoubleProductwoImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_product_without_image_view_item,parent,false);
        return new DoubleProductwoImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoubleProductwoImageAdapter.ViewHolder holder, int position) {
        if(demo==0){
            if(products.get(position).get("discount")!=null && (Double.parseDouble(products.get(position).get("discount").toString()))!=0.0){
                holder.tvDiscount.setText(products.get(position).get("discount").toString()+"% OFF");
                holder.tvDiscount.setVisibility(View.VISIBLE);
                holder.tvWithoutDiscount.setText("\u20B9"+products.get(position).get("price").toString());
                holder.tvWithoutDiscount.setPaintFlags(holder.tvWithoutDiscount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvProductPrice.setText("\u20B9"+(Double.parseDouble(products.get(position).get("price").toString())*(100-Double.parseDouble(products.get(position).get("discount").toString()))/100)+"");
            }
            else{
                holder.tvDiscount.setText("");
                holder.tvDiscount.setVisibility(View.VISIBLE);
                holder.tvProductPrice.setText("\u20B9"+products.get(position).get("price").toString());
                holder.tvProductPrice.setPaintFlags(holder.tvWithoutDiscount.getPaintFlags());

            }
            holder.tvProductName.setText(products.get(position).get("name").toString());
            if(products.get(position).get("name").toString().length()>20) {
                holder.tvProductName.setTextSize(12);
                holder.tvProductName.setMaxLines(2);
            }
            holder.tvCompany.setText(products.get(position).get("company").toString());
            holder.tvExtraInfo1.setText((products.get(position).get("extraInfo1")!=null&&!products.get(position).get("extraInfo1").toString().equals("null"))?products.get(position).get("extraInfo1").toString():"");
            holder.tvCategory.setText((products.get(position).get("extraInfo2")!=null&&!products.get(position).get("extraInfo2").toString().equals("null"))?products.get(position).get("extraInfo2").toString():"category: "+products.get(position).get("category").toString());



            holder.tvProductName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity)mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.tvExtraInfo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity)mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.tvCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity)mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.tvCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity)mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });

            holder.tvProductPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity)mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                }
            });
            holder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Product can only be added by a customer.", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else{
            holder.tvProductName.setText("Product Name"+position);
            holder.tvProductPrice.setText("\u20B92104");
            holder.tvCategory.setText("Category");
            holder.tvExtraInfo1.setText("ExtraInfo1");
            holder.tvCompany.setText("Company"+position);
            holder.tvDiscount.setText("20% OFF");
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvWithoutDiscount.setText("\u20B92524.8");
            holder.tvWithoutDiscount.setPaintFlags(holder.tvWithoutDiscount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }



    }

    @Override
    public int getItemCount() {
        if(demo==0)
            if(products!=null)
                return products==null?0:products.size();
            else
                return 0;
        else
            return 4;
    }

    public void setProducts(List<Map<String,Object>> products){
        this.products = products;
    }

}