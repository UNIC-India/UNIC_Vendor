package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentHomeBinding;
import com.unic.unic_vendor_final_1.datamodels.User;
import com.unic.unic_vendor_final_1.viewmodels.FirestoreDataViewModel;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.helpers.IntermediateShopList;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding fragmentHomeBinding;
    private UserShopsViewModel userShopsViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirestoreDataViewModel firestoreDataViewModel = new ViewModelProvider(this).get(FirestoreDataViewModel.class);
        userShopsViewModel = new ViewModelProvider(this).get(UserShopsViewModel.class);
        firestoreDataViewModel.getUserData();
        userShopsViewModel.titleSetter.setValue(0);
        firestoreDataViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user==null)
                    return;
                if(user.getFullName()==null)
                    return;
                fragmentHomeBinding.tvWorkspace.setText(user!=null?user.getFullName().split(" ")[0]+"'s"+" Workspace":"Workspace");
            }
        });
        firestoreDataViewModel.getError().observe(getViewLifecycleOwner(),e -> Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show());

        fragmentHomeBinding.cvMenuOrders.setOnClickListener(this);
        fragmentHomeBinding.cvMenuMyApps.setOnClickListener(this);
        fragmentHomeBinding.cvMenuSettings.setOnClickListener(this);
        fragmentHomeBinding.cvMenuQr.setOnClickListener(this);
        fragmentHomeBinding.cvMenuNotifications.setOnClickListener(this);
        fragmentHomeBinding.cvMenuMyProducts.setOnClickListener(this);
        fragmentHomeBinding.cvMenuReport.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cv_menu_orders:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new OrdersFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.cv_menu_my_apps:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new MyAppsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.cv_menu_qr:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new QRFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.cv_menu_notifications:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new NotificationsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            case R.id.cv_menu_my_products:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new IntermediateShopList())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.cv_menu_settings:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment,new SettingsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, new ComingSoon())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;

        }

    }
}
