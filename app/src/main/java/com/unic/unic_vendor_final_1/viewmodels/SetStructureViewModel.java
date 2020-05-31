package com.unic.unic_vendor_final_1.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;

import java.util.ArrayList;
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
    private MutableLiveData<List<Map<String,Object>>> companies = new MutableLiveData<>();
    private MutableLiveData<List<Map<String,Object>>> searchResults = new MutableLiveData<>();
    private DocumentSnapshot lastDoc=null;
    private boolean isFirst = true;

    private MutableLiveData<Boolean> isFirstProductSelection = new MutableLiveData<>();
    private MutableLiveData<DocumentSnapshot> lastProductSelectionDoc = new MutableLiveData<>();

    private MutableLiveData<Boolean> isFirstProduct = new MutableLiveData<>();
    private MutableLiveData<DocumentSnapshot> lastProductDoc = new MutableLiveData<>();

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

    public void getPaginatedProductData(boolean isFirst,DocumentSnapshot lastDoc,int where){
        List<Map<String,Object>> productData;
        if(isFirst)
            productData = new ArrayList<>();
        else
            productData = products.getValue();

        if(!isFirst&&lastDoc==null)
            return;

        firebaseRepository.getPaginatedProducts(shop.getValue().getId(),lastDoc,isFirst)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots==null||queryDocumentSnapshots.size()==0) {
                            lastProductSelectionDoc.setValue(null);
                            return;
                        }
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments())
                            productData.add(doc.getData());

                        products.setValue(productData);
                        productStatus.setValue(1);

                        switch (where){
                            case 1:
                                lastProductSelectionDoc.setValue(queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1));;
                                isFirstProductSelection.setValue(Boolean.FALSE);
                                break;
                            case 2:
                                lastProductDoc.setValue(queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1));
                                isFirstProduct.setValue(Boolean.FALSE);
                                break;

                        }
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

                if (queryDocumentSnapshots==null)
                    return;

                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    switch (doc.getId()){
                        case "categories":
                            List<String> categoriesList = doc.get("namesArray")!=null?(List<String>)doc.get("namesArray"):new ArrayList<>();
                            extras.put("categories",categoriesList);

                            List<Map<String,Object>> categoryData = new ArrayList<>();

                            for(String category : categoriesList){
                                Map<String,Object> data = new HashMap<>();
                                data.put("cname",category);
                                categoryData.add(data);
                            }

                            categories.setValue(categoryData);

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

    public void searchProductsByName(String nameKey){
        List<Map<String,Object>> data = new ArrayList<>();
        firebaseRepository.searchProductsByName(shop.getValue().getId(),nameKey).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots==null)
                    return;
                for (DocumentSnapshot doc : queryDocumentSnapshots){
                    data.add(doc.getData());
                }
                searchResults.setValue(data);
            }
        });

    }

    public void searchProductsByNameRefineByCompany(String nameKey,String company){
        List<Map<String,Object>> data = new ArrayList<>();
        firebaseRepository.searchProductByNameRefineCompany(shop.getValue().getId(),nameKey,company).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots==null)
                    return;
                for (DocumentSnapshot doc : queryDocumentSnapshots){
                    data.add(doc.getData());
                }
                searchResults.setValue(data);
            }
        });
    }

    public void searchProductsByNameRefineCategory(String nameKey,String category){
        List<Map<String,Object>> data = new ArrayList<>();

        firebaseRepository.searchProductByNameRefineCategory(shop.getValue().getId(),nameKey,category).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots==null)
                    return;
                for(DocumentSnapshot doc : queryDocumentSnapshots){
                    data.add(doc.getData());
                }
                searchResults.setValue(data);
            }
        });
    }

    public void searchProductsByCategoryList(List<String> categories){

        List<Map<String,Object>> data = new ArrayList<>();

        if (categories.size()==0){
            searchResults.setValue(null);
            return;
        }

        firebaseRepository.getProductsFromCategories(shop.getValue().getId(), categories).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots==null)
                    return;
                for (DocumentSnapshot doc : queryDocumentSnapshots){
                    data.add(doc.getData());
                }
                searchResults.setValue(data);
            }
        });
    }

    public void searchProductsByCompanyList(List<String> companies){

        List<Map<String,Object>> data = new ArrayList<>();

        if (companies.size()==0){
            searchResults.setValue(null);
            return;
        }

        firebaseRepository.getProductsFromCompanies(shop.getValue().getId(),companies).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots==null)
                    return;
                for (DocumentSnapshot doc : queryDocumentSnapshots){
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

    public MutableLiveData<List<Map<String, Object>>> getCategories() {
        return categories;
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

    public MutableLiveData<Fragment> getCurrentFrag() {
        return currentFrag;
    }

    public LiveData<List<Map<String, Object>>> getSearchResults() {
        return searchResults;
    }

    public void setCurrentFrag(Fragment currentFrag) {
        this.currentFrag.setValue(currentFrag);
    }

    public MutableLiveData<Boolean> getIsFirstProductSelection() {
        return isFirstProductSelection;
    }

    public MutableLiveData<DocumentSnapshot> getLastProductSelectionDoc() {
        return lastProductSelectionDoc;
    }

    public MutableLiveData<Boolean> getIsFirstProduct() {
        return isFirstProduct;
    }

    public MutableLiveData<DocumentSnapshot> getLastProductDoc() {
        return lastProductDoc;
    }
}
