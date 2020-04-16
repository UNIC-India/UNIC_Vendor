package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.Adapter_setViews;
import com.unic.unic_vendor_final_1.databinding.FragmentViewSelectorBinding;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSelector extends Fragment {
    private int pageId,code;
    private SetStructureViewModel setStructureViewModel;
    private FragmentViewSelectorBinding viewSelectorBinding;
    public ViewSelector() {
        // Required empty public constructor
    }

    public ViewSelector(int pageId, int code){
        this.pageId=pageId;
        this.code=code;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewSelectorBinding=FragmentViewSelectorBinding.inflate(inflater,container,false);
        loadViews(code);
        return viewSelectorBinding.getRoot();
    }
    void loadViews(int code){
        Adapter_setViews adapter_setViews=new Adapter_setViews(getContext());
        adapter_setViews.setCode(code+1);
        switch (code){
            case 0:
                viewSelectorBinding.rvDemo1.setAdapter(adapter_setViews);
                viewSelectorBinding.rvDemo1.setLayoutManager(new LinearLayoutManager(getContext()));
                break;
            case 1:
                viewSelectorBinding.rvDemo1.setAdapter(adapter_setViews);
                viewSelectorBinding.rvDemo1.setLayoutManager(new LinearLayoutManager(getContext()));
                break;
            case 2:
                viewSelectorBinding.rvDemo1.setAdapter(adapter_setViews);
                viewSelectorBinding.rvDemo1.setLayoutManager(new LinearLayoutManager(getContext()));
                break;
            case 3:
                viewSelectorBinding.rvDemo1.setAdapter(adapter_setViews);
                viewSelectorBinding.rvDemo1.setLayoutManager(new LinearLayoutManager(getContext()));
                break;
            case 4:
                viewSelectorBinding.rvDemo1.setAdapter(adapter_setViews);
                viewSelectorBinding.rvDemo1.setLayoutManager(new LinearLayoutManager(getContext()));
                break;
        }
    }
}
