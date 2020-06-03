package com.unic.unic_vendor_final_1.adapters.shop_view_components.SliderViewAdapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.commons.BlurBuilder;

import java.util.List;
import java.util.Map;

public class SliderAdapter extends PagerAdapter {

    private Activity activity;
    private List<Map<String,Object>> data;
    private int demo = 0;

    private int demoImages[] = {R.drawable.slider_1,R.drawable.slider_2,R.drawable.slider_3};

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
            ((ImageView)viewItem.findViewById(R.id.slider_foreground)).setImageResource(demoImages[position]);
        }

        else {

            Glide.with(activity)
                    .asBitmap()
                    .load(data.get(position).get("imageLink"))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            ((ImageView)viewItem.findViewById(R.id.slider_foreground)).setImageBitmap(resource);

                            Bitmap stretchedBitmap = BlurBuilder.blur(activity.getBaseContext(),resource);

                            ((ImageView)viewItem.findViewById(R.id.slider_background)).setImageBitmap(stretchedBitmap);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
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
