package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductListAdapter;
import com.unic.unic_vendor_final_1.databinding.ActivityProductSelectorBinding;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;
import com.unic.unic_vendor_final_1.views.activities.SetShopStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSelector extends Fragment implements View.OnClickListener{

    private int viewCode;
    private int pageId;

    private SetStructureViewModel setStructureViewModel;
    private ActivityProductSelectorBinding productSelectorBinding;

    private ProductListAdapter adapter;

    private List<Map<String,Object>> data = new ArrayList<>();

    public ProductSelector(){}

    public ProductSelector(int pageId,int viewCode){
        this.viewCode = viewCode;
        this.pageId = pageId;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        productSelectorBinding = ActivityProductSelectorBinding.inflate(inflater,container,false);
        setStructureViewModel = ViewModelProviders.of(getActivity()).get(SetStructureViewModel.class);
        data = setStructureViewModel.getStructure().getValue().getPage(pageId).getView(viewCode).getData();
        adapter = new ProductListAdapter(getContext());

        setStructureViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Map<String, Object>>>() {
            @Override
            public void onChanged(List<Map<String, Object>> maps) {
                adapter.setProducts(maps);
                adapter.notifyDataSetChanged();
            }
        });

        setStructureViewModel.getProductStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1) {
                    adapter.setSelectedProducts(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        productSelectorBinding.productsSelectorRecyclerView.setLayoutManager(layoutManager);
        productSelectorBinding.productsSelectorRecyclerView.setAdapter(adapter);
        productSelectorBinding.btnConfirmProducts.setOnClickListener(this);
        return productSelectorBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_confirm_products){
            data = adapter.returnSelectedProducts();
            Structure structure = setStructureViewModel.getStructure().getValue();
            structure.updateProductList(pageId,viewCode,data);
            setStructureViewModel.setStructure(structure);
            ((SetShopStructure)getActivity()).returnToPage(pageId);
        }
    }
}
