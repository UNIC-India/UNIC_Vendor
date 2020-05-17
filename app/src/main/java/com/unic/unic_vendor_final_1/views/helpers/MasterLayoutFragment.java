package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    public MasterLayoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.master_layout, container, false);
        setStructureViewModel= new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        rv=view.findViewById(R.id.rv);
        spinner=view.findViewById(R.id.spinner);
        searchTextView = view.findViewById(R.id.autoCompleteTextView);
        masterProductAdapter=new MasterProductAdapter(getContext());
        masterCompaniesAdapter=new MasterCompaniesAdapter(getContext());
        masterCategoriesAdapter= new MasterCategoriesAdapter(getContext());
        setAdapter(setter);

        searchTextView.addTextChangedListener(this);

        setStructureViewModel.getProducts().observe(getActivity(), new Observer<List<Map<String, Object>>>() {
            @Override
            public void onChanged(List<Map<String, Object>> maps) {
                if(maps.size()>products.size())
                    isAtBottom = false;
                setProducts(maps);
                masterCategoriesAdapter.setProducts(maps);
                masterProductAdapter.setProducts(maps);
                masterCompaniesAdapter.setProducts(maps);
                masterCompaniesAdapter.notifyDataSetChanged();
                masterProductAdapter.notifyDataSetChanged();
                masterCategoriesAdapter.notifyDataSetChanged();

            }

        });
        setStructureViewModel.getSearchResults().observe(getActivity(), new Observer<List<Map<String, Object>>>() {
            @Override
            public void onChanged(List<Map<String, Object>> maps) {
                setSearchResults(maps);
            }
        });
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        return  view;
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

    public void setProducts(List<Map<String, Object>> products) {
        if (products!=null)
            this.products = products;
    }

    public void setSearchResults(List<Map<String, Object>> searchResults) {
        this.searchResults = searchResults;
        masterProductAdapter.setProducts(searchResults);
        masterProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        List<Map<String,Object>> refinedProducts = new ArrayList<>();
        switch (setter){
            case 0:
                if(s.length()==2){
                    setStructureViewModel.searchProductsByName(setStructureViewModel.getShop().getValue().getId(),s.toString());
                    masterProductAdapter.setProducts(refinedProducts);
                    masterProductAdapter.notifyDataSetChanged();
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
                if(s.length()!=0){
                    for(Map map : products){
                        if (((String)map.get("category")).toLowerCase().contains(s.toString().toLowerCase())){
                            refinedProducts.add(map);
                        }
                    }
                    masterCategoriesAdapter.setProducts(refinedProducts);
                    masterCategoriesAdapter.notifyDataSetChanged();
                }
                else {
                    masterCategoriesAdapter.setProducts(products);
                    masterCategoriesAdapter.notifyDataSetChanged();
                }
                break;
            case 2:
                if(s.length()!=0){
                    for(Map map : products){
                        if (((String)map.get("company")).toLowerCase().contains(s.toString().toLowerCase())){
                            refinedProducts.add(map);
                        }
                    }
                    masterCompaniesAdapter.setProducts(refinedProducts);
                    masterCompaniesAdapter.notifyDataSetChanged();
                }
                else {
                    masterCompaniesAdapter.setProducts(products);
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
