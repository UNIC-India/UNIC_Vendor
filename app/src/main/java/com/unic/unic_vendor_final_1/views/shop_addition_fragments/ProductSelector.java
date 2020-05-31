package com.unic.unic_vendor_final_1.views.shop_addition_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductSelector extends Fragment implements View.OnClickListener{

    private int viewCode;
    private int pageId;

    private SetStructureViewModel setStructureViewModel;
    private FragmentProductSelectorBinding productSelectorBinding;

    private ProductListAdapter adapter;

    private List<Map<String,Object>> data = new ArrayList<>();
    private List<Map<String,Object>> extraData = new ArrayList<>();

    private boolean isFirst = true;
    private DocumentSnapshot lastDoc;

    private boolean isAtBottom = false;

    private int queryType = 0;

    public ProductSelector(){}

    public ProductSelector(int pageId,int viewCode){
        this.viewCode = viewCode;
        this.pageId = pageId;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        productSelectorBinding = FragmentProductSelectorBinding.inflate(inflater,container,false);
        setStructureViewModel = new ViewModelProvider(getActivity()).get(SetStructureViewModel.class);
        data = setStructureViewModel.getStructure().getValue().getPage(pageId).getView(viewCode).getData();
        adapter = new ProductListAdapter(getContext());

        setStructureViewModel.clearSearch();

        setStructureViewModel.getPaginatedProductData(isFirst,lastDoc,1);

        setStructureViewModel.setCurrentFrag(getActivity().getSupportFragmentManager().findFragmentById(R.id.shop_pages_loader));

        setStructureViewModel.getProducts().observe(getViewLifecycleOwner(), maps -> {
            adapter.setProducts(maps);
            adapter.notifyDataSetChanged();
        });

        setStructureViewModel.getStructure().observe(getViewLifecycleOwner(), structure -> {
            data = structure.getPage(pageId).getView(viewCode).getData();
            adapter.setSelectedProducts(data);
            adapter.notifyDataSetChanged();
        });

        setStructureViewModel.getSearchResults().observe(getViewLifecycleOwner(), maps ->{
            if(maps!=null) {
                adapter.setProducts(maps);
                adapter.notifyDataSetChanged();
            }
        });

        productSelectorBinding.productSelectionSwipe.setColorScheme(R.color.colorPrimary,R.color.colorSecondary,R.color.colorTertiary);

        productSelectorBinding.productSelectionSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setStructureViewModel.getLastProductSelectionDoc().setValue(null);
                setStructureViewModel.getIsFirstProductSelection().setValue(Boolean.TRUE);
                setStructureViewModel.clearSearch();
                setStructureViewModel.getPaginatedProductData(true,null,1);
            }
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
            structure.updateProductList(pageId,viewCode,data);
            setStructureViewModel.setStructure(structure);
            ((SetShopStructure) Objects.requireNonNull(getActivity())).returnToPage(pageId);
        }

        else if(v.getId()==R.id.btnleft){
            Structure structure = setStructureViewModel.getStructure().getValue();
            structure.deleteView(pageId,viewCode);
            setStructureViewModel.setStructure(structure);
        }
    }

    private void setFirst(boolean first) {
        isFirst = first;
    }

    private void setLastDoc(DocumentSnapshot lastDoc) {
        this.lastDoc = lastDoc;
    }

}
