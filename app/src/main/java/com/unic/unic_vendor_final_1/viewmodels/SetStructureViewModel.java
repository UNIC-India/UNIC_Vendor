package com.unic.unic_vendor_final_1.viewmodels;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.datamodels.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class SetStructureViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();
    private MutableLiveData<List<Map<String,Object>>> products = new MutableLiveData<>();
    private MutableLiveData<Structure> structure = new MutableLiveData<>();
    private MutableLiveData<List<Object>> adapters = new MutableLiveData<>();


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

    public MutableLiveData<List<Object>> getAdapters(){
        return adapters;
    }

    public MutableLiveData<Structure> getStructure() {
        return structure;
    }
}
