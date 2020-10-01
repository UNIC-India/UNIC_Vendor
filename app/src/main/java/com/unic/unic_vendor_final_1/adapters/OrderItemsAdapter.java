package com.unic.unic_vendor_final_1.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.unic.unic_vendor_final_1.BuildConfig;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;

import java.util.List;
import java.util.Map;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    private Context mContext;
    private Order order;
    private Boolean isVisible=false;
    public Boolean reset=false;



    public OrderItemsAdapter(Context context){
        this.mContext = context;
    }

    private class AvailabilityListener implements View.OnClickListener{

        private int position;

        public AvailabilityListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (!reset) {
                if (((CheckBox)v).isChecked()) {
                    order.getItems().get(position).put("availability", -1);
                } else {
                    order.getItems().get(position).put("availability", 1);
                }
            }
            else{
                order.getItems().get(position).put("availability", 1);
            }

            notifyItemChanged(position);

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName,tvCompany,tvPrice, tvTotal,tvQty, tvExtraInfo1,tvExtraInfo2;
        CardView cdProduct;
        CheckBox checkBox;


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
            checkBox=itemView.findViewById(R.id.product_checkbox);



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

        holder.tvProductName.setText(order.getItems().get(position).get("name").toString());
        holder.tvCompany.setText(order.getItems().get(position).get("company").toString());
        holder.tvPrice.setText("\u20B9 "+order.getItems().get(position).get("price").toString());
        holder.tvTotal.setText("\u20B9 "+Double.parseDouble(order.getItems().get(position).get("price").toString())*Integer.parseInt(order.getItems().get(position).get("orderQuantity").toString())+"");
        holder.tvQty.setText(""+order.getItems().get(position).get("orderQuantity").toString());
        holder.tvExtraInfo1.setText((order.getItems().get(position).get("extraInfo1")!=null&&!order.getItems().get(position).get("extraInfo1").toString().equals("null"))?order.getItems().get(position).get("extraInfo1").toString():"");
        holder.tvExtraInfo2.setText((order.getItems().get(position).get("extraInfo2")!=null&&!order.getItems().get(position).get("extraInfo2").toString().equals("null"))?order.getItems().get(position).get("extraInfo2").toString():"");
        if(isVisible)
            holder.checkBox.setVisibility(View.VISIBLE);
        else
            holder.checkBox.setVisibility(View.GONE);
        if(order.getItems().get(position).get("availability")!=null){
            switch (Integer.parseInt(order.getItems().get(position).get("availability").toString())){
                case -1:
                    holder.cdProduct.setCardBackgroundColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.grey3)));
                    holder.checkBox.setChecked(true);
                    break;
                case 1:
                    holder.cdProduct.setCardBackgroundColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.white)));
                    holder.checkBox.setChecked(false);
                    break;

            }
        }
        else
            order.getItems().get(position).put("availability",1);
        holder.checkBox.setOnClickListener(new AvailabilityListener(position));

        holder.cdProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.home_fragment,new ProductDescriptionFragment(order.getItems().get(position)))
                        .commit();
            }
        });
        if(position==getItemCount()-1){
            reset=false;
        }







    }

    @Override
    public int getItemCount() {
        return order==null||order.getItems()==null?0:order.getItems().size();
    }

    public void setProducts(Order order){
        this.order = order;
    }

    public void setCheckBoxVisibility(Boolean isVisible){
        this.isVisible=isVisible;
    }

    public Order returnOrder(){
        return order;
    }

}