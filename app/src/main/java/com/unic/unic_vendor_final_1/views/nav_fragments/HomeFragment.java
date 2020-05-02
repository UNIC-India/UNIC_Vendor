package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.content.Intent;
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
import com.unic.unic_vendor_final_1.databinding.FragmentHomeBinding;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding fragmentHomeBinding;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);
        fragmentHomeBinding.cardViewApps.setOnClickListener(this);
        fragmentHomeBinding.tvWorkspace.setText(((UserHome)Objects.requireNonNull(getActivity())).user!=null?((UserHome)Objects.requireNonNull(getActivity())).user.getFullName().split(" ")[0]+"'s"+" Workspace":"Workspace");
        fragmentHomeBinding.cardView.setOnClickListener(this);
        fragmentHomeBinding.cardView6.setOnClickListener(this);
        return fragmentHomeBinding.getRoot();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.card_view_apps){

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment,new MyAppsFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        else if(v.getId() == R.id.card_view5){

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment,new NotificationsFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        else if(v.getId() == R.id.card_view6){

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment,new MyProducts())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        else if(v.getId()==R.id.card_view){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment,new MyOrders())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }
}
