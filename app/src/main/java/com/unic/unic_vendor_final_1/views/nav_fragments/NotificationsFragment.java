package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.activities.SetNotification;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button btnNotify=container.findViewById(R.id.btnNotify);
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SetNotification.class));
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);


    }

    public void sendNotification(){}

}
