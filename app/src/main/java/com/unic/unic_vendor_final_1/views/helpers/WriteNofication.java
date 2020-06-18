package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.commons.Helpers;
import com.unic.unic_vendor_final_1.databinding.FragmentWriteNoficationBinding;
import com.unic.unic_vendor_final_1.datamodels.Notification;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.nav_fragments.HomeFragment;
import com.unic.unic_vendor_final_1.views.nav_fragments.NotificationsFragment;


public class WriteNofication extends Fragment implements View.OnClickListener{
    FragmentWriteNoficationBinding writeNoficationBinding;
    UserShopsViewModel userShopsViewModel;
    String shopId;
    String shopName;
    public WriteNofication() {
        // Required empty public constructor
    }
    public WriteNofication(String shopId, String shopName){
        this.shopId=shopId;
        this.shopName=shopName;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        writeNoficationBinding=FragmentWriteNoficationBinding.inflate(getLayoutInflater(),container,false);
        writeNoficationBinding.loading.setVisibility(View.GONE);
        userShopsViewModel= new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        writeNoficationBinding.tvShopName.setText("Name: "+shopName);
        writeNoficationBinding.btncancel.setOnClickListener(this);
        writeNoficationBinding.btnsend.setOnClickListener(this);

        userShopsViewModel.notificationStatus.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1) {
                    writeNoficationBinding.loading.setVisibility(View.GONE);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.home_fragment, new NotificationsFragment())
                            .commit();
                }
                else if(integer==0)
                    writeNoficationBinding.loading.setVisibility(View.VISIBLE);
            }
        });


        return writeNoficationBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btncancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnsend:
                String title=writeNoficationBinding.etTitle.getText().toString();
                String message=writeNoficationBinding.etMessage.getText().toString();
                writeNoficationBinding.btnsend.setEnabled(false);
                userShopsViewModel.sendNotification(new Notification(title,shopId,message));
                Toast.makeText(getActivity(), "Notification sent", Toast.LENGTH_SHORT).show();


                break;
        }
    }
}
