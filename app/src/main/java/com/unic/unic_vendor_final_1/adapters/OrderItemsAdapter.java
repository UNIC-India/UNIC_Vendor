package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;

import java.util.List;
import java.util.Map;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> products;
    private List<Integer> qty;


    public OrderItemsAdapter(Context context){
        this.mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName,tvCompany,tvPrice, tvTotal,tvQty, tvExtraInfo1,tvExtraInfo2;
        CardView cdProduct;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvProductName = itemView.findViewById(R.id.product_name);
            tvCompany=itemView.findViewById(R.id.product_company_name);
            tvPrice=itemView.findViewById(R.id.product_price);
            tvTotal=itemView.findViewById(R.id.tvTotal);
            tvQty=itemView.findViewById(R.id.tvQty);
            cdProduct=itemView.findViewById(R.id.cdProduct);
            tvExtraInfo1=itemView.findViewById(R.id.tvExtraInfo1);
            tvExtraInfo2=itemView.findViewById(R.id.tvExtraInfo2);

        }
    }

    @NonNull
    @Override
    public OrderItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_list_item,parent,false);
        return new OrderItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsAdapter.ViewHolder holder, int position) {

        holder.tvProductName.setText(products.get(position).get("name").toString());
        holder.tvCompany.setText(products.get(position).get("company").toString());
        holder.tvPrice.setText("Rs "+products.get(position).get("price").toString());
        holder.tvTotal.setText(Double.parseDouble(products.get(position).get("price").toString())*qty.get(position)+"");
        holder.tvQty.setText(""+qty.get(position));
        holder.tvExtraInfo1.setText(products.get(position).get("ExtraInfo1")!=null?products.get(position).get("ExtraInfo1").toString():"");
        holder.tvExtraInfo2.setText(products.get(position).get("ExtraInfo2")!=null?products.get(position).get("ExtraInfo2").toString():"");

        holder.cdProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.home_fragment,new ProductDescriptionFragment(products.get(position)))
                        .commit();
            }
        });





    }

    @Override
    public int getItemCount() {
        return products.size();

    }

    public void setProducts(List<Map<String,Object>> products,List<Integer> qty){
        this.products = products;
        this.qty=qty;

    }

}