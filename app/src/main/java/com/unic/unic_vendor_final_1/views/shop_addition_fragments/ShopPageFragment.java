package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.CategoryViewsAdapters.CategoriesAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ImageViewAdapters.DoubleImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.DoubleProductAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.TripleProductAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.SliderViewAdapters.SliderAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentShopPageBinding;
import com.unic.unic_vendor_final_1.datamodels.Page;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        parentLayoutParams.height = (int)dpToPx(page.getSize());
        parent.setLayoutParams(parentLayoutParams);
        inflateViews(page);
        return shopPageBinding.getRoot();
    }

    private void inflateViews(Page page){

        parent.removeAllViews();
        views.clear();

        for(com.unic.unic_vendor_final_1.datamodels.View view : page.getViews()){
            inflateView(view);
        }

    }

    private void inflateView(com.unic.unic_vendor_final_1.datamodels.View view){
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

                views.add(view21);

                CategoriesAdapter categoriesAdapter=new CategoriesAdapter(getContext());
                categoriesAdapter.setCategories(view.getData());
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

                views.add(view22);

                CategoriesAdapter categoriesAdapter2=new CategoriesAdapter(getContext());
                categoriesAdapter2.setCategories(view.getData());
                RecyclerView.LayoutManager categoriesLayoutManager2=new GridLayoutManager(getContext(),3);
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

                views.add(view31);

                SliderAdapter sliderAdapter = new SliderAdapter(getActivity(),view.getData());
                pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager viewPager = sliderView.findViewById(R.id.slider_images_flipper);

                viewPager.startAutoScroll();
                viewPager.setInterval(3000);
                viewPager.setCycle(true);
                viewPager.setStopScrollWhenTouch(true);
                viewPager.setAdapter(sliderAdapter);
                viewPager.setOffscreenPageLimit(3);
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

                views.add(view32);

                SliderAdapter sliderIndicatorAdapter = new SliderAdapter(getActivity(),view.getData());

                pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager viewIndicatorPager = sliderIndicatorView.findViewById(R.id.slider_images_flipper);

                viewIndicatorPager.startAutoScroll();
                viewIndicatorPager.setInterval(3000);
                viewIndicatorPager.setCycle(true);
                viewIndicatorPager.setStopScrollWhenTouch(true);
                viewIndicatorPager.setOffscreenPageLimit(3);

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

                views.add(view41);
                break;
            case 42:
                View tripleProductView = getLayoutInflater().inflate(R.layout.triple_product_view,parent,false);
                tripleProductView.setId(view.getViewCode());

                View view42 = getLayoutInflater().inflate(R.layout.view_bounding,parent,false);
                view42.setId(view.getViewCode());
                parent.addView(view42,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight()+30)));
                RelativeLayout.LayoutParams tripleProductViewParams = (RelativeLayout.LayoutParams) view42.getLayoutParams();
                tripleProductViewParams.topMargin = (int)dpToPx(view.getyPos()+30*viewPos);

                ((ViewGroup)view42.findViewById(R.id.view_loader)).addView(tripleProductView,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)dpToPx(view.getHeight())));
                view42.findViewById(R.id.view_deleter).setOnClickListener(this::onClick);
                view42.findViewById(R.id.view_dragger).setOnTouchListener(this::onTouch);


                TextView tvHeader2 = tripleProductView.findViewById(R.id.triple_image_header);
                tvHeader2.setText(view.getHeader());
                tripleProductView.findViewById(R.id.btn_add_products).setOnClickListener(this);

                TripleProductAdapter tripleProductAdapter = new TripleProductAdapter(getContext());
                tripleProductAdapter.setProducts(view.getData());
                LinearLayoutManager TripleProductLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                RecyclerView tripleProductRecyclerView = tripleProductView.findViewById(R.id.triple_image_recycler_view);
                tripleProductRecyclerView.setLayoutManager(TripleProductLayoutManager);
                tripleProductRecyclerView.setNestedScrollingEnabled(false);
                tripleProductRecyclerView.setAdapter(tripleProductAdapter);
                tripleProductRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
                views.add(view42);
                break;
            case 43:
                break;
            case 44:
                break;
            case 51:
                View Text_View=Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.text_view_item,parent,false);
                Text_View.setId(view.getViewCode());
                parent.addView(Text_View);
                EditText etText=Text_View.findViewById(R.id.etText);
                EditText etSize=Text_View.findViewById(R.id.etSize);
                TextView tvBold=Text_View.findViewById(R.id.tvBold);
                TextView tvItalics=Text_View.findViewById(R.id.tvItalic);
                etText.setText(view.getData().get(0).get("text")==null?" ":view.getData().get(0).get("text").toString());
                if(view.getData().get(0).containsKey("Bold")&&view.getData().get(0).get("Bold").toString().equals("True"))
                    etText.setTypeface(null, Typeface.BOLD);
                if(view.getData().get(0).containsKey("Italics")&&view.getData().get(0).get("Italics").toString().equals("True"))
                    etText.setTypeface(null, Typeface.ITALIC);
                if(view.getData().get(0).containsKey("Size"))
                    etText.setTextSize(Integer.parseInt(view.getData().get(0).get("Size").toString()));
                tvBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etText.getTypeface().getStyle()==Typeface.BOLD)
                            etText.setTypeface(null,Typeface.NORMAL);
                        else
                            etText.setTypeface(null,Typeface.BOLD);
                    }
                });
                tvItalics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etText.getTypeface().getStyle()==Typeface.ITALIC)
                            etText.setTypeface(null,Typeface.ITALIC);
                        else
                            etText.setTypeface(null,Typeface.ITALIC);
                    }
                });
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

    private void deleteView(int viewCode){
        for(int i=0;i<views.size();i++)
            if(viewCode==views.get(i).getId()){
                views.remove(i);
                break;
            }
        if(viewCode/1000==1||viewCode/1000==3)
            setStructureViewModel.deleteViewPics(page.getPageId(),viewCode);

        refreshViews();
    }

    private void refreshViews(){

        ArrayList<com.unic.unic_vendor_final_1.datamodels.View> oldViews = page.getViews();
        page.setViews(new ArrayList<>());
        page.setSize(0);

        for(int i=0;i<views.size();i++){
            int viewCode = views.get(i).getId();

            com.unic.unic_vendor_final_1.datamodels.View view = oldViews.get(viewCode%100-1);

            List<Map<String,Object>> data = view.getData();
            page.addView(view,viewCode/100);
            page.updateView(viewCode-viewCode%100+page.getViews().size(),data);

        }

        inflateViews(page);

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
                whatToDo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((SetShopStructure)Objects.requireNonNull(getActivity())).selectView(page.getPageId(),position);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.view_deleter:

                int pId = ((View)v.getParent()).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("CONFIRM YOUR ACTIONS")
                        .setMessage("Are you sure you want to delete this view?" )
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteView(pId);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
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
}
