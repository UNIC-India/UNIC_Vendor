package com.unic.unic_vendor_final_1.views.helpers;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.Adapter_setViews;
import com.unic.unic_vendor_final_1.databinding.FragmentViewSelectorBinding;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ShopPageFragment;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSelector extends Fragment implements  View.OnClickListener {
    private int pageId,code;
    private SetStructureViewModel setStructureViewModel;
    private FragmentViewSelectorBinding viewSelectorBinding;
    Adapter_setViews adapter_setViews;
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
        SetStructureViewModel setStructureViewModel=new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        setStructureViewModel.setCurrentFrag(getActivity().getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader));
        return viewSelectorBinding.getRoot();
    }


    void loadViews(int code){
        adapter_setViews=new Adapter_setViews(getContext());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRight:
                viewAdder((code+1)+""+(adapter_setViews.lastchecked+1));

        }
    }

    public void viewAdder(String code){
        switch (code){
            case "21":
                com.unic.unic_vendor_final_1.datamodels.View view21 = new com.unic.unic_vendor_final_1.datamodels.View();
                view21.setFields("cname");
                ((SetShopStructure) Objects.requireNonNull(getActivity())).addView(pageId, view21,21);
                break;
            case "41":
                final EditText etViewHeader = new EditText(getContext());
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter View Title");
                builder.setMessage("");
                builder.setView(etViewHeader);
                builder.setPositiveButton("DONE", (dialog, which) -> {
                    if (etViewHeader.getText().toString().trim().length()>0) {
                        com.unic.unic_vendor_final_1.datamodels.View view = new com.unic.unic_vendor_final_1.datamodels.View();
                        view.setHeight(260);
                        view.setFields("name,imageId,price");
                        view.setHeader(etViewHeader.getText().toString().trim());
                        ((SetShopStructure) Objects.requireNonNull(getActivity())).addView(pageId, view,41);
                    }
                });
                builder.setNegativeButton("CANCEL", (dialog, which) -> {
                });
                AlertDialog dialog  = builder.create();
                dialog.show();
                break;
            case "42":
                final EditText etViewHeader2 = new EditText(getContext());
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                builder2.setTitle("Enter View Title");
                builder2.setMessage("");
                builder2.setView(etViewHeader2);
                builder2.setPositiveButton("DONE", (dialog2, which) -> {
                    if (etViewHeader2.getText().toString().trim().length()>0) {
                        com.unic.unic_vendor_final_1.datamodels.View view = new com.unic.unic_vendor_final_1.datamodels.View();
                        view.setHeight(260);
                        view.setFields("name,imageId,price");
                        view.setHeader(etViewHeader2.getText().toString().trim());
                        ((SetShopStructure) Objects.requireNonNull(getActivity())).addView(pageId, view,42);
                    }
                });
                builder2.setNegativeButton("CANCEL", (dialog2, which) -> {
                });
                AlertDialog dialog2  = builder2.create();
                dialog2.show();
                break;
            case "51":
                com.unic.unic_vendor_final_1.datamodels.View view51 = new com.unic.unic_vendor_final_1.datamodels.View();
                view51.setFields("Text");
                ((SetShopStructure) Objects.requireNonNull(getActivity())).addView(pageId, view51,51);
                break;

        }
    }
}
