package com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ProductDescriptionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String,Object>> products = new ArrayList<>();
    private List<Map<String,Object>> checkedProducts = new ArrayList<>();
    int demo;
    int which;
    private SetStructureViewModel setStructureViewModel;

    public ProductListAdapter(Context context,int which){
        this.mContext = context;
        demo=0;
        this.which = which;
        setStructureViewModel = new ViewModelProvider((FragmentActivity)mContext).get(SetStructureViewModel.class);
    }
    public ProductListAdapter(int demo){
        this.demo=demo;
        which = 1;
    }
    class ProductDetailsListener implements View.OnClickListener {
        private  int position;

        ProductDetailsListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(which==1)
            ((AppCompatActivity)mContext).getSupportFragmentManager()
                    .beginTransaction().replace(R.id.shop_pages_loader,new ProductDescriptionFragment(products.get(position)))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();
            else if(which==2)
                ((AppCompatActivity)mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.home_fragment,new ProductDescriptionFragment(products.get(position)))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
        }
    }




private class CheckBoxListener implements View.OnClickListener {

        int position;

        CheckBoxListener(int position) {
            this.position = position;
        }

        public void onClick(View v) {

            if (((CheckBox) v).isChecked()) {
                checkedProducts.add(products.get(position));
            } else {
                checkedProducts.removeIf(map -> map.get("firestoreId").toString().equals(products.get(position).get("firestoreId").toString()));
            }
            notifyItemChanged(position);
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName,tvCompany,tvPrice,tvCategory;;
        ImageView ivProductPhoto;
        CheckBox cbCheck;
        ImageView addToCart,btnDelete;
        CardView bounding;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCheck = itemView.findViewById(R.id.product_checkbox);
            tvProductName = itemView.findViewById(R.id.product_name);
            ivProductPhoto = itemView.findViewById(R.id.product_image);
            tvCompany=itemView.findViewById(R.id.product_company_name);
            tvPrice=itemView.findViewById(R.id.product_price);
            tvCategory=itemView.findViewById(R.id.product_category);
            addToCart = itemView.findViewById(R.id.product_list_add_to_cart);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            bounding=itemView.findViewById(R.id.product_details_list_card);

            cbCheck.setChecked(false);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_details_list_item,parent,false);
        if(which==3||which==2) {
            view.findViewById(R.id.product_list_add_to_cart).setVisibility(View.GONE);
            view.findViewById(R.id.product_checkbox).setVisibility(View.GONE);
        }
        if(which==2){
            view.findViewById(R.id.btnDelete).setVisibility(View.VISIBLE);
        }
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
               holder.ivProductPhoto.setVisibility(View.GONE);
            Glide
                    .with(mContext)
                    .load(products.get(position).get("imageId").toString())
                    .into(holder.ivProductPhoto);
            holder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Product can only be added by a customer.", Toast.LENGTH_SHORT).show();

                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(mContext).setMessage("Are you sure you want to delete " +  products.get(position).get("name").toString())
                            .setPositiveButton("YES",((dialog, which1) -> {
                                setStructureViewModel.deleteProduct(products.get(position).get("shopId").toString(),products.get(position).get("firestoreId").toString());
                                dialog.dismiss();
                            }))
                            .setNegativeButton("NO",((dialog, which1) -> dialog.dismiss()))
                            .create().show();

                }
            });
            holder.bounding.setOnClickListener(new ProductDetailsListener(position));

            switch (which){
                case 1:
                    holder.cbCheck.setChecked(checkedProducts != null && checkedProducts.stream().anyMatch(map -> map.get("firestoreId").toString().equals(products.get(position).get("firestoreId").toString())));

                    holder.cbCheck.setOnClickListener(new CheckBoxListener(position));
                    break;
                case 2:
                case 3:
                    holder.cbCheck.setVisibility(View.GONE);
                    break;
            }
        }
        else{
            holder.tvProductName.setText("Product name " + (position+1));
            holder.tvCompany.setText("Company");
            holder.tvPrice.setText("1103");
            holder.ivProductPhoto.setImageResource(R.drawable.demo_product);
            holder.cbCheck.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(demo==0)
            return products==null?0:products.size();
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