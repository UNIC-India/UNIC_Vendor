package com.unic.unic_vendor_final_1.views.settings_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.TeamAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentTeamBinding;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;

import java.util.List;
import java.util.Map;

public class TeamFragment extends Fragment implements View.OnClickListener {
    FragmentTeamBinding teamBinding;
    UserShopsViewModel userShopsViewModel;
    TeamAdapter teamAdapter;
    String shopName;
    String shopId;
    int setter;
    String role="salesMan";

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
        teamAdapter=new TeamAdapter(getContext(),shopId);
        userShopsViewModel.getAllMembers(shopId).observe(getViewLifecycleOwner(), new Observer<List<Map<String, String>>>() {
            @Override
            public void onChanged(List<Map<String, String>> maps) {
                teamAdapter.setData(maps);
                teamAdapter.notifyDataSetChanged();
            }
        });
        teamBinding.rvTeam.setAdapter(teamAdapter);
        teamBinding.rvTeam.setLayoutManager(new LinearLayoutManager(getContext()));



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
                        setAdapters(setter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        setter=0;
                        setAdapters(setter);

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
                Window window= dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                break;
        }
    }
    public void setAdapters(int position){
        switch (position){
            case 0:
                this.role="salesMan";
                break;
            case 1:
                this.role="deliveryMan";
                break;
            case 2:
                this.role="dispatcher";
                break;
            case 3:
                this.role="preparer";
                break;
        }
    }
}
