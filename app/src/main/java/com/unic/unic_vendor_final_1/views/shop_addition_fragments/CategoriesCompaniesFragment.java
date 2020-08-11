package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesCompaniesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentCategoriesCompaniesBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesCompaniesFragment extends Fragment {
    private String key;
    private List<String> subKeys;
    private int type;
    private String currentKey;
    private SetStructureViewModel setStructureViewModel;
    private FragmentCategoriesCompaniesBinding categoriesCompaniesBinding;
    private ProductListAdapter productListAdapter;
    private CategoriesCompaniesAdapter categoriesCompaniesAdapter;
    LinearLayoutManager linearLayoutManager;
    private List<Map<String,Object>> data;
    private List<Map<String,Object>> searchResults;
    public CategoriesCompaniesFragment() {
        // Required empty public constructor
    }
    public CategoriesCompaniesFragment(String key, List<String> subKeys, int type){
        this.key = key;
        this.subKeys = subKeys;
        this.type = type;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        categoriesCompaniesBinding= FragmentCategoriesCompaniesBinding.inflate(getLayoutInflater());
        setStructureViewModel= new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        productListAdapter=new ProductListAdapter(getContext(),3);
        categoriesCompaniesAdapter = new CategoriesCompaniesAdapter(getContext(),type);
        categoriesCompaniesAdapter.setSubKeys(subKeys);
        categoriesCompaniesAdapter.setKey(key);

        setStructureViewModel.getCurrentKey().setValue(null);

        linearLayoutManager = new LinearLayoutManager(getContext());
        categoriesCompaniesBinding.rvCategory.setLayoutManager(linearLayoutManager);

        if(type==0)
            setStructureViewModel.getPaginatedCategoryProducts(key,true,null);
        if (type==1)
            setStructureViewModel.getPaginatedCompanyProducts(key,true,null);

        setStructureViewModel.getCategoryProducts().observe(getViewLifecycleOwner(),maps -> {
            if(maps==null)
                return;
            data = maps;
            productListAdapter.setProducts(data);

            if(subKeys.size()==1){
                categoriesCompaniesBinding.rvCategory.setAdapter(productListAdapter);
                productListAdapter.notifyDataSetChanged();
            }
            else if(subKeys.size()>1&&data.size()>20){
                categoriesCompaniesBinding.rvCategory.setAdapter(categoriesCompaniesAdapter);
                productListAdapter.notifyDataSetChanged();
            }
            else{
                categoriesCompaniesBinding.rvCategory.setAdapter(productListAdapter);
                productListAdapter.notifyDataSetChanged();
            }

        });

        setStructureViewModel.getSearchResults().observe(getViewLifecycleOwner(),this::setSearchResults);

        setStructureViewModel.getCurrentKey().observe(getViewLifecycleOwner(),s -> {
            if(s==null){
                return;
            }
            categoriesCompaniesBinding.rvCategory.setAdapter(productListAdapter);
        });

        categoriesCompaniesBinding.keySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(categoriesCompaniesBinding.rvCategory.getAdapter()!=null) {

                    if (categoriesCompaniesBinding.rvCategory.getAdapter().getClass() == ProductListAdapter.class) {
                        if (s.length() == 0) {
                            if (data.size() > 20 && subKeys.size() > 1) {
                                productListAdapter.setProducts(searchResults);
                                productListAdapter.notifyDataSetChanged();
                                return;
                            }
                            productListAdapter.setProducts(data);
                            productListAdapter.notifyDataSetChanged();
                        }
                        if (s.length() > 0) {
                            if (data.size() > 20 && subKeys.size() > 1) {
                                refineSearchResults(2, s.toString());
                            } else
                                refineSearchResults(1, s.toString());
                        }

                    }

                    if (categoriesCompaniesBinding.rvCategory.getAdapter().getClass() == CategoriesCompaniesAdapter.class) {
                        if (s.length() == 0) {
                            categoriesCompaniesAdapter.setSubKeys(subKeys);
                            categoriesCompaniesAdapter.notifyDataSetChanged();
                        }
                        if (s.length() > 0) {
                            List<String> refinedSubKeys = new ArrayList<>();
                            subKeys.forEach(subKey -> {
                                if (subKey.toLowerCase().contains(s.toString().toLowerCase()))
                                    refinedSubKeys.add(subKey);
                            });
                            categoriesCompaniesAdapter.setSubKeys(refinedSubKeys);
                            categoriesCompaniesAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return categoriesCompaniesBinding.getRoot();
    }
    public void setSearchResults(List<Map<String, Object>> searchResults) {
        this.searchResults = searchResults;
        if(searchResults==null) {
            if(data==null)
                return;
            if(subKeys.size()==1||data.size()<21) {
                productListAdapter.setProducts(data);
                productListAdapter.notifyDataSetChanged();
                categoriesCompaniesBinding.rvCategory.setAdapter(productListAdapter);
            }
            else
                categoriesCompaniesBinding.rvCategory.setAdapter(categoriesCompaniesAdapter);
            return;
        }
        productListAdapter.setProducts(searchResults);
        categoriesCompaniesBinding.rvCategory.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();
    }

    public void refineSearchResults(int type, String s){
        List<Map<String,Object>> refinedResults = new ArrayList<>();
        if(type==1)
            data.forEach(map -> {
                if(map.get("name").toString().toLowerCase().contains(s.toLowerCase()))
                    refinedResults.add(map);
            });
        if(type==2){
            searchResults.forEach(map -> {
                if(map.get("name").toString().toLowerCase().contains(s.toLowerCase()))
                    refinedResults.add(map);
            });
        }
        productListAdapter.setProducts(refinedResults);
        productListAdapter.notifyDataSetChanged();
    }
}
