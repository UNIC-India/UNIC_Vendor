package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class CategorySelector extends Fragment implements  View.OnClickListener {
    private FragmentCategorySelectorBinding categorySelectorBinding;
    private SetStructureViewModel setStructureViewModel;
    private int pageId;
    private int viewCode;
    private CategorySelectionAdapter categorySelectionAdapter;
    private List<Map<String,Object>> categories;
    private List<Map<String,Object>> prevData;
    public CategorySelector() {
        // Required empty public constructor
    }
    public CategorySelector(int pageId,int viewCode){
        this.pageId=pageId;
        this.viewCode=viewCode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categorySelectorBinding=FragmentCategorySelectorBinding.inflate(inflater,container,false);
        setStructureViewModel= new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        prevData = setStructureViewModel.getStructure().getValue().getPage(pageId).getView(viewCode).getData();
        categorySelectionAdapter=new CategorySelectionAdapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        categorySelectorBinding.productsSelectorRecyclerView.setLayoutManager(layoutManager);
        categorySelectorBinding.productsSelectorRecyclerView.setAdapter(categorySelectionAdapter);
        categorySelectorBinding.btnConfirmProducts.setOnClickListener(this);

        return categorySelectorBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_confirm_products){
            prevData = categorySelectionAdapter.returnSelectedCategories();
            Structure structure = setStructureViewModel.getStructure().getValue();
            structure.updateProductList(pageId,viewCode,prevData);
            setStructureViewModel.setStructure(structure);
            ((SetShopStructure) Objects.requireNonNull(getActivity())).returnToPage(pageId);
        }
    }
}
