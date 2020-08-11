package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.MasterCategoriesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.MasterCompaniesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.MasterProductAdapter;
import com.unic.unic_vendor_final_1.databinding.MasterLayoutBinding;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MasterLayoutFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher, AdapterView.OnItemClickListener {
    private RecyclerView rv;
    private com.unic.unic_vendor_final_1.datamodels.View view;
    private int pageId;
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
    private Map<String,Map<String,List<String>>> extraData = new HashMap<>();
    private MasterLayoutBinding masterLayoutBinding;
    boolean isSelectorOpen = false;
    Animation slideUp,slideDown,immediateSlideUp;

    private boolean isFirst = true;
    private DocumentSnapshot lastDoc;


    public MasterLayoutFragment() {
        // Required empty public constructor
    }
    public MasterLayoutFragment(com.unic.unic_vendor_final_1.datamodels.View view, int pageId){
        this.view=view;
        this.pageId = pageId;
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

        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
        immediateSlideUp = AnimationUtils.loadAnimation(getContext(),R.anim.immediate_slide_up);
        searchTextView.addTextChangedListener(this);

        setStructureViewModel.getProducts().observe(getActivity(), maps -> {
            if(maps.size()>products.size())
                isAtBottom = false;
            setProducts(maps);

        });

        setStructureViewModel.getIsFirstProduct().observe(getViewLifecycleOwner(), this::setFirst);

        setStructureViewModel.getSearchResults().observe(getViewLifecycleOwner(), this::setSearchResults);

        setStructureViewModel.getShopExtras().observe(getViewLifecycleOwner(), this::setExtraData);

        setStructureViewModel.getLastProductDoc().observe(getViewLifecycleOwner(),this::setLastDoc);

        setStructureViewModel.getPaginatedProductData(true,null,2);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array,R.layout.simple_textbox);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        masterLayoutBinding.masterTypeList.setAdapter(adapter);
        masterLayoutBinding.masterTypeList.setOnItemClickListener(this);

        /*spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);*/
        if(view!=null&&view.getData()!=null&&view.getData().size()!=0&&view.getData().get(0).get("default")!=null) {
            masterLayoutBinding.masterTypeList.setSelection(Integer.parseInt(view.getData().get(0).get("default").toString()));
            //spinner.setSelection(Integer.parseInt(view.getData().get(0).get("default").toString()));
            setAdapter(Integer.parseInt(view.getData().get(0).get("default").toString()));
        }
        else {
            masterLayoutBinding.masterTypeList.setSelection(0);
            //spinner.setSelection(0);
            setAdapter(0);
        }

        masterLayoutBinding.getRoot().setEnabled(false);

        masterLayoutBinding.masterProductFilter.setOnClickListener(v -> {
            if(isSelectorOpen) {
                masterLayoutBinding.masterTypeList.startAnimation(slideUp);
                masterLayoutBinding.masterTypeList.setVisibility(View.GONE);
                isSelectorOpen = false;
            }
            else {
                masterLayoutBinding.masterTypeList.startAnimation(slideDown);
                masterLayoutBinding.masterTypeList.setVisibility(View.VISIBLE);
                isSelectorOpen = true;
            }
        });

        return masterLayoutBinding.getRoot();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        if(position!=Integer.parseInt((view.getData().size()!=0)?view.getData().get(0).get("default").toString():"0") ){
            int count = 0;
            int height = 0;

            switch (position){
                case 0:
                    height = 658;
                    break;
                case 1:
                    count = extraData.get("categories").size();
                    height = 53 + count*48;
                    break;

                case 2:
                    count = extraData.get("companies").size();
                    height = 53 + count*48;
                    break;

            }
            Map<String,Object> heightMap = new HashMap<>();
            heightMap.put("0",658);
            heightMap.put("1",Math.min(53+extraData.get("categories").size()*48,658));
            heightMap.put("2",Math.min(53+extraData.get("companies").size()*48,658));

            List<Map<String, Object>> temp = new ArrayList<>();
            Map<String, Object> d = new HashMap<>();    //Both these variables are used for setting the default view in the MasterLayout.
            d.put("default", position);

            temp.add(d);
            temp.add(heightMap);
            view.setData(temp);

            height = Math.min(height, 658);
            view.setHeight(height);

            ((SetShopStructure)getActivity()).updateMasterLayoutHeight(pageId,view.getViewCode());
        }

        else {
            setter = position;
            setAdapter(setter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //  setter=0;
        // setAdapter(setter);
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
                        if(newState==RecyclerView.SCROLL_STATE_IDLE&&!isAtBottom&&recyclerView.canScrollVertically(-1)&&!recyclerView.canScrollVertically(1)){
                            isAtBottom = true;
                            setStructureViewModel.getPaginatedProductData(isFirst,lastDoc,2);
                        }
                    }
                });

                masterLayoutBinding.getRoot().setEnabled(true);

                masterLayoutBinding.getRoot().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        setStructureViewModel.getPaginatedProductData(true,null,2);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> masterLayoutBinding.getRoot().setRefreshing(false),2000);
                    }
                });

                break;
            case 1:
                masterLayoutBinding.getRoot().setEnabled(false);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                masterCategoriesAdapter.notifyDataSetChanged();
                rv.setAdapter(masterCategoriesAdapter);

                break;
            case 2:
                masterLayoutBinding.getRoot().setEnabled(false);
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
        if(searchResults==null) {
            masterProductAdapter.setProducts(products);
            masterProductAdapter.notifyDataSetChanged();
            return;
        }
        this.searchResults = searchResults;
        masterProductAdapter.setProducts(searchResults);
        masterProductAdapter.notifyDataSetChanged();
    }
    private void setExtraData(Map<String,Map<String,List<String>>> extraData) {
        this.extraData = extraData;
        masterCategoriesAdapter.setCategories(new ArrayList<>(extraData.get("categories").keySet()));
        masterCompaniesAdapter.setCompanies(new ArrayList<>(extraData.get("companies").keySet()));
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
                    setStructureViewModel.searchProductsByName(s.toString());
                }
                else if(s.length()<2){
                    masterProductAdapter.setProducts(products);
                    masterProductAdapter.notifyDataSetChanged();
                    setStructureViewModel.clearSearch();
                }
                else{
                    refineSearchResult(s);
                }
                break;
            case 1:
                List<String> refinedCategories = new ArrayList<>();
                if(s.length()!=0){
                    for(String category : extraData.get("categories").keySet()){
                        if(category.toLowerCase().contains(s.toString().toLowerCase()))
                            refinedCategories.add(category);
                    }
                    masterCategoriesAdapter.setCategories(refinedCategories);
                }
                else {
                    masterCategoriesAdapter.setCategories(new ArrayList<>(extraData.get("categories").keySet()));
                }
                masterCategoriesAdapter.notifyDataSetChanged();
                break;
            case 2:
                List<String> refinedCompanies = new ArrayList<>();
                if(s.length()!=0){
                    for(String company : extraData.get("companies").keySet()){
                        if(company.toLowerCase().contains(s.toString().toLowerCase()))
                            refinedCompanies.add(company);
                    }
                    masterCompaniesAdapter.setCompanies(refinedCompanies);
                }
                else {
                    masterCompaniesAdapter.setCompanies(new ArrayList<>(extraData.get("companies").keySet()));
                }
                masterCompaniesAdapter.notifyDataSetChanged();
                break;
        }



    }

    @Override
    public void afterTextChanged(Editable s) {

        if(s.length()<2){
            masterProductAdapter.setProducts(products);
            masterProductAdapter.notifyDataSetChanged();
            setStructureViewModel.clearSearch();
        }

    }

    private void refineSearchResult(CharSequence s){

        if(searchResults==null||searchResults.size()==0) {
            setStructureViewModel.searchProductsByName(s.toString().substring(0,2));
            return;
        }

        List<Map<String,Object>> refinedProducts = new ArrayList<>();
        for(Map map : searchResults){
            if(map.get("name").toString().toLowerCase().contains(s.toString().toLowerCase()))
                refinedProducts.add(map);
        }
        masterProductAdapter.setProducts(refinedProducts);
        masterProductAdapter.notifyDataSetChanged();
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setLastDoc(DocumentSnapshot lastDoc) {
        this.lastDoc = lastDoc;
    }

    /*public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        if(position!=Integer.parseInt((view.getData().size()!=0)?view.getData().get(0).get("default").toString():"0") ){
            int count = 0;
            int height = 0;

            switch (position){
                case 0:
                    height = 658;
                    break;
                case 1:
                    count = extraData.get("categories").size();
                    height = 53 + count*48;
                    break;

                case 2:
                    count = extraData.get("companies").size();
                    height = 53 + count*48;
                    break;

            }
            Map<String,Object> heightMap = new HashMap<>();
            heightMap.put("0",658);
            heightMap.put("1",Math.min(53+extraData.get("categories").size()*48,658));
            heightMap.put("2",Math.min(53+extraData.get("companies").size()*48,658));

            List<Map<String, Object>> temp = new ArrayList<>();
            Map<String, Object> d = new HashMap<>();    //Both these variables are used for setting the default view in the MasterLayout.
            d.put("default", position);

            temp.add(d);
            temp.add(heightMap);
            view.setData(temp);

            height = Math.min(height, 658);
            view.setHeight(height);

            ((SetShopStructure)getActivity()).updateMasterLayoutHeight(pageId,view.getViewCode());
        }

        else {
            setter = position;
            setAdapter(setter);
        }

    }
}
