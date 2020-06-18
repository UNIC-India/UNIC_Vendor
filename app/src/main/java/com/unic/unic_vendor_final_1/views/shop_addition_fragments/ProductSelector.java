package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentProductSelectorBinding;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductSelector extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private int pageId,code;
    private com.unic.unic_vendor_final_1.datamodels.View view;

    private SetStructureViewModel setStructureViewModel;
    private FragmentProductSelectorBinding productSelectorBinding;

    private ProductListAdapter adapter;

    private List<Map<String,Object>> data = new ArrayList<>();

    private List<Map<String,Object>> searchResults = new ArrayList<>();

    private Map<String,List<String>> extraData = new HashMap<>();

    private boolean isFirst = true;
    private DocumentSnapshot lastDoc;

    private boolean isAtBottom = false;

    private int queryType = 0;

    public ProductSelector(){}

    public ProductSelector(int pageId, com.unic.unic_vendor_final_1.datamodels.View view, int code){
        this.view = view;
        this.code = code;
        this.pageId = pageId;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        productSelectorBinding = FragmentProductSelectorBinding.inflate(inflater,container,false);
        setStructureViewModel = new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        data = view.getData();
        adapter = new ProductListAdapter(getContext(),1);
        adapter.setSelectedProducts(data);

        ArrayAdapter<CharSequence> selectionAdapter=ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array,android.R.layout.simple_spinner_item);
        selectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productSelectorBinding.productSelectionSpinner.setAdapter(selectionAdapter);
        productSelectorBinding.productSelectionSpinner.setOnItemSelectedListener(this);


        productSelectorBinding.productSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0){
                    setStructureViewModel.getPaginatedProductData(true,null,1);
                    return;
                }

                switch (queryType) {
                    case 0:
                        if(s.length()==2) {
                            setStructureViewModel.searchProductsByName(s.toString());
                        }
                        else if(s.length()<2){
                            setStructureViewModel.clearSearch();
                            setStructureViewModel.getPaginatedProductData(true,null,1);
                        }
                        else{
                            refineSearch(s);

                        }
                        break;
                    case 1:

                        List<String> refinedCategories = new ArrayList<>();

                        for(String category : extraData.get("categories")){
                            if(category.toLowerCase().contains(s.toString().toLowerCase()))
                                refinedCategories.add(category);
                        }

                        setStructureViewModel.searchProductsByCategoryList(refinedCategories);
                        break;
                    case 2:
                        List<String> refinedCompanies = new ArrayList<>();
                        for(String company : extraData.get("companies")){
                            if (company.toLowerCase().contains(s.toString().toLowerCase()))
                                refinedCompanies.add(company);
                        }
                        setStructureViewModel.searchProductsByCompanyList(refinedCompanies);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setStructureViewModel.clearSearch();

        setStructureViewModel.getPaginatedProductData(isFirst,lastDoc,1);

        setStructureViewModel.setCurrentFrag(getActivity().getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader));

        setStructureViewModel.getShopExtras().observe(getViewLifecycleOwner(),this::setExtraData);

        setStructureViewModel.getProducts().observe(getViewLifecycleOwner(), maps -> {
            adapter.setProducts(maps);
            adapter.notifyDataSetChanged();
        });

        setStructureViewModel.getSearchResults().observe(getViewLifecycleOwner(), maps ->{
            if(maps!=null) {
                adapter.setProducts(maps);
                adapter.notifyDataSetChanged();
            }
        });

        productSelectorBinding.productSelectionSwipe.setColorScheme(R.color.colorPrimary,R.color.colorSecondary,R.color.colorTertiary);

        productSelectorBinding.productSelectionSwipe.setOnRefreshListener(() -> {
            setStructureViewModel.getLastProductSelectionDoc().setValue(null);
            setStructureViewModel.getIsFirstProductSelection().setValue(Boolean.TRUE);
            setStructureViewModel.clearSearch();
            setStructureViewModel.getPaginatedProductData(true,null,1);
            Handler handler = new Handler();
            handler.postDelayed(() -> productSelectorBinding.productSelectionSwipe.setRefreshing(false),2000);
        });

        productSelectorBinding.productsSelectorRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE&&recyclerView.canScrollVertically(-1)&&!recyclerView.canScrollVertically(1)&&!isAtBottom){
                    setStructureViewModel.getPaginatedProductData(isFirst,lastDoc,1);
                    isAtBottom = true;
                }
                else
                    isAtBottom = false;
            }
        });

        setStructureViewModel.getIsFirstProductSelection().observe(getViewLifecycleOwner(),this::setFirst);

        setStructureViewModel.getLastProductSelectionDoc().observe(getViewLifecycleOwner(),this::setLastDoc);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        productSelectorBinding.productsSelectorRecyclerView.setLayoutManager(layoutManager);
        productSelectorBinding.productsSelectorRecyclerView.setAdapter(adapter);

        return productSelectorBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnRight){
            data = adapter.returnSelectedProducts();
            Structure structure = setStructureViewModel.getStructure().getValue();
            if(code==43){
                view.setHeight(35 q+58*data.size());
            }
            if(code==44){
                view.setHeight(35+75*data.size());
            }
            if(view.getViewCode()==0){
                view.setData(data);
                structure.getPage(pageId).addNewView(view,code);
            }
            else
                structure.updateProductList(pageId,view.getViewCode(),data);
            ((SetShopStructure) Objects.requireNonNull(getActivity())).returnToPage(pageId);
        }

        else if(v.getId()==R.id.btnleft){
           getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void setFirst(boolean first) {
        isFirst = first;
    }

    private void setLastDoc(DocumentSnapshot lastDoc) {
        this.lastDoc = lastDoc;
    }

    public void setExtraData(Map<String, List<String>> extraData) {
        this.extraData = extraData;
    }

    public void setQueryType(int queryType) {

        if(this.queryType!=queryType){
            setStructureViewModel.getPaginatedProductData(true,null,1);
        }

        this.queryType = queryType;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setQueryType(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setQueryType(0);
    }

    public void refineSearch(CharSequence s){

        List<Map<String,Object>> searchResults = setStructureViewModel.getSearchResults().getValue();

        List<Map<String,Object>> refinedSearchResults = new ArrayList<>();

        for(Map map : searchResults){
            if(map.get("name").toString().toLowerCase().contains(s.toString().toLowerCase()))
                refinedSearchResults.add(map);
        }

        adapter.setProducts(refinedSearchResults);
        adapter.notifyDataSetChanged();

    }
}