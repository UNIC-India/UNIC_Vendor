package com.unic.unic_vendor_final_1.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Product;

import java.util.ArrayList;
import java.util.List;

public class AddNewProductViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository=new FirebaseRepository();
    public MutableLiveData<Product> product=new MutableLiveData<>();
    private MutableLiveData<Integer> productStatus= new MutableLiveData<>();
    private MutableLiveData<List<String>> companies = new MutableLiveData<>();
    private MutableLiveData<List<String>> categories = new MutableLiveData<>();
    private String shopId;

    private MutableLiveData<Boolean> imageUploadStatus = new MutableLiveData<>();

    public void saveProduct(){
        firebaseRepository.saveProduct(shopId,product.getValue())
                .addOnSuccessListener(documentReference -> {
                    product.getValue().setFirestoreId(documentReference.getId());
                    productStatus.setValue(1);
                    firebaseRepository.prepareProduct(shopId,product.getValue().getCompany(),product.getValue().getCategory(),product.getValue().getSubcategory());
                    setProductId();
                })
                .addOnFailureListener(e -> {
                    productStatus.setValue(-1);
                    e.printStackTrace();
                });
    }

    public void setProductId(){
        firebaseRepository.setProductId(shopId,product.getValue().getFirestoreId())
                .addOnSuccessListener(aVoid -> productStatus.setValue(2))
                .addOnFailureListener(e -> {
                    productStatus.setValue(-1);
                    e.printStackTrace();
                });
    }

    public void uploadImage(byte[] data, int position){
        firebaseRepository.saveProductImage(shopId,product.getValue().getFirestoreId(),data,position)
                .addOnSuccessListener(taskSnapshot -> {
                    setProductImageLink(position);
                })
                .addOnFailureListener(e -> {
                    productStatus.setValue(-1);
                    e.printStackTrace();
                });
    }

    public void setProductImageLink(int position){
        firebaseRepository.getProductImageLink(shopId,product.getValue().getFirestoreId(),position)
                .addOnSuccessListener(uri -> {
                    if(position==0)
                        product.getValue().setImageId(uri.toString());

                    product.getValue().addToImageLinks(uri.toString());

                    imageUploadStatus.setValue(true);
                })
                .addOnFailureListener(e -> {
                    productStatus.setValue(-1);
                    e.printStackTrace();
                });
    }

    public void setProductImageLinkOnFirebase(){
        firebaseRepository.setProductImage(shopId,product.getValue().getFirestoreId(),product.getValue().getImageId(),product.getValue().getImageLinks())
                .addOnSuccessListener(aVoid -> productStatus.setValue(4))
                .addOnFailureListener(e -> {
                    productStatus.setValue(-1);
                    e.printStackTrace();
                });
    }

    public void getShopExtras() {
        firebaseRepository.getShopExtras(shopId).get().addOnSuccessListener(docs -> {
            if(docs == null)
                return;

            docs.getDocuments().forEach(doc -> {
                if(doc.getId().equals("companies")) {
                    List<String> companiesList = new ArrayList<>(doc.getData().keySet());
                    companiesList.remove("count");
                    companies.setValue(companiesList);
                }
                else if(doc.getId().equals("categories")) {
                    List<String> categoriesList = new ArrayList<>(doc.getData().keySet());
                    categoriesList.remove("count");
                    categories.setValue(categoriesList);
                }
            });



        });
    }

    public void setShopId(String shopId){
        this.shopId=shopId;
    }

    public LiveData<Product> getProduct(){
        return product;
    }
    public void setProduct(Product product){
        this.product.setValue(product);
    }

    public LiveData<Integer> getProductStatus(){
        return productStatus;
    }

    public MutableLiveData<Boolean> getImageUploadStatus() {
        return imageUploadStatus;
    }

    public LiveData<List<String>> getCompanies() {
        return companies;
    }

    public LiveData<List<String>> getCategories() {
        return categories;
    }
}
