package com.unic.unic_vendor_final_1.views.helpers;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.databinding.FragmentTeamBinding;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

public class TeamFragment extends Fragment implements View.OnClickListener {
    FragmentTeamBinding teamBinding;
    UserShopsViewModel userShopsViewModel;
    String shopName;
    String shopId;
    int setter;
    String role="SalesMan";

    public TeamFragment() {
        // Required empty public constructor
    }

    public TeamFragment(String shopId, String shopName){
        this.shopId=shopId;
        this.shopName=shopName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        teamBinding=FragmentTeamBinding.inflate(getLayoutInflater(),container,false);
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        teamBinding.btnAddTeamMember.setOnClickListener(this);


        return teamBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAddTeamMember:

                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.member_dialog);
                ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(), R.array.roles,android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((Spinner)dialog.findViewById(R.id.spinnerRole)).setAdapter(adapter);
                ((Spinner)dialog.findViewById(R.id.spinnerRole)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setter=position;
                        setAdapters(setter,role);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        setter=0;
                        setAdapters(setter,role);

                    }
                });

                dialog.findViewById(R.id.btncancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      userShopsViewModel.addMember(((EditText)dialog.findViewById(R.id.editText)).getText().toString(),role,shopId);
                    }
                });
                dialog.show();
                break;
        }
    }
    public void setAdapters(int position,String role){
        switch (position){
            case 0:
                role="SalesMan";
                break;
            case 1:
                role="Delivery Man";
                break;
            case 2:
                role="Dispatcher";
                break;
            case 3:
                role="Preparer";
                break;
        }
    }
}
