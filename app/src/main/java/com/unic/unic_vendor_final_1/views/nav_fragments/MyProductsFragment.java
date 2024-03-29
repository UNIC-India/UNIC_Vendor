package com.unic.unic_vendor_final_1.views.nav_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentMyProductsBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.viewmodels.UserShopsViewModel;
import com.unic.unic_vendor_final_1.views.activities.AddNewProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class MyProductsFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private FragmentMyProductsBinding myProductsBinding;
    private ProductListAdapter productListAdapter;
    private String shopId;
    private SetStructureViewModel setStructureViewModel;
    private boolean isFirst;
    private DocumentSnapshot lastDoc;
    UserShopsViewModel userShopsViewModel;

    private boolean isAtBottom = false;
    private boolean isSelectorOpen = false;

    private Animation slideUp,slideDown,immediateSlideUp;

    private Map<String,Map<String,List<String>>> extraData = new HashMap<>();

    private int queryType;

    public MyProductsFragment() {
        // Required empty public constructor
    }

    public MyProductsFragment(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myProductsBinding = FragmentMyProductsBinding.inflate(inflater, container, false);
        productListAdapter = new ProductListAdapter(getContext(), 2);
        userShopsViewModel=new ViewModelProvider(getActivity()).get(UserShopsViewModel.class);
        userShopsViewModel.titleSetter.setValue(6);

        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
        immediateSlideUp = AnimationUtils.loadAnimation(getContext(),R.anim.immediate_slide_up);

        ArrayAdapter<CharSequence> selectionAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array, R.layout.simple_textbox);
        selectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        myProductsBinding.myProductFilterList.setAdapter(selectionAdapter);
        myProductsBinding.myProductFilterList.setOnItemClickListener(this);

        /*myProductsBinding.productSpinner.setAdapter(selectionAdapter);
        myProductsBinding.productSpinner.setOnItemSelectedListener(this);*/

        setStructureViewModel = new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        setStructureViewModel.getShopData(shopId);
        setStructureViewModel.getSetProductsUpdating().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean){
                setStructureViewModel.getPaginatedProductData(true, null, 3);
                setStructureViewModel.getSetProductsUpdating().setValue(false);
            }
        });

        myProductsBinding.productSelectionSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0){
                    setStructureViewModel.getPaginatedProductData(true,null,3);
                    return;
                }

                switch (queryType) {
                    case 0:
                        if(s.length()==2) {
                            setStructureViewModel.searchProductsByName(s.toString());
                        }
                        else if(s.length()<2){
                            setStructureViewModel.clearSearch();
                            setStructureViewModel.getPaginatedProductData(true,null,3);
                        }
                        else{
                            refineSearch(s);
                        }
                        break;
                    case 1:

                        List<String> refinedCategories = new ArrayList<>();

                        for(String category : extraData.get("categories").keySet()){
                            if(category.toLowerCase().contains(s.toString().toLowerCase()))
                                refinedCategories.add(category);
                        }

                        setStructureViewModel.searchProductsByCategoryList(refinedCategories);
                        break;
                    case 2:
                        List<String> refinedCompanies = new ArrayList<>();
                        for(String company : extraData.get("companies").keySet()){
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

        myProductsBinding.myProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myProductsBinding.myProductsRecyclerView.setAdapter(productListAdapter);

        setStructureViewModel.getShop().observe(getViewLifecycleOwner(), this::setShop);
        setStructureViewModel.getIsFirstMyProduct().observe(getViewLifecycleOwner(), this::setFirst);
        setStructureViewModel.getLastMyProductDoc().observe(getViewLifecycleOwner(), this::setLastDoc);
        setStructureViewModel.getProducts().observe(getViewLifecycleOwner(), maps -> {
            productListAdapter.setProducts(maps);
            productListAdapter.notifyDataSetChanged();
            if(maps==null||maps.size()==0){
                myProductsBinding.animNoData.setVisibility(View.VISIBLE);
                myProductsBinding.noData.setVisibility(View.VISIBLE);

            }
        });

        setStructureViewModel.getSearchResults().observe(getViewLifecycleOwner(), maps -> {
            if (maps != null) {
                productListAdapter.setProducts(maps);
                productListAdapter.notifyDataSetChanged();
            }

        });

        myProductsBinding.myProductsSwipe.setColorScheme(R.color.colorPrimary, R.color.colorSecondary, R.color.colorTertiary);

        myProductsBinding.myProductsSwipe.setOnRefreshListener(() -> {
            setStructureViewModel.getLastProductSelectionDoc().setValue(null);
            setStructureViewModel.getIsFirstProductSelection().setValue(Boolean.TRUE);
            setStructureViewModel.clearSearch();
            setStructureViewModel.getPaginatedProductData(true, null, 3);
            Handler handler = new Handler();
            handler.postDelayed(() -> myProductsBinding.myProductsSwipe.setRefreshing(false), 2000);
        });

        setStructureViewModel.getIsFirstProductSelection().observe(getViewLifecycleOwner(), this::setFirst);

        setStructureViewModel.getLastProductSelectionDoc().observe(getViewLifecycleOwner(), this::setLastDoc);

        setStructureViewModel.getShopExtras().observe(getViewLifecycleOwner(),this::setExtraData);

        myProductsBinding.myProductsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.canScrollVertically(-1) && !recyclerView.canScrollVertically(1) && !isAtBottom) {
                    setStructureViewModel.getPaginatedProductData(isFirst, lastDoc, 3);
                    isAtBottom = true;
                } else
                    isAtBottom = false;
            }
        });

        myProductsBinding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewProduct.class);
                intent.putExtra("shopId", shopId);
                startActivityForResult(intent,AddNewProduct.ADD_PRODUCTS);
            }
        });

        myProductsBinding.myProductFilter.setOnClickListener(v -> {
            if(isSelectorOpen) {
                myProductsBinding.myProductFilterList.startAnimation(slideUp);
                myProductsBinding.myProductFilterList.setVisibility(View.GONE);
                isSelectorOpen = false;
            }
            else {
                myProductsBinding.myProductFilterList.setVisibility(View.VISIBLE);
                myProductsBinding.myProductFilterList.startAnimation(slideDown);
                isSelectorOpen = true;
            }
        });

        // Inflate the layout for this fragment
        return myProductsBinding.getRoot();
    }

    public void setShop(Shop shop) {

        if (shop == null)
            return;
        setStructureViewModel.getPaginatedProductData(true, null, 3);
    }


    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setLastDoc(DocumentSnapshot lastDoc) {
        this.lastDoc = lastDoc;
    }

    public void setQueryType(int queryType) {

        if (this.queryType != queryType) {
            setStructureViewModel.clearSearch();
            setStructureViewModel.getPaginatedProductData(true, null, 3);
        }

        this.queryType = queryType;
    }

    public void setExtraData(Map<String, Map<String, List<String>>> extraData) {
        this.extraData = extraData;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setQueryType(position);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        myProductsBinding.myProductFilterList.startAnimation(slideUp);
        myProductsBinding.myProductFilterList.setVisibility(View.GONE);
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

        productListAdapter.setProducts(refinedSearchResults);
        productListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==AddNewProduct.ADD_PRODUCTS)
            setStructureViewModel.getPaginatedProductData(true,null,3);
    }
}