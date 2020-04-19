package com.unic.unic_vendor_final_1.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SetStructureViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();
    private MutableLiveData<Structure> structure = new MutableLiveData<>();
    private MutableLiveData<Integer> status = new MutableLiveData<>();
    private MutableLiveData<Integer> productStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> structureStatus = new MutableLiveData<>();
    private MutableLiveData<List<Map<String,Object>>> products = new MutableLiveData<>();
    private MutableLiveData<List<Map<String,Object>>> categories = new MutableLiveData<>();

    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void getShopData(String shopId){
        firebaseRepository.getShop(shopId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                shop.setValue(documentSnapshot.toObject(Shop.class));
                status.setValue(1);
            }
        });
    }

    public void getProductData(String  shopId){
        final List<Map<String,Object>> productData = new ArrayList<>();
        final List<Map<String,Object>> categoryData = new ArrayList<>();
        List<Map<String,Object>> tempCat=new ArrayList<>();
        int i=0;
        firebaseRepository.getProducts(shopId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc:queryDocumentSnapshots) {
                    productData.add(doc.getData());
                    Map<String,Object> hp=new HashMap<>();
                    hp.put("cname",Objects.requireNonNull(doc.get("category")).toString());
                    if (!categoryData.contains(hp))
                        categoryData.add(hp);
                }
            }
        });


        products.setValue(productData);
        categories.setValue(categoryData);
        productStatus.setValue(1);
    }

    public void getStructureData(final String shopId){
        firebaseRepository
                .getShopStructure(shopId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            structure.setValue(documentSnapshot.toObject(Structure.class));
                            structureStatus.setValue(1);
                        }
                        else structureStatus.setValue(0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        structureStatus.setValue(0);
                    }
                });
    }

    public void saveShopStructure(){
        firebaseRepository.saveShopStructure(Objects.requireNonNull(structure.getValue()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        status.setValue(4);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.setValue(-1);
                    }
                });
    }

    public LiveData<Shop> getShop() {
        return shop;
    }

    public LiveData<Integer> getStatus() {
        return status;
    }

    public LiveData<Integer> getProductStatus() {
        return productStatus;
    }

    public LiveData<Integer> getStructureStatus() {
        return structureStatus;
    }

    public MutableLiveData<Structure> getStructure() {
        return structure;
    }

    public LiveData<List<Map<String, Object>>> getProducts() {
        return products;
    }

    public void setStructure(Structure structure) {
        this.structure.setValue(structure);
    }

    public MutableLiveData<List<Map<String, Object>>> getCategories() {
        return categories;
    }

    public void setCategories(MutableLiveData<List<Map<String, Object>>> categories) {
        this.categories = categories;
    }
}
