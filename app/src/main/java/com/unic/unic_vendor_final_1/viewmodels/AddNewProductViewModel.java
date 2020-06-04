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

public class AddNewProductViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository=new FirebaseRepository();
    public MutableLiveData<Product> product=new MutableLiveData<>();
    private MutableLiveData<Integer> productStatus= new MutableLiveData<>();
    private String shopId;

    public void saveProduct(){
        firebaseRepository.saveProduct(shopId,product.getValue())
                .addOnSuccessListener(documentReference -> {
                    product.getValue().setFirestoreId(documentReference.getId());
                    productStatus.setValue(1);
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

    public void uploadImage(byte[] data){
        firebaseRepository.saveProductImage(shopId,product.getValue().getFirestoreId(),data)
                .addOnSuccessListener(taskSnapshot -> {
                    productStatus.setValue(3);
                    setProductImageLink();
                })
                .addOnFailureListener(e -> {
                    productStatus.setValue(-1);
                    e.printStackTrace();
                });
    }

    public void setProductImageLink(){
        firebaseRepository.getProductImageLink(shopId,product.getValue().getFirestoreId())
                .addOnSuccessListener(uri -> {
                    product.getValue().setImageId(uri.toString());
                    setProductImageLinkOnFirebase();
                })
                .addOnFailureListener(e -> {
                    productStatus.setValue(-1);
                    e.printStackTrace();
                });
    }

    public void setProductImageLinkOnFirebase(){
        firebaseRepository.setProductImage(shopId,product.getValue().getFirestoreId(),product.getValue().getImageId())
                .addOnSuccessListener(aVoid -> productStatus.setValue(4))
                .addOnFailureListener(e -> {
                    productStatus.setValue(-1);
                    e.printStackTrace();
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



}
