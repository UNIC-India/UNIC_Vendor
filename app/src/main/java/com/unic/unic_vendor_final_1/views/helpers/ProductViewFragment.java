package com.unic.unic_vendor_final_1.views.helpers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.ProductViewAdapters.ProductListAdapter;
import com.unic.unic_vendor_final_1.databinding.FragmentProductViewBinding;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.viewmodels.SetStructureViewModel;

public class ProductViewFragment extends Fragment {

    private FragmentProductViewBinding productViewBinding;
    private ProductListAdapter productListAdapter;
    private String shopId;
    private SetStructureViewModel setStructureViewModel;

    private boolean isFirst;
    private DocumentSnapshot lastDoc;

    private boolean isAtBottom = false;

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

        setStructureViewModel = new ViewModelProvider(this).get(SetStructureViewModel.class);
        setStructureViewModel.getShopData(shopId);

        setStructureViewModel.getShop().observe(getViewLifecycleOwner(),this::setShop);
        setStructureViewModel.getIsFirstMyProduct().observe(getViewLifecycleOwner(),this::setFirst);
        setStructureViewModel.getLastMyProductDoc().observe(getViewLifecycleOwner(),this::setLastDoc);

        productViewBinding.myProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productViewBinding.myProductsRecyclerView.setAdapter(productListAdapter);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_view, container, false);
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
}