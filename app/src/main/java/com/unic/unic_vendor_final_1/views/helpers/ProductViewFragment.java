package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentProductViewBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductViewFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private FragmentProductViewBinding productViewBinding;
    private ProductListAdapter productListAdapter;
    private String shopId;
    private SetStructureViewModel setStructureViewModel;

    private boolean isFirst;
    private DocumentSnapshot lastDoc;

    private boolean isAtBottom = false;

    private Map<String,List<String>> extraData = new HashMap<>();

    private int queryType;

    public ProductViewFragment() {
        // Required empty public constructor
    }

    public ProductViewFragment(String shopId){
        this.shopId = shopId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        productViewBinding = FragmentProductViewBinding.inflate(inflater,container,false);
        productListAdapter = new ProductListAdapter(getContext(),2);

        ArrayAdapter<CharSequence> selectionAdapter=ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array,android.R.layout.simple_spinner_item);
        selectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productViewBinding.productSpinner.setAdapter(selectionAdapter);
        productViewBinding.productSpinner.setOnItemSelectedListener(this);

        setStructureViewModel = new ViewModelProvider(this).get(SetStructureViewModel.class);
        setStructureViewModel.getShopData(shopId);

        productViewBinding.productSelectionSearch.addTextChangedListener(new TextWatcher() {
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
                            //TODO Refine product search in product selector
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

        productViewBinding.myProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productViewBinding.myProductsRecyclerView.setAdapter(productListAdapter);

        setStructureViewModel.getShop().observe(getViewLifecycleOwner(),this::setShop);
        setStructureViewModel.getIsFirstMyProduct().observe(getViewLifecycleOwner(),this::setFirst);
        setStructureViewModel.getLastMyProductDoc().observe(getViewLifecycleOwner(),this::setLastDoc);
        setStructureViewModel.getProducts().observe(getViewLifecycleOwner(), maps -> {
            productListAdapter.setProducts(maps);
            productListAdapter.notifyDataSetChanged();
        });

        setStructureViewModel.getSearchResults().observe(getViewLifecycleOwner(), maps ->{
            if(maps!=null) {
                productListAdapter.setProducts(maps);
                productListAdapter.notifyDataSetChanged();
            }
        });

        productViewBinding.myProductsSwipe.setColorScheme(R.color.colorPrimary,R.color.colorSecondary,R.color.colorTertiary);

        productViewBinding.myProductsSwipe.setOnRefreshListener(() -> {
            setStructureViewModel.getLastProductSelectionDoc().setValue(null);
            setStructureViewModel.getIsFirstProductSelection().setValue(Boolean.TRUE);
            setStructureViewModel.clearSearch();
            setStructureViewModel.getPaginatedProductData(true,null,1);
            Handler handler = new Handler();
            handler.postDelayed(() -> productViewBinding.myProductsSwipe.setRefreshing(false),2000);
        });

        setStructureViewModel.getIsFirstProductSelection().observe(getViewLifecycleOwner(),this::setFirst);

        setStructureViewModel.getLastProductSelectionDoc().observe(getViewLifecycleOwner(),this::setLastDoc);

        productViewBinding.myProductsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        // Inflate the layout for this fragment
        return productViewBinding.getRoot();
    }

    public void setShop(Shop shop) {

        if (shop==null)
            return;
        setStructureViewModel.getPaginatedProductData(true,null,3);
    }



    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setLastDoc(DocumentSnapshot lastDoc) {
        this.lastDoc = lastDoc;
    }

    public void setQueryType(int queryType) {

        if(this.queryType!=queryType){
            setStructureViewModel.clearSearch();
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
}