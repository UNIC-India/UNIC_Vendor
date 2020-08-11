package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter2;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ImageViewAdapters.DoubleImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.DoubleProductAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.DoubleProductWithoutImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListWithoutImagesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.SliderViewAdapters.SliderAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentShopPageBinding;
import com.unic.unic_vendor_final_1.datamodels.Page;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;
import com.unic.unic_vendor_final_1.views.helpers.AutoScrollViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopPageFragment extends Fragment implements View.OnClickListener , View.OnTouchListener {

    int prevY;
    ArrayList<View> views = new ArrayList<>();

    SetStructureViewModel setStructureViewModel;

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
        FragmentShopPageBinding shopPageBinding = FragmentShopPageBinding.inflate(inflater, container, false);

        setStructureViewModel=new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        setStructureViewModel.setCurrentFrag(getActivity().getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader));
        parent = shopPageBinding.shopViewParent;
        ViewGroup.LayoutParams parentLayoutParams = parent.getLayoutParams();
        parentLayoutParams.height = (int)dpToPx(page!=null?page.getSize():0);
        parent.setLayoutParams(parentLayoutParams);
        inflateViews();
        return shopPageBinding.getRoot();
    }

    private void inflateViews(){

        if(page==null)
            return;

        parent.removeAllViews();
        views.clear();

        for(com.unic.unic_vendor_final_1.datamodels.View view : page.getViews()){
            inflateView(view,true);
        }

    }

    public void inflateViewsAfterOffset(int offset){
        for(int i=offset;i<page.getViews().size();i++){
            parent.removeViewAt(offset);
            views.remove(offset);
        }
        for(int i=offset;i<page.getViews().size();i++){
            inflateView(page.getViews().get(i),true);
        }
    }


    private void inflateView(com.unic.unic_vendor_final_1.datamodels.View view,boolean isNew){
        int viewType = view.getViewCode()/100;
        int viewPos = view.getViewCode()%100-1;
        switch (viewType){
            case 0:
                FrameLayout frameLayout =new FrameLayout(getContext());
                frameLayout.setId(view.getViewCode()*10+1);
                FragmentManager fm=getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(frameLayout.getId(),new MasterLayoutFragment(view,page.getPageId())).commit();

                ViewGroup frame = new RelativeLayout(getContext());
                frame.addView(frameLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                frame.setId(view.getViewCode());

                View view00 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view00.setId(view.getViewCode());
                parent.addView(view00,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams frameLayoutParams = (RelativeLayout.LayoutParams) view00.getLayoutParams();
                frameLayoutParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);
                view00.setLayoutParams(frameLayoutParams);
                ((ViewGroup)view00.findViewById(R.id.view_loader)).addView(frame,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view00.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                view00.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);

                if(isNew)
                    views.add(view00);


                break;
            case 11:
                //TODO
            case 12:
                View doubleImageView = getLayoutInflater().inflate(R.layout.double_images_view,parent,false);
                doubleImageView.setId(view.getViewCode());

                View view12 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view12.setId(view.getViewCode());
                parent.addView(view12,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams doubleImageParams = (RelativeLayout.LayoutParams) view12.getLayoutParams();
                doubleImageParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);
                view12.setLayoutParams(doubleImageParams);

                ((ViewGroup)view12.findViewById(R.id.view_loader)).addView(doubleImageView,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));

                view12.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);
                view12.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);

                DoubleImageAdapter doubleImageAdapter = new DoubleImageAdapter(getContext());
                doubleImageAdapter.setData(view.getData());
                RecyclerView doubleImageRecyclerView = doubleImageView.findViewById(R.id.double_image_recycler_view);
                LinearLayoutManager doubleImageLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                doubleImageRecyclerView.setLayoutManager(doubleImageLayoutManager);
                doubleImageRecyclerView.setAdapter(doubleImageAdapter);
                doubleImageRecyclerView.setNestedScrollingEnabled(false);
                doubleImageRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

                if (isNew)
                    views.add(view12);
                break;
            case 13:
                //TODO
            case 21:
                View categoriesView=getLayoutInflater().inflate(R.layout.categories_view,parent,false);
                categoriesView.setId(view.getViewCode());

                View view21 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view21.setId(view.getViewCode());
                parent.addView(view21,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams categoriesViewParams = (RelativeLayout.LayoutParams) view21.getLayoutParams();
                categoriesViewParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);
                view21.setLayoutParams(categoriesViewParams);

                ((ViewGroup)view21.findViewById(R.id.view_loader)).addView(categoriesView,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view21.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                view21.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);

                if(isNew)
                    views.add(view21);

                CategoriesAdapter2 categoriesAdapter=new CategoriesAdapter2(getContext(),view.getFields().equals("cname")?0:1);
                categoriesAdapter.setCategories(view.getData());
                if(view.getFields().equals("cname"))
                    categoriesAdapter.setSubKeys(setStructureViewModel.getShopExtras().getValue().get("categories"));
                if(view.getFields().equals("compname"))
                    categoriesAdapter.setSubKeys(setStructureViewModel.getShopExtras().getValue().get("companies"));
                LinearLayoutManager categoriesLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                RecyclerView categoriesRecyclerView=categoriesView.findViewById(R.id.categories_recycler_view);
                categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);
                categoriesRecyclerView.setAdapter(categoriesAdapter);
                break;


            case 22:
                View categoriesView2=getLayoutInflater().inflate(R.layout.categories_view,parent,false);
                categoriesView2.setId(view.getViewCode());

                View view22 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view22.setId(view.getViewCode());
                parent.addView(view22,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams categoriesViewParams2 = (RelativeLayout.LayoutParams) view22.getLayoutParams();
                categoriesViewParams2.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);
                view22.setLayoutParams(categoriesViewParams2);

                ((ViewGroup)view22.findViewById(R.id.view_loader)).addView(categoriesView2,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view22.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                view22.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);

                if(isNew)
                    views.add(view22);

                CategoriesAdapter2 categoriesAdapter2=new CategoriesAdapter2(getContext(),view.getFields().equals("cname")?0:1);
                categoriesAdapter2.setCategories(view.getData());
                if(view.getFields().equals("cname"))
                    categoriesAdapter2.setSubKeys(setStructureViewModel.getShopExtras().getValue().get("categories"));
                if(view.getFields().equals("compname"))
                    categoriesAdapter2.setSubKeys(setStructureViewModel.getShopExtras().getValue().get("companies"));
                RecyclerView.LayoutManager categoriesLayoutManager2=new GridLayoutManager(getContext(),3){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }

                    @Override
                    public boolean canScrollHorizontally() {
                        return false;
                    }
                };
                RecyclerView categoriesRecyclerView2=categoriesView2.findViewById(R.id.categories_recycler_view);
                categoriesRecyclerView2.setLayoutManager(categoriesLayoutManager2);
                categoriesRecyclerView2.setAdapter(categoriesAdapter2);
                break;
            case 23:
                //TODO
            case 24:
                //TODO
            case 31:

                View sliderView = getLayoutInflater().inflate(R.layout.slider,parent,false);
                sliderView.setId(view.getViewCode());
                sliderView.findViewById(R.id.slider_images_tab).setVisibility(View.GONE);

                View view31 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view31.setId(view.getViewCode());
                parent.addView(view31,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams sliderParams = (RelativeLayout.LayoutParams) view31.getLayoutParams();
                sliderParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);
                view31.setLayoutParams(sliderParams);

                ((ViewGroup)view31.findViewById(R.id.view_loader)).addView(sliderView,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));

                view31.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);
                view31.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);

                if(isNew)
                    views.add(view31);

                SliderAdapter sliderAdapter = new SliderAdapter(getActivity(),view.getData());
                AutoScrollViewPager viewPager = sliderView.findViewById(R.id.slider_images_flipper);

                viewPager.startAutoScroll();
                viewPager.setInterval(3000);
                viewPager.setCycle(true);
                viewPager.setStopScrollWhenTouch(true);
                viewPager.setAdapter(sliderAdapter);
                viewPager.setOffscreenPageLimit(1);
                break;

            case 32:

                View sliderIndicatorView = getLayoutInflater().inflate(R.layout.slider,parent,false);
                sliderIndicatorView.setId(view.getViewCode());

                View view32 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view32.setId(view.getViewCode());
                parent.addView(view32,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams sliderIndicatorParams = (RelativeLayout.LayoutParams) view32.getLayoutParams();
                sliderIndicatorParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);
                view32.setLayoutParams(sliderIndicatorParams);

                ((ViewGroup)view32.findViewById(R.id.view_loader)).addView(sliderIndicatorView,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));

                view32.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);
                view32.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);

                if(isNew)
                    views.add(view32);

                SliderAdapter sliderIndicatorAdapter = new SliderAdapter(getActivity(),view.getData());

                AutoScrollViewPager viewIndicatorPager = sliderIndicatorView.findViewById(R.id.slider_images_flipper);

                viewIndicatorPager.startAutoScroll();
                viewIndicatorPager.setInterval(3000);
                viewIndicatorPager.setCycle(true);
                viewIndicatorPager.setStopScrollWhenTouch(true);
                viewIndicatorPager.setOffscreenPageLimit(1);

                TabLayout sliderTabs = sliderIndicatorView.findViewById(R.id.slider_images_tab);
                sliderTabs.setupWithViewPager(viewIndicatorPager);

                viewIndicatorPager.setAdapter(sliderIndicatorAdapter);
                break;

            case 41:
                View doubleProductView = getLayoutInflater().inflate(R.layout.double_product_view,parent,false);
                doubleProductView.setId(view.getViewCode());

                View view41 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view41.setId(view.getViewCode());

                parent.addView(view41,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams doubleProductViewParams = (RelativeLayout.LayoutParams) view41.getLayoutParams();
                doubleProductViewParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);
                view41.setLayoutParams(doubleProductViewParams);

                ((ViewGroup)view41.findViewById(R.id.view_loader)).addView(doubleProductView,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view41.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                TextView tvHeader = doubleProductView.findViewById(R.id.double_product_header);
                tvHeader.setText(view.getHeader());
                doubleProductView.findViewById(R.id.btn_add_products).setOnClickListener(this);

                view41.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);

                DoubleProductAdapter doubleProductAdapter = new DoubleProductAdapter(getContext());
                doubleProductAdapter.setProducts(view.getData());
                LinearLayoutManager doubleProductLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                RecyclerView doubleProductRecyclerView = doubleProductView.findViewById(R.id.double_product_recycler_view);
                doubleProductRecyclerView.setLayoutManager(doubleProductLayoutManager);
                doubleProductRecyclerView.setNestedScrollingEnabled(false);
                doubleProductRecyclerView.setAdapter(doubleProductAdapter);
                doubleProductRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

                if(isNew)
                    views.add(view41);
                break;
            case 42:
                View doubleProductwoImages = getLayoutInflater().inflate(R.layout.double_product_view,parent,false);
                doubleProductwoImages.setId(view.getViewCode());

                View view42 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view42.setId(view.getViewCode());
                parent.addView(view42,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams tripleProductViewParams = (RelativeLayout.LayoutParams) view42.getLayoutParams();
                tripleProductViewParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);

                ((ViewGroup)view42.findViewById(R.id.view_loader)).addView(doubleProductwoImages,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view42.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                view42.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);


                TextView tvHeader2 = doubleProductwoImages.findViewById(R.id.double_product_header);
                tvHeader2.setText(view.getHeader());
                doubleProductwoImages.findViewById(R.id.btn_add_products).setOnClickListener(this);

                DoubleProductWithoutImageAdapter doubleProductWithoutImageAdapter = new DoubleProductWithoutImageAdapter(getContext());
                doubleProductWithoutImageAdapter.setProducts(view.getData());
                LinearLayoutManager doubleProductwoImageLayout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                RecyclerView doubleProductwoImagesRecyclerView = doubleProductwoImages.findViewById(R.id.double_product_recycler_view);
                doubleProductwoImagesRecyclerView.setLayoutManager(doubleProductwoImageLayout);
                doubleProductwoImagesRecyclerView.setNestedScrollingEnabled(false);
                doubleProductwoImagesRecyclerView.setAdapter(doubleProductWithoutImageAdapter);
                doubleProductwoImagesRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
                if(isNew)
                    views.add(view42);
                break;
            case 43:
                View productListWithoutImages = getLayoutInflater().inflate(R.layout.double_product_view,parent,false);
                productListWithoutImages.setId(view.getViewCode());

                View view43 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view43.setId(view.getViewCode());
                parent.addView(view43,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams productListWithoutImagesParams = (RelativeLayout.LayoutParams) view43.getLayoutParams();
                productListWithoutImagesParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);

                ((ViewGroup)view43.findViewById(R.id.view_loader)).addView(productListWithoutImages,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view43.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                view43.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);

                TextView tvPLWoIHeader = productListWithoutImages.findViewById(R.id.double_product_header);
                tvPLWoIHeader.setText(view.getHeader());
                productListWithoutImages.findViewById(R.id.btn_add_products).setOnClickListener(this);

                ProductListWithoutImagesAdapter productListWithoutImagesAdapter = new ProductListWithoutImagesAdapter(getContext());
                productListWithoutImagesAdapter.setProducts(view.getData());
                LinearLayoutManager productListWithoutImagesLayout = new LinearLayoutManager(getContext()){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                RecyclerView productListWithoutImagesRecyclerView = productListWithoutImages.findViewById(R.id.double_product_recycler_view);
                productListWithoutImagesRecyclerView.setLayoutManager(productListWithoutImagesLayout);
                productListWithoutImagesRecyclerView.setNestedScrollingEnabled(false);
                productListWithoutImagesRecyclerView.setAdapter(productListWithoutImagesAdapter);
                if(isNew)
                    views.add(view43);
                break;
            case 44:
                View productListWithImages = getLayoutInflater().inflate(R.layout.double_product_view,parent,false);
                productListWithImages.setId(view.getViewCode());

                View view44 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view44.setId(view.getViewCode());
                parent.addView(view44,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams productListWithImagesParams = (RelativeLayout.LayoutParams) view44.getLayoutParams();
                productListWithImagesParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);

                ((ViewGroup)view44.findViewById(R.id.view_loader)).addView(productListWithImages,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view44.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                view44.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);

                TextView tvPLWiIHeader = productListWithImages.findViewById(R.id.double_product_header);
                tvPLWiIHeader.setText(view.getHeader());
                productListWithImages.findViewById(R.id.btn_add_products).setOnClickListener(this);

                ProductListAdapter productListWithImagesAdapter = new ProductListAdapter(getContext(),3);
                productListWithImagesAdapter.setProducts(view.getData());
                LinearLayoutManager productListWithImagesLayout = new LinearLayoutManager(getContext()){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                RecyclerView productListWithImagesRecyclerView = productListWithImages.findViewById(R.id.double_product_recycler_view);
                productListWithImagesRecyclerView.setLayoutManager(productListWithImagesLayout);
                productListWithImagesRecyclerView.setNestedScrollingEnabled(false);
                productListWithImagesRecyclerView.setAdapter(productListWithImagesAdapter);
                if(isNew)
                    views.add(view44);
                break;
            case 51:
                View Text_View=Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.text_view_item,parent,false);
                Text_View.setId(view.getViewCode());

                View view51 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view51.setId(view.getViewCode());
                parent.addView(view51,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams Text_Viewparams = (RelativeLayout.LayoutParams) view51.getLayoutParams();
                Text_Viewparams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);

                ((ViewGroup)view51.findViewById(R.id.view_loader)).addView(Text_View,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view51.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                view51.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);

                if(isNew)
                    views.add(view51);


                EditText etText=Text_View.findViewById(R.id.etText);
                EditText etSize=Text_View.findViewById(R.id.etSize);
                TextView tvBold=Text_View.findViewById(R.id.tvBold);
                TextView tvItalics=Text_View.findViewById(R.id.tvItalic);
                if(view.getData().size()==0) {
                    view.getData().add(new HashMap<>());
                    view.getData().get(0).put("size", 18);
                }

                etText.setText(view.getData().get(0).get("text") == null ? " " : view.getData().get(0).get("text").toString());
                if (view.getData().get(0).containsKey("bold") && ((Boolean)view.getData().get(0).get("bold"))) {
                    etText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tvBold.setBackgroundColor(getResources().getColor(R.color.gray_1));
                }
                if (view.getData().get(0).containsKey("italics") && ((Boolean)view.getData().get(0).get("italics"))) {
                    etText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
                    tvItalics.setBackgroundColor(getResources().getColor(R.color.gray_1));

                }
                if(view.getData().get(0).containsKey("bold") && ((Boolean)view.getData().get(0).get("bold"))&&view.getData().get(0).containsKey("italics") && ((Boolean)view.getData().get(0).get("italics"))) {
                    etText.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
                    tvBold.setBackgroundColor(getResources().getColor(R.color.gray_1));
                    tvItalics.setBackgroundColor(getResources().getColor(R.color.gray_1));


                }
                if (view.getData().get(0).containsKey("size")) {
                    etText.setTextSize(Integer.parseInt(view.getData().get(0).get("size").toString()));
                    etSize.setText(view.getData().get(0).get("size")+"");
                }

                tvBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etText!=null&&etText.getTypeface()!=null)
                            switch (etText.getTypeface().getStyle()){
                                case Typeface.ITALIC:
                                    etText.setTypeface(Typeface.DEFAULT,Typeface.BOLD_ITALIC);
                                    tvBold.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_1));
                                    view.getData().get(0).put("bold",true);
                                    break;

                                case Typeface.BOLD:
                                    etText.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
                                    tvBold.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                                    tvItalics.setBackground(getActivity().getDrawable(R.drawable.round_corner));
                                    view.getData().get(0).put("bold",false);
                                    break;
                                case Typeface.NORMAL:
                                    etText.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                                    tvBold.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_1));
                                    view.getData().get(0).put("bold",true);
                                    break;
                                case Typeface.BOLD_ITALIC:
                                    etText.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
                                    tvBold.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                                    tvItalics.setBackground(getActivity().getDrawable(R.drawable.round_corner));
                                    view.getData().get(0).put("bold",false);
                                    break;

                            }
                    }
                });
                tvItalics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etText!=null&&etText.getTypeface()!=null)
                            switch (etText.getTypeface().getStyle()) {
                                case Typeface.ITALIC:
                                    etText.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                                    tvItalics.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                                    tvItalics.setBackground(getActivity().getDrawable(R.drawable.round_corner));
                                    view.getData().get(0).put("italics",true);
                                    break;
                                case Typeface.BOLD:
                                    etText.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
                                    tvItalics.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_1));
                                    view.getData().get(0).put("italics",true);
                                    break;
                                case Typeface.NORMAL:
                                    etText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
                                    tvItalics.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_1));
                                    view.getData().get(0).put("italics",true);
                                    break;
                                case Typeface.BOLD_ITALIC:
                                    etText.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                                    tvItalics.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                                    tvItalics.setBackground(getActivity().getDrawable(R.drawable.round_corner));
                                    view.getData().get(0).put("italics",false);
                                    break;
                            }
                    }

                });
                etText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        view.getData().get(0).put("text",etText.getText().toString());

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                etSize.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (etSize.getText()==null||etSize.getText().toString().length()==0) {
                            etText.setTextSize(18);
                            view.getData().get(0).put("size",18);
                        }
                        else{
                            if(Integer.parseInt(etSize.getText().toString())>=10 && Integer.parseInt(etSize.getText().toString())<=32) {
                                etText.setTextSize(Integer.parseInt(etSize.getText().toString()));
                                view.getData().get(0).put("size",Integer.parseInt(etSize.getText().toString()));
                            }
                            else{
                                etText.setTextSize(18);
                                view.getData().get(0).put("size",18);
                                Toast.makeText(getActivity(), "Size can only be between 10 and 32", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


        }
    }

    private float dpToPx(int dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private void deleteView(int viewCode){
        for(int i=0;i<views.size();i++)
            if(viewCode==views.get(i).getId()){
                parent.removeViewAt(i);
                views.remove(i);
                break;
            }
        if(viewCode/1000==1||viewCode/1000==3)
            setStructureViewModel.deleteViewPics(page.getPageId(),viewCode);

        refreshViews();
        inflateViewsAfterOffset(viewCode%100-1);
    }

    private void refreshViews(){

        List<com.unic.unic_vendor_final_1.datamodels.View> oldViews = page.getViews();
        page.setViews(new ArrayList<>());
        page.setSize(0);

        for(int i=0;i<views.size();i++){
            int viewCode = views.get(i).getId();

            com.unic.unic_vendor_final_1.datamodels.View view = oldViews.get(viewCode%100-1);

            List<Map<String,Object>> data = view.getData();
            page.addView(view,viewCode/100);
            page.updateView(viewCode-viewCode%100+page.getViews().size(),data);
            views.get(i).setId(viewCode-viewCode%100+page.getViews().size());
            ((ViewGroup)views.get(i).findViewById(R.id.view_loader)).getChildAt(0).setId(viewCode-viewCode%100+page.getViews().size());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_products:
                ((SetShopStructure) Objects.requireNonNull(getActivity())).selectProducts(page.getPageId(),page.getView(((View)v.getParent()).getId()), ((Integer) ((View) v.getParent()).getId())/100);
                break;
            case R.id.btnRight:
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.popup_what_you_want_to_do);
                dialog.setTitle("What Do You want to do?");
                ListView whatToDo = (ListView) dialog.findViewById(R.id.listWhatDo);
                whatToDo.setOnItemClickListener((parent, view, position, id) -> {
                    ((SetShopStructure)Objects.requireNonNull(getActivity())).selectView(page.getPageId(),position);
                    dialog.dismiss();
                });
                dialog.show();
                break;
            case R.id.view_deleter:

                int pId = ((View)v.getParent()).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("CONFIRM YOUR ACTIONS")
                        .setMessage("Are you sure you want to delete this view?" )
                        .setPositiveButton("DELETE", (dialog, which) -> {
                            deleteView(pId);
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        View parentView = (View)view.getParent();

        int currView = views.indexOf(parentView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)parentView.getLayoutParams();




        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                prevY = (int)motionEvent.getRawY();
                parent.requestDisallowInterceptTouchEvent(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                if((currView!=views.size()-1)&&params.topMargin>((RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams()).topMargin+((RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams()).height/2){
                    RelativeLayout.LayoutParams lowerViewParams = (RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams();
                    lowerViewParams.topMargin-=params.height;
                    views.get(currView+1).setLayoutParams(lowerViewParams);
                    swapViews(views,currView,currView+1);
                    currView++;
                    break;
                }
                else if((currView!=0)&&params.topMargin<((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).topMargin+((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).height/2){
                    RelativeLayout.LayoutParams lowerViewParams = (RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams();
                    lowerViewParams.topMargin+=params.height;
                    views.get(currView-1).setLayoutParams(lowerViewParams);
                    swapViews(views,currView,currView-1);
                    currView--;
                    break;
                }
                else{
                    params.topMargin+=((int)motionEvent.getRawY()-prevY);
                    parentView.setLayoutParams(params);
                    prevY = (int) motionEvent.getRawY();
                }

                return true;
            case MotionEvent.ACTION_UP:
                parent.requestDisallowInterceptTouchEvent(false);
                if((currView!=views.size()-1)&&params.topMargin+params.height>((RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams()).topMargin){
                    params.topMargin = ((RelativeLayout.LayoutParams)views.get(currView+1).getLayoutParams()).topMargin-params.height;
                }
                else if (currView!=0&&params.topMargin<((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).height){
                    params.topMargin = ((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)views.get(currView-1).getLayoutParams()).height;
                }
                else if(params.topMargin < 0){
                    params.topMargin = 0;
                }
                else if(params.topMargin+params.height>parent.getLayoutParams().height){
                    params.topMargin = parent.getLayoutParams().height - params.topMargin;
                }

                parentView.setLayoutParams(params);
                refreshViews();
                return true;
        }


        return false;
    }

    private void swapViews(ArrayList<View> views, int v1, int v2){
        View v = views.get(v1);
        views.set(v1,views.get(v2));
        views.set(v2,v);
    }

    public int getPageId(){
        return page.getPageId();
    }
}