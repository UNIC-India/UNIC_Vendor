package com.unic.unic_vendor_final_1.views.shop_structure_fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.Adapter_oooa;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.DoubleImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.TripleImageAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentShopPageBinding;
import com.unic.unic_vendor_final_1.datamodels.Page;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.helpers.ShopViewSelector;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopPageFragment extends Fragment implements View.OnClickListener{

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0;
            } else {
                outRect.left = space;
            }
        }
    }

    private Page page;
    private FragmentShopPageBinding shopPageBinding;
    private ViewGroup parent;
    private SetStructureViewModel setStructureViewModel;

    private static final int VIEW_REQUEST_CODE = 101;

    public ShopPageFragment(Page page) {

        this.page = page;



        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shopPageBinding = FragmentShopPageBinding.inflate(inflater,container,false);
        parent = shopPageBinding.shopViewParent;
        setStructureViewModel = ViewModelProviders.of(getActivity()).get(SetStructureViewModel.class);
        shopPageBinding.btnAddView.setOnClickListener(this);
        setStructureViewModel.getStructure().observe(getViewLifecycleOwner(), new Observer<Structure>() {
            @Override
            public void onChanged(Structure structure) {
                inflateViews();
            }
        });
        return shopPageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inflateViews();
    }

    private void inflateViews(){
        parent.removeAllViews();
        for(com.unic.unic_vendor_final_1.datamodels.View view : page.getViews())
            inflateView(view);
    }

    private void inflateView(com.unic.unic_vendor_final_1.datamodels.View view){
        switch(Integer.parseInt(view.getViewId())/10){
            case 41:
                View doubleImagesView = getLayoutInflater().inflate(R.layout.double_image_view,null);
                doubleImagesView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                RelativeLayout.LayoutParams doubleImagesParams = (RelativeLayout.LayoutParams)doubleImagesView.getLayoutParams();
                doubleImagesParams.topMargin =(int)dpToPx(view.getyPos());
                doubleImagesView.setLayoutParams(doubleImagesParams);
                parent.addView(doubleImagesView);

                ((TextView)doubleImagesView.findViewById(R.id.double_image_header)).setText(view.getHeader());
                RecyclerView doubleImagesRecyclerView = doubleImagesView.findViewById(R.id.double_image_recycler_view);
                Adapter_oooa adapter_oooa = new Adapter_oooa(getContext());
                adapter_oooa.setProducts(setStructureViewModel.getViewProducts(view.getProducts()));
                LinearLayoutManager doubleImagesLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                doubleImagesRecyclerView.setLayoutManager(doubleImagesLayoutManager);
                doubleImagesRecyclerView.setAdapter(adapter_oooa);
                doubleImagesRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

                break;
            case 42:
                View tripleImagesView = getLayoutInflater().inflate(R.layout.triple_image_view,null);
                tripleImagesView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                RelativeLayout.LayoutParams tripleImagesParams = (RelativeLayout.LayoutParams)tripleImagesView.getLayoutParams();
                tripleImagesParams.topMargin =(int)dpToPx(view.getyPos());
                tripleImagesView.setLayoutParams(tripleImagesParams);
                parent.addView(tripleImagesView);

                ((TextView)tripleImagesView.findViewById(R.id.triple_image_header)).setText(view.getHeader());
                RecyclerView tripleImagesRecyclerView = tripleImagesView.findViewById(R.id.triple_image_recycler_view);
                TripleImageAdapter tripleImageAdapter = new TripleImageAdapter(getContext());
                tripleImageAdapter.setProducts(setStructureViewModel.getViewProducts(view.getProducts()));
                LinearLayoutManager tripleImagesLayoutManager = new LinearLayoutManager(getContext());
                tripleImagesRecyclerView.setLayoutManager(tripleImagesLayoutManager);
                tripleImagesRecyclerView.setAdapter(tripleImageAdapter);
                tripleImagesRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

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

    private void addView(int code){
        final View popupView =LayoutInflater.from(getContext()).inflate(R.layout.view_header_selector,null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        switch (code){
            case 41:
                final com.unic.unic_vendor_final_1.datamodels.View doubleImagesView = new com.unic.unic_vendor_final_1.datamodels.View();
                doubleImagesView.setViewId(Integer.valueOf(code).toString()+page.getViews().size());
                doubleImagesView.setHeight(360);
                doubleImagesView.setFields("imageId,name,price");
                popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
                popupView.findViewById(R.id.btn_view_header_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String header = ((EditText)popupView.findViewById(R.id.et_view_header_text)).getText().toString();
                        if(header.length()!=0)
                            doubleImagesView.setHeader(header);
                        Structure structure =  setStructureViewModel.getStructure().getValue();
                        assert structure != null;
                        structure.addView(page.getPageId(),doubleImagesView);
                        setStructureViewModel.getStructure().setValue(structure);
                        setStructureViewModel.getSelectedProducts(page.getPageId(),doubleImagesView.getViewId());
                        page = setStructureViewModel.getStructure().getValue().getPage(page.getPageId());
                        inflateViews();
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.btn_view_header_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
            case 42:
                final com.unic.unic_vendor_final_1.datamodels.View tripleImagesView = new com.unic.unic_vendor_final_1.datamodels.View();
                tripleImagesView.setViewId(Integer.valueOf(code).toString()+page.getViews().size());
                tripleImagesView.setHeight(253);
                tripleImagesView.setFields("imageId,name,price");
                popupWindow.showAtLocation(parent,Gravity.CENTER,0,0);
                popupView.findViewById(R.id.btn_view_header_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String header = ((EditText)popupView.findViewById(R.id.et_view_header_text)).getText().toString();
                        if(header.length()!=0)
                            tripleImagesView.setHeader(header);
                        Structure structure = setStructureViewModel.getStructure().getValue();
                        structure.addView(page.getPageId(),tripleImagesView);
                        setStructureViewModel.getStructure().setValue(structure);
                        setStructureViewModel.getSelectedProducts(page.getPageId(),tripleImagesView.getViewId());
                        page = setStructureViewModel.getStructure().getValue().getPage(page.getPageId());
                        inflateViews();
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.btn_view_header_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
        }

        inflateViews();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_view) {
            Intent intent = new Intent(getContext(), ShopViewSelector.class);
            intent.putExtra("tag",page.getPageName());
            getActivity().startActivityForResult(intent, VIEW_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {



        if (requestCode==VIEW_REQUEST_CODE&&resultCode==Activity.RESULT_OK&&data!=null){
            addView(data.getExtras().getInt("viewCode"));

        }

    }
}
