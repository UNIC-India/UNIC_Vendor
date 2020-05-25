package com.unic.unic_vendor_final_1.views.helpers;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.MasterCategoriesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.MasterCompaniesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.MasterProductAdapter;
import com.unic.unic_vendor_final_1.databinding.MasterLayoutBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MasterLayoutFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher {
    private RecyclerView rv;
    private Spinner spinner;
    private SetStructureViewModel setStructureViewModel;
    private int setter=0;
    private MasterCategoriesAdapter masterCategoriesAdapter;
    private MasterCompaniesAdapter masterCompaniesAdapter;
    private MasterProductAdapter masterProductAdapter;
    private List<Map<String, Object>> products = new ArrayList<>();
    private AutoCompleteTextView searchTextView;
    private boolean isAtBottom = false;
    private List<Map<String,Object>> searchResults = new ArrayList<>();
    private Map<String,List<String>> extraData = new HashMap<>();
    private MasterLayoutBinding masterLayoutBinding;


    public MasterLayoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        masterLayoutBinding = MasterLayoutBinding.inflate(inflater,container,false);

        setStructureViewModel= new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        rv=masterLayoutBinding.rv;
        spinner=masterLayoutBinding.spinner;
        searchTextView = masterLayoutBinding.autoCompleteTextView;
        masterProductAdapter=new MasterProductAdapter(getContext());
        masterCompaniesAdapter=new MasterCompaniesAdapter(getContext());
        masterCategoriesAdapter= new MasterCategoriesAdapter(getContext());
        setAdapter(setter);

        searchTextView.addTextChangedListener(this);

        setStructureViewModel.getProducts().observe(getActivity(), maps -> {
            if(maps.size()>products.size())
                isAtBottom = false;
            setProducts(maps);

        });
        setStructureViewModel.getSearchResults().observe(getViewLifecycleOwner(), this::setSearchResults);
        setStructureViewModel.getShopExtras().observe(getViewLifecycleOwner(), this::setExtraData);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return masterLayoutBinding.getRoot();
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

    private void setAdapter(int position){
        switch (position){
            case 0:
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(masterProductAdapter);
                rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if(newState==RecyclerView.SCROLL_STATE_IDLE&&!isAtBottom&&!recyclerView.canScrollVertically(1)){
                            isAtBottom = true;
                            setStructureViewModel.getPaginatedProductData(setStructureViewModel.getShop().getValue().getId());
                        }
                    }
                });

                break;
            case 1:
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                masterCategoriesAdapter.notifyDataSetChanged();
                rv.setAdapter(masterCategoriesAdapter);

                break;
            case 2:
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                masterCompaniesAdapter.notifyDataSetChanged();
                rv.setAdapter(masterCompaniesAdapter);
                break;
        }
    }

    private void setProducts(List<Map<String, Object>> products) {
        if(products!=null) {
            this.products = products;
            masterProductAdapter.setProducts(products);
            masterProductAdapter.notifyDataSetChanged();
        }
    }



    private void setSearchResults(List<Map<String, Object>> searchResults) {
        this.searchResults = searchResults;
        masterProductAdapter.setProducts(searchResults);
        masterProductAdapter.notifyDataSetChanged();
    }
    private void setExtraData(Map<String, List<String>> extraData) {
        this.extraData = extraData;
        masterCategoriesAdapter.setCategories(extraData.get("categories"));
        masterCompaniesAdapter.setCompanies(extraData.get("companies"));
        masterCategoriesAdapter.notifyDataSetChanged();
        masterCompaniesAdapter.notifyDataSetChanged();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }



    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        switch (setter){
            case 0:
                if(s.length()==2){
                    setStructureViewModel.searchProductsByName(setStructureViewModel.getShop().getValue().getId(),s.toString());
                }
                else if(s.length()<2){
                    masterProductAdapter.setProducts(products);
                    masterProductAdapter.notifyDataSetChanged();
                }
                else{
                    refineSearchResult(s);
                }
                break;
            case 1:
                List<String> refinedCategories = new ArrayList<>();
                if(s.length()!=0){
                    for(String category : extraData.get("categories")){
                        if(category.toLowerCase().contains(s.toString().toLowerCase()))
                            refinedCategories.add(category);
                    }
                    /*for(Map map : products){
                        if (((String)map.get("category")).toLowerCase().contains(s.toString().toLowerCase())){
                            refinedProducts.add(map);
                        }
                    }*/
                    masterCategoriesAdapter.setCategories(refinedCategories);
                    masterCategoriesAdapter.notifyDataSetChanged();
                }
                else {
                    masterCategoriesAdapter.setCategories(extraData.get("categories"));
                    masterCategoriesAdapter.notifyDataSetChanged();
                }
                break;
            case 2:
                List<String> refinedCompanies = new ArrayList<>();
                if(s.length()!=0){
                    for(String company : extraData.get("companies")){
                        if(company.toLowerCase().contains(s.toString().toLowerCase()))
                            refinedCompanies.add(company);
                    }
                    masterCompaniesAdapter.setCompanies(refinedCompanies);
                    masterCompaniesAdapter.notifyDataSetChanged();
                }
                else {
                    masterCompaniesAdapter.setCompanies(extraData.get("companies"));
                    masterCompaniesAdapter.notifyDataSetChanged();
                }
                break;
        }



    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void refineSearchResult(CharSequence s){

        if(searchResults.size()==0)
            return;

        List<Map<String,Object>> refinedProducts = new ArrayList<>();
        for(Map map : searchResults){
            if(map.get("name").toString().toLowerCase().contains(s.toString().toLowerCase()))
                refinedProducts.add(map);
        }
        masterProductAdapter.setProducts(refinedProducts);
        masterProductAdapter.notifyDataSetChanged();
    }

}
