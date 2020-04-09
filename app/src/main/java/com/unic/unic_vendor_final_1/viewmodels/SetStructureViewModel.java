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
    private MutableLiveData<List<String>> categories = new MutableLiveData<>();

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
        final List<String> categoryData = new ArrayList<>();
        firebaseRepository.getProducts(shopId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots!=null)
                for(DocumentSnapshot doc : queryDocumentSnapshots){
                    productData.add(doc.getData());
                    if(!categoryData.contains(Objects.requireNonNull(doc.get("category")).toString()))
                        categoryData.add(Objects.requireNonNull(doc.get("category")).toString());
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
                        structure.setValue(documentSnapshot.toObject(Structure.class));
                        structureStatus.setValue(1);
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
}
