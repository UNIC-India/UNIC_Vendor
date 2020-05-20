package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentHomeBinding;
import com.unic.unic_vendor_final_1.datamodels.User;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding fragmentHomeBinding;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);
        fragmentHomeBinding.cardViewApps.setOnClickListener(this);
        FirestoreDataViewModel firestoreDataViewModel = new ViewModelProvider(this).get(FirestoreDataViewModel.class);
        firestoreDataViewModel.getUserData();
        firestoreDataViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                fragmentHomeBinding.tvWorkspace.setText(user!=null?user.getFullName().split(" ")[0]+"'s"+" Workspace":"Workspace");
            }
        });
        fragmentHomeBinding.cardView.setOnClickListener(this);
        fragmentHomeBinding.cardView6.setOnClickListener(this);
        fragmentHomeBinding.cardView3.setOnClickListener(this);
        fragmentHomeBinding.cardViewQr.setOnClickListener(this);
        fragmentHomeBinding.cardView5.setOnClickListener(this);
        fragmentHomeBinding.cardView7.setOnClickListener(this);
        return fragmentHomeBinding.getRoot();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.card_view:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new OrdersFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            case R.id.card_view_apps:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new MyAppsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            case R.id.card_view_qr:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new QRFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            case R.id.card_view5:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new NotificationsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            case R.id.card_view6:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new OrdersFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            default:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, new ComingSoon())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;

        }

    }
}
