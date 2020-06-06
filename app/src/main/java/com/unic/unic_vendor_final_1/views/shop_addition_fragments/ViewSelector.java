package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.Adapter_setViews;
import com.unic.unic_vendor_final_1.databinding.FragmentViewSelectorBinding;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;
import com.unic.unic_vendor_final_1.views.shop_addition_fragments.ShopPageFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSelector extends Fragment implements  View.OnClickListener {
    private int pageId,code;
    private SetStructureViewModel setStructureViewModel;
    private FragmentViewSelectorBinding viewSelectorBinding;
    private Adapter_setViews adapter_setViews;
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
        if(code==-1){
            viewAdder(Integer.valueOf((code+1)*10 + 0).toString());

        }
        else if(code==4){
            viewAdder(Integer.valueOf((code+1)*10 + 1).toString());
        }
        else {
            adapter_setViews = new Adapter_setViews(getContext());
            adapter_setViews.setCode(code + 1);
            viewSelectorBinding.rvDemo1.setAdapter(adapter_setViews);
            viewSelectorBinding.rvDemo1.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRight:
                viewAdder(Integer.valueOf((code+1)*10 + adapter_setViews.lastchecked+1).toString());
                break;
            case R.id.btnleft:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

        }
    }

    public void viewAdder(String code){
        switch (code){

            case "00":
            case "0":
                com.unic.unic_vendor_final_1.datamodels.View view00 = new com.unic.unic_vendor_final_1.datamodels.View();
                view00.setHeight(650);

                Map<String, Object> defaultval=new HashMap<>();
                defaultval.put("default",1);
                view00.getData().add(defaultval);
                Toast.makeText(getActivity(), "View Added!", Toast.LENGTH_SHORT).show();
                ((SetShopStructure)getActivity()).addView(pageId,view00,0);
                break;

            case "11":
                com.unic.unic_vendor_final_1.datamodels.View view12 = new com.unic.unic_vendor_final_1.datamodels.View();
                view12.setHeight(210);
                view12.setFields("imageLink,tag");
                ((SetShopStructure)getActivity()).addView(pageId,view12,12);
                break;

            case "21":
                com.unic.unic_vendor_final_1.datamodels.View view21 = new com.unic.unic_vendor_final_1.datamodels.View();
                view21.setFields("cname");
                view21.setHeader("Categories");
                view21.setHeight(47);
                ((SetShopStructure) Objects.requireNonNull(getActivity())).addView(pageId, view21,21);
                break;
            case "22":
                com.unic.unic_vendor_final_1.datamodels.View view22 = new com.unic.unic_vendor_final_1.datamodels.View();
                view22.setFields("cname");
                view22.setHeader("Categories");
                view22.setHeight(47);
                ((SetShopStructure) Objects.requireNonNull(getActivity())).addView(pageId, view22,22);
                break;

            case "31":

                com.unic.unic_vendor_final_1.datamodels.View view31 = new com.unic.unic_vendor_final_1.datamodels.View();
                view31.setFields("imageLink,tag");
                view31.setHeight(200);
                ((SetShopStructure)getActivity()).addView(pageId,view31,31);
                break;

            case "32":
                com.unic.unic_vendor_final_1.datamodels.View view32 = new com.unic.unic_vendor_final_1.datamodels.View();
                view32.setFields("imageLink,tag");
                view32.setHeight(250);
                ((SetShopStructure)getActivity()).addView(pageId,view32,32);
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
                        view.setHeight(200);
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
                        view.setHeight(185);
                        view.setFields("name,company,price");
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
                view51.setFields("text");

                view51.setHeight(100);
                ((SetShopStructure) Objects.requireNonNull(getActivity())).addView(pageId, view51,51);
                break;

        }
    }

    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
