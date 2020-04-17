package com.unic.unic_vendor_final_1.views.helpers;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.Adapter_setViews;
import com.unic.unic_vendor_final_1.databinding.FragmentStagingArea1Binding;
import com.unic.unic_vendor_final_1.databinding.FragmentViewSelectorBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class StagingArea1 extends Fragment implements  View.OnClickListener {
    private int viewCode;
    int pageId;
    private SetStructureViewModel setStructureViewModel;
    private FragmentStagingArea1Binding stagingArea1Binding;
    Adapter_setViews adapter_setViews;
    public StagingArea1() {
        // Required empty public constructor
    }

    public StagingArea1(int pageId,int viewCode){
        this.pageId=pageId;
        this.viewCode=viewCode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        stagingArea1Binding=FragmentStagingArea1Binding.inflate(inflater,container,false);


        return stagingArea1Binding.getRoot();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }


}
