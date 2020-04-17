package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.DoubleProductAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.TripleImageAdapter;
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
    public Dialog dialog;


    public ShopPageFragment() {
        // Required empty public constructor
    }

    public ShopPageFragment(Page page){
        this.page = page;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.unic.unic_vendor_final_1.databinding.FragmentShopPageBinding shopPageBinding = FragmentShopPageBinding.inflate(inflater, container, false);
        parent = shopPageBinding.shopViewParent;
        shopPageBinding.btnAddView.setOnClickListener(this);
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

    public void inflateView(com.unic.unic_vendor_final_1.datamodels.View view){
        int viewType = view.getViewCode()/100;
        switch (viewType){
            case 11:
                //TODO
            case 12:
                //TODO
            case 13:
                //TODO
            case 21:
                View categoriesView=Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.categories_view,parent,false);
                categoriesView.setId(view.getViewCode());
                parent.addView(categoriesView);
                CategoriesAdapter categoriesAdapter=new CategoriesAdapter(getContext());
                categoriesAdapter.setCategories(view.getData());
                LinearLayoutManager categoriesLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                RecyclerView categoriesRecyclerView=categoriesView.findViewById(R.id.categories_recycler_view);
                categoriesRecyclerView.setAdapter(categoriesAdapter);
                break;


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
                parent.addView(doubleProductView);
                TextView tvHeader = doubleProductView.findViewById(R.id.double_product_header);
                tvHeader.setText(view.getHeader());
                doubleProductView.findViewById(R.id.btn_add_products).setOnClickListener(this);

                DoubleProductAdapter doubleProductAdapter = new DoubleProductAdapter(getContext());
                doubleProductAdapter.setProducts(view.getData());
                LinearLayoutManager doubleProductLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                RecyclerView doubleProductRecyclerView = doubleProductView.findViewById(R.id.double_product_recycler_view);
                doubleProductRecyclerView.setLayoutManager(doubleProductLayoutManager);
                doubleProductRecyclerView.setNestedScrollingEnabled(false);
                doubleProductRecyclerView.setAdapter(doubleProductAdapter);
                doubleProductRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
                break;
            case 42:
                View TripleProductView = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.triple_image_view,parent,false);
                TripleProductView.setId(view.getViewCode());
                parent.addView(TripleProductView);

                TextView tvHeader2 = TripleProductView.findViewById(R.id.triple_image_header);
                tvHeader2.setText(view.getHeader());
                TripleProductView.findViewById(R.id.btn_add_products).setOnClickListener(this);

                TripleImageAdapter tripleImageAdapter = new TripleImageAdapter(getContext());
                tripleImageAdapter.setProducts(view.getData());
                LinearLayoutManager TripleProductLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                RecyclerView tripleProductRecyclerView = TripleProductView.findViewById(R.id.triple_image_recycler_view);
                tripleProductRecyclerView.setLayoutManager(TripleProductLayoutManager);
                tripleProductRecyclerView.setNestedScrollingEnabled(false);
                tripleProductRecyclerView.setAdapter(tripleImageAdapter);
                tripleProductRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
                break;
            case 43:
                break;
            case 44:
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_products:
                ((SetShopStructure) Objects.requireNonNull(getActivity())).selectProducts(page.getPageId(),((View)v.getParent()).getId());
                break;
            case R.id.btn_add_view:
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.popup_what_you_want_to_do);
                dialog.setTitle("What Do You want to do?");
                ListView whatToDo = (ListView) dialog.findViewById(R.id.listWhatDo);
                whatToDo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((SetShopStructure)Objects.requireNonNull(getActivity())).selectView(page.getPageId(),position);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;



                /*final EditText etViewHeader = new EditText(getContext());
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter View Title");
                builder.setMessage("");
                builder.setView(etViewHeader);
                builder.setPositiveButton("DONE", (dialog, which) -> {
                    if (etViewHeader.getText().toString().trim().length()>0) {
                        com.unic.unic_vendor_final_1.datamodels.View view = new com.unic.unic_vendor_final_1.datamodels.View();
                        view.setHeight(260);
                        view.setFields("name,imageId,price");
                        view.setHeader(etViewHeader.getText().toString().trim());
                        ((SetShopStructure) Objects.requireNonNull(getActivity())).addView(page.getPageId(), view,41);
                    }
                });
                builder.setNegativeButton("CANCEL", (dialog, which) -> {
                });
                AlertDialog dialog  = builder.create();
                dialog.show();*/

        }
    }
}
