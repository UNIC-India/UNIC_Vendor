package com.unic.unic_vendor_final_1.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetStructureViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();
    private MutableLiveData<List<Object>> products = new MutableLiveData<>();

    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void getShopData(String shopId){
        firebaseRepository.getShop(shopId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                shop.setValue(documentSnapshot.toObject(Shop.class));
            }
        });
    }

    public void getProductDetails(String shopId){
        firebaseRepository.getProducts(shopId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Object> data = new ArrayList<>();
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("name",doc.get("name").toString());
                    map.put("imageLink",doc.get("imageLink").toString());
                    data.add(map);
                }

                products.setValue(data);
            }
        });
    }

    public MutableLiveData<Shop> getShop(){
        return shop;
    }

}
