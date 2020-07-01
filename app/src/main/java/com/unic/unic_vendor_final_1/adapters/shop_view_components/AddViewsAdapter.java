package com.unic.unic_vendor_final_1.adapters.shop_view_components;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter2;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ImageViewAdapters.DoubleImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.DoubleProductAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.DoubleProductWithoutImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListWithoutImagesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.SliderViewAdapters.SliderAdapter;
import com.unic.unic_vendor_final_1.views.helpers.AutoScrollViewPager;

public class AddViewsAdapter extends RecyclerView.Adapter<AddViewsAdapter.ViewHolder> {

    public int lastchecked=-1;
    int code=0;
    Context mContext;


    public AddViewsAdapter(Context mContext){
        this.mContext=mContext;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl;
        RadioButton rdbtn;
        TextView tvViewTitle;
        CardView cvDemo;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cvDemo=itemView.findViewById(R.id.cvDemo);
            rl=itemView.findViewById(R.id.rlDemo);
            rdbtn=itemView.findViewById(R.id.rdBtn);
            tvViewTitle=itemView.findViewById(R.id.tvViewTitle);
            cvDemo.setOnClickListener(v->{
                if(lastchecked!=-1)
                    notifyItemChanged(lastchecked);
                lastchecked=getAdapterPosition();
                notifyItemChanged(getAdapterPosition());
            });
            rdbtn.setOnClickListener(v -> {
                if(lastchecked!=-1)
                    notifyItemChanged(lastchecked);
                lastchecked=getAdapterPosition();
                notifyItemChanged(getAdapterPosition());
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

        holder.rl.removeAllViews();

        switch(code){
            case 0:
                break;

            case 1:
                if ((position==0)){
                    DoubleImageAdapter doubleImageAdapter = new DoubleImageAdapter(1);
                    LinearLayoutManager doubleImagesLayoutManager = new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);

                    RecyclerView rv = new RecyclerView(mContext);
                    ((ViewGroup)holder.rl).addView(rv,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    rv.setLayoutManager(doubleImagesLayoutManager);
                    rv.setAdapter(doubleImageAdapter);
                    rv.setNestedScrollingEnabled(false);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Double Images");
                }
                break;

            case 2:
                if(position==0){
                    CategoriesAdapter2 categoriesAdapter=new CategoriesAdapter2(1);
                    LinearLayoutManager categoriesLayoutManager= new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);

                    RecyclerView rv = new RecyclerView(mContext);

                    ((ViewGroup)holder.rl).addView(rv,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    rv.setLayoutManager(categoriesLayoutManager);
                    rv.setAdapter(categoriesAdapter);
                    rv.setNestedScrollingEnabled(false);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Simple Categories Display");
                }
                else if(position==1){
                    CategoriesAdapter categoriesAdapter=new CategoriesAdapter(1);
                    RecyclerView.LayoutManager mLayoutManager= new GridLayoutManager(mContext,3);


                    RecyclerView rv = new RecyclerView(mContext);

                    ((ViewGroup)holder.rl).addView(rv,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    rv.setLayoutManager(mLayoutManager);
                    rv.setAdapter(categoriesAdapter);
                    rv.setNestedScrollingEnabled(false);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Triple Categories Display");
                }
                break;

            case 3:

                if(position==0){
                    SliderAdapter sliderAdapter = new SliderAdapter(getActivity(mContext),1);
                    View sliderView = getActivity(mContext).getLayoutInflater().inflate(R.layout.slider,holder.rl,false);
                    ((ViewGroup)holder.rl).addView(sliderView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    sliderView.findViewById(R.id.slider_images_tab).setVisibility(View.GONE);

                    AutoScrollViewPager viewPager = sliderView.findViewById(R.id.slider_images_flipper);

                    viewPager.startAutoScroll();
                    viewPager.setInterval(3000);
                    viewPager.setCycle(true);
                    viewPager.setStopScrollWhenTouch(true);

                    viewPager.setAdapter(sliderAdapter);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Simple Slider");

                }

                else if (position==1){
                    SliderAdapter sliderAdapter = new SliderAdapter(getActivity(mContext),1);
                    View sliderView = getActivity(mContext).getLayoutInflater().inflate(R.layout.slider,holder.rl,false);
                    ((ViewGroup)holder.rl).addView(sliderView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    AutoScrollViewPager viewPager = sliderView.findViewById(R.id.slider_images_flipper);

                    viewPager.startAutoScroll();
                    viewPager.setInterval(3000);
                    viewPager.setCycle(true);
                    viewPager.setStopScrollWhenTouch(true);

                    TabLayout sliderTabs = sliderView.findViewById(R.id.slider_images_tab);
                    sliderTabs.setupWithViewPager(viewPager);

                    viewPager.setAdapter(sliderAdapter);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Slider With Bottom Indicator");
                }

                break;
            case 4:

                if(position==0) {

                    DoubleProductAdapter doubleProductAdapter = new DoubleProductAdapter(1);
                    LinearLayoutManager doubleProductLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);

                    RecyclerView rv = new RecyclerView(mContext);

                    ((ViewGroup)holder.rl).addView(rv,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    rv.setLayoutManager(doubleProductLayoutManager);
                    rv.setAdapter(doubleProductAdapter);
                    rv.setNestedScrollingEnabled(false);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Products with Images");
                }
                if(position==1){
                    DoubleProductWithoutImageAdapter doubleProductWithoutImageAdapter =new DoubleProductWithoutImageAdapter(1);
                    LinearLayoutManager tripleProductLayoutManager=new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);

                    RecyclerView rv = new RecyclerView(mContext);

                    ((ViewGroup)holder.rl).addView(rv,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    rv.setLayoutManager(tripleProductLayoutManager);
                    rv.setAdapter(doubleProductWithoutImageAdapter);
                    rv.setNestedScrollingEnabled(false);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Products without Images");
                }
                if(position==2){
                    ProductListWithoutImagesAdapter productListWithoutImagesAdapter=new ProductListWithoutImagesAdapter(1);
                    LinearLayoutManager ProductLayoutManager=new LinearLayoutManager(mContext);

                    RecyclerView rv = new RecyclerView(mContext);

                    ((ViewGroup)holder.rl).addView(rv,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    rv.setLayoutManager(ProductLayoutManager);
                    rv.setNestedScrollingEnabled(false);
                    rv.setAdapter(productListWithoutImagesAdapter);
                    holder.rdbtn.setChecked(position==lastchecked);
                    holder.tvViewTitle.setText("Product List Without Image");
                }
                if(position==3){
                    ProductListAdapter productListWithImagesAdapter=new ProductListAdapter(1);
                    LinearLayoutManager ProductLayoutManager=new LinearLayoutManager(mContext);

                    RecyclerView rv = new RecyclerView(mContext);

                    ((ViewGroup)holder.rl).addView(rv,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    rv.setLayoutManager(ProductLayoutManager);
                    rv.setNestedScrollingEnabled(false);
                    rv.setAdapter(productListWithImagesAdapter);
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
            return 2;
        else if(code==3)
            return 2;
        else if(code==1)
            return 1;
        return 0;
    }

    public void setCode(int code){
        this.code = code;
    }

    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

}