package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategorySelectionAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentCategorySelectorBinding;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategorySelector extends Fragment implements  View.OnClickListener, AdapterView.OnItemSelectedListener {
    private FragmentCategorySelectorBinding categorySelectorBinding;
    private SetStructureViewModel setStructureViewModel;
    private int pageId,code;
    private com.unic.unic_vendor_final_1.datamodels.View view;
    private CategorySelectionAdapter categorySelectionAdapter, companySelectionAdapter;
    private List<Map<String,Object>> categories;
    private List<Map<String,Object>> prevData;
    int setter=0;
    boolean isFirst = true;
    public CategorySelector() {
        // Required empty public constructor
    }
    public CategorySelector(int pageId, com.unic.unic_vendor_final_1.datamodels.View view, int code){
        this.pageId=pageId;
        this.view = view;
        this.code = code;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categorySelectorBinding=FragmentCategorySelectorBinding.inflate(inflater,container,false);
        setStructureViewModel= new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);


        isFirst = view.getViewCode() == 0;
        prevData = view.getData();
        setStructureViewModel.setCurrentFrag(getActivity().getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader));

        categorySelectionAdapter=new CategorySelectionAdapter(getContext());
        companySelectionAdapter=new CategorySelectionAdapter(getContext());
        companySelectionAdapter.showCompanies=true;

        setStructureViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Map<String, Object>>>() {
            @Override
            public void onChanged(List<Map<String, Object>> maps) {
               categorySelectionAdapter.setCategories(maps);
                categorySelectionAdapter.notifyDataSetChanged();
            }
        });

       setStructureViewModel.getCompanies().observe(getViewLifecycleOwner(), new Observer<List<Map<String, Object>>>() {
           @Override
           public void onChanged(List<Map<String, Object>> maps) {
               companySelectionAdapter.setCategories(maps);
               companySelectionAdapter.notifyDataSetChanged();
           }
       });

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(), R.array.category_or_company,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySelectorBinding.companyorcategory.setAdapter(adapter);
        categorySelectorBinding.companyorcategory.setOnItemSelectedListener(this);
        return categorySelectorBinding.getRoot();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setter=position;
        setAdapter(setter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setter=0;
        setAdapter(setter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void setAdapter(int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        switch (position) {
            case 0:
                categorySelectorBinding.productsSelectorRecyclerView.setLayoutManager(layoutManager);
                categorySelectorBinding.productsSelectorRecyclerView.setAdapter(categorySelectionAdapter);
                break;
            case 1:
                categorySelectorBinding.productsSelectorRecyclerView.setLayoutManager(layoutManager);
                categorySelectorBinding.productsSelectorRecyclerView.setAdapter(companySelectionAdapter);
                break;


        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnRight){
            Structure structure = setStructureViewModel.getStructure().getValue();
            if(setter==0)
             prevData = categorySelectionAdapter.returnSelectedCategories();
            else {
                prevData = companySelectionAdapter.returnSelectedCategories();
                view.setFields("compname");
            }
            view.setData(prevData);
            if(isFirst){
                structure.getPage(pageId).addNewView(view,code);
            }
            else
                structure.updateProductList(pageId,view.getViewCode(),prevData);
            setStructureViewModel.setStructure(structure);
            ((SetShopStructure) Objects.requireNonNull(getActivity())).returnToPage(pageId);
        }

        else if(v.getId()==R.id.btnleft){
           getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
