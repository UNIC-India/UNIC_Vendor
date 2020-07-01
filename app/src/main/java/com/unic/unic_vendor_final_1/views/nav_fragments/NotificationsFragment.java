package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.NotificationsAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentNotificationsBinding;
import com.unic.unic_vendor_final_1.datamodels.Notification;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.helpers.IntermediateShopList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {
    FragmentNotificationsBinding notificationsBinding;
    UserShopsViewModel userShopsViewModel;
    NotificationsAdapter notificationsAdapter;
    int from;
    public NotificationsFragment() {

    }




    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationsBinding=FragmentNotificationsBinding.inflate(inflater,container,false);
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);

        userShopsViewModel.notificationStatus.setValue(2);
        notificationsAdapter=new NotificationsAdapter(getContext());
        userShopsViewModel.getNotifications().observe(getViewLifecycleOwner(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                if(notifications==null||notifications.size()==0){
                    notificationsBinding.ivNoNotifications.setVisibility(View.VISIBLE);
                    notificationsBinding.tvNoNotifications.setVisibility(View.VISIBLE);
                }
                else{
                    notificationsBinding.ivNoNotifications.setVisibility(View.GONE);
                    notificationsBinding.tvNoNotifications.setVisibility(View.GONE);
                }
                if(notifications!=null) {
                    notificationsAdapter.setNotifications(notifications);
                    notificationsAdapter.notifyDataSetChanged();

                }
            }
        });
        notificationsBinding.rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationsBinding.rvNotifications.setAdapter(notificationsAdapter);

        notificationsBinding.btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.home_fragment,new IntermediateShopList(1))
                        .addToBackStack(null)
                        .commit();
            }
        });
        // Inflate the layout for this fragment
        return notificationsBinding.getRoot();


    }



}