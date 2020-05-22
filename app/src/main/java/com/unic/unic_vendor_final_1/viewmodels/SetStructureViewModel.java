package com.unic.unic_vendor_final_1.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SetStructureViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();
    private MutableLiveData<Fragment> currentFrag=new MutableLiveData<>();
    private MutableLiveData<Structure> structure = new MutableLiveData<>();
    private MutableLiveData<Integer> status = new MutableLiveData<>();
    private MutableLiveData<Integer> productStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> structureStatus = new MutableLiveData<>();
    private MutableLiveData<Map<String,List<String>>> shopExtras = new MutableLiveData<>();
    private MutableLiveData<List<Map<String,Object>>> products = new MutableLiveData<>();
    private MutableLiveData<List<Map<String,Object>>> categories = new MutableLiveData<>();
    private MutableLiveData<List<Map<String,Object>>> searchResults = new MutableLiveData<>();
    private DocumentSnapshot lastDoc=null;
    private boolean isFirst = true;

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

    public void getPaginatedProductData(String shopId){
        List<Map<String,Object>> productData;
        if(isFirst)
            productData = new ArrayList<>();
        else
            productData = products.getValue();
        firebaseRepository.getPaginatedProducts(shopId,lastDoc,isFirst)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size()==0)
                            return;
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments())
                            productData.add(doc.getData());
                        lastDoc = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);
                        products.setValue(productData);
                        productStatus.setValue(1);
                        isFirst=false;
                    }
                });

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

    public void getShopExtras(String  shopId){
        Map<String,List<String>> extras = new HashMap<>();
        firebaseRepository.getShopExtras(shopId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    switch (doc.getId()){
                        case "categories":
                            List<String> categories = doc.get("namesArray")!=null?(List<String>)doc.get("namesArray"):new ArrayList<>();
                            extras.put("categories",categories);
                        case "companies":
                            List<String> companies = doc.get("namesArray")!=null?(List<String>)doc.get("namesArray"):new ArrayList<>();
                            extras.put("companies",companies);
                    }
                }
                shopExtras.setValue(extras);
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

    public void searchProductsByName(String shopId,String nameKey){
        List<Map<String,Object>> data = new ArrayList<>();
        firebaseRepository.searchProductsByName(nameKey,shopId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot doc : queryDocumentSnapshots){
                    data.add(doc.getData());
                }
                searchResults.setValue(data);
            }
        });

    }
    public void clearSearch(){
        searchResults.setValue(null);
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

    public LiveData<Map<String,List<String>>> getShopExtras() {
        return shopExtras;
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

    public MutableLiveData<Fragment> getCurrentFrag() {
        return currentFrag;
    }

    public LiveData<List<Map<String, Object>>> getSearchResults() {
        return searchResults;
    }

    public void setCurrentFrag(Fragment currentFrag) {
        this.currentFrag.setValue(currentFrag);
    }
}
