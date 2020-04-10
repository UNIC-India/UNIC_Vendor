package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.unic.unic_vendor_final_1.R;

import java.util.Objects;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }
    public void onCategorySelect1(View v){
        ImageView iv1=v.findViewById(R.id.imageView);
        iv1.setImageResource(R.drawable.ordercolumnafterclick);



        double secs = 0.05;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.home_fragment,new MyAppsFragment());
                ft.commit();



            }
        }, (int) (secs * 1000));

    }

    public void onCategorySelect2(View v){
        ImageView iv2=v.findViewById(R.id.imageView2);
        iv2.setImageResource(R.drawable.myappscolumnafterclick);



        double secs = 0.05;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.home_fragment,new MyAppsFragment());
                ft.commit();



            }
        }, (int) (secs * 1000));

    }
}
