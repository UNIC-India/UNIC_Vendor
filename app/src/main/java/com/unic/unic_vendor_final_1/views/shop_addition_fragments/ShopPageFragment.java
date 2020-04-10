package com.unic.unic_vendor_final_1.views.helpers;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.DoubleProductAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentShopPageBinding;
import com.unic.unic_vendor_final_1.datamodels.Page;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopPageFragment extends Fragment implements View.OnClickListener {

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, @NotNull View view,
                                   RecyclerView parent, @NotNull RecyclerView.State state) {

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0;
            } else {
                outRect.left = space;
            }
        }
    }

    private Page page;
    private ViewGroup parent;

    public ShopPageFragment() {
        // Required empty public constructor
    }

    public ShopPageFragment(Page page){
        this.page = page;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.unic.unic_vendor_final_1.databinding.FragmentShopPageBinding shopPageBinding = FragmentShopPageBinding.inflate(inflater, container, false);
        parent = shopPageBinding.shopViewParent;

        return shopPageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inflateViews();
    }

    private void inflateViews(){

        for(com.unic.unic_vendor_final_1.datamodels.View view : page.getViews()){
            inflateView(view);
        }

    }

    private void inflateView(com.unic.unic_vendor_final_1.datamodels.View view){
        int viewType = view.getViewCode()/100;
        switch (viewType){
            case 11:
                //TODO
            case 12:
                //TODO
            case 13:
                //TODO
            case 21:
                //TODO
            case 22:
                //TODO
            case 23:
                //TODO
            case 24:
                //TODO
            case 31:
                //TODO
            case 32:
                //TODO
            case 41:
                View doubleProductView = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.double_product_view,parent,false);
                doubleProductView.setId(view.getViewCode());
                parent.addView(doubleProductView, RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()));
                RelativeLayout.LayoutParams doubleProductLayoutParams = (RelativeLayout.LayoutParams) doubleProductView.getLayoutParams();
                doubleProductLayoutParams.topMargin = (int)dpToPx(view.getyPos());
                doubleProductView.setLayoutParams(doubleProductLayoutParams);
                TextView tvHeader = doubleProductView.findViewById(R.id.double_product_header);
                tvHeader.setText(view.getHeader());
                doubleProductView.findViewById(R.id.btn_add_products).setOnClickListener(this);

                DoubleProductAdapter doubleProductAdapter = new DoubleProductAdapter(getContext());
                doubleProductAdapter.setProducts(view.getData());
                LinearLayoutManager doubleProductLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                RecyclerView doubleProductRecyclerView = doubleProductView.findViewById(R.id.double_product_recycler_view);
                doubleProductRecyclerView.setLayoutManager(doubleProductLayoutManager);
                doubleProductRecyclerView.setAdapter(doubleProductAdapter);
                doubleProductRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        }
    }

    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    public void onClick(View v) {
        ((SetShopStructure) Objects.requireNonNull(getActivity())).selectProducts(page.getPageId(),((View)v.getParent()).getId());
    }
}
