package com.unic.unic_vendor_final_1.viewmodels;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.DoubleImageAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.SliderAdapter;
import com.unic.unic_vendor_final_1.adapters.shop_view_components.TripleImageAdapter;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.views.SetShopStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
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


public class SetStructureViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();
    private MutableLiveData<List<Map<String,Object>>> products = new MutableLiveData<>();
    private MutableLiveData<Structure> structure = new MutableLiveData<>();


    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void getShopData(final String shopId){
        firebaseRepository.getShop(shopId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                shop.setValue(documentSnapshot.toObject(Shop.class));
                getProductDetails(shopId);
            }
        });
    }

    public void getProductDetails(String shopId){

        firebaseRepository.getProducts(shopId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.d("SetStructure",e.toString());
                    return;
                }
                ArrayList<Map<String,Object>> data = new ArrayList<>();
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){

                    data.add(doc.getData());
                }

                products.setValue(data);
            }
        });
    }

    public MutableLiveData<Shop> getShop(){
        return shop;
    }

    public LiveData<List<Map<String,Object>>> getProducts(){
        return products;
    }

    public LiveData<ArrayList<String>> getCategories(){

        MutableLiveData<ArrayList<String>> categories = new MutableLiveData<>();

        ArrayList<String> data = new ArrayList<String>();
        for(int i = 0; i< Objects.requireNonNull(products.getValue()).size(); i++){
            String categ;
            categ = Objects.requireNonNull(products.getValue().get(i).get("category")).toString();
            if(data.contains(categ))
                 continue;
            data.add(categ);
        }
        categories.setValue(data);
        return categories;
    }

    public MutableLiveData<Structure> getStructure() {
        return structure;
    }

    public void addView(ViewGroup parent, int viewCode,ArrayList<Map<String,Object>> checkedProducts){
        switch(viewCode){
            case 22:
                View doubleImagesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_image_view,parent,false);
                RecyclerView doubleImagesRecyclerView = doubleImagesView.findViewById(R.id.double_image_recycler_view);
                DoubleImageAdapter doubleImageAdapter = new DoubleImageAdapter(parent.getContext());
                doubleImageAdapter.setProducts(checkedProducts);
                doubleImagesRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
                doubleImagesRecyclerView.setAdapter(doubleImageAdapter);
        }
    }
}
