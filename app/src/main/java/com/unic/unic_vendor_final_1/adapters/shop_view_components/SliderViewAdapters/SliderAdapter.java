package com.unic.unic_vendor_final_1.adapters.shop_view_components.SliderViewAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.unic.unic_vendor_final_1.R;

import java.util.List;
import java.util.Map;

public class SliderAdapter extends PagerAdapter {

    private Activity activity;
    private List<Map<String,Object>> data;
    private int demo = 0;

    public SliderAdapter(Activity activity, List<Map<String,Object>> data){
        this.activity = activity;
        this.data = data;
        this.demo = 0;
    }

    public SliderAdapter(Activity activity, int demo){
        this.activity = activity;
        this.demo = demo;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = ((Activity)activity).getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.slider_image_view_item,container,false);

        if(demo==1){
            ((ImageView)viewItem).setImageResource(R.drawable.slider_1);
        }

        else {

            Glide
                    .with(activity)
                    .load(data.get(position).get("imageLink"))
                    .into((ImageView) viewItem);
        }

        container.addView(viewItem);

        return viewItem;

    }

    @Override
    public int getCount() {

        if (demo==1)
            return 3;

        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }


}
