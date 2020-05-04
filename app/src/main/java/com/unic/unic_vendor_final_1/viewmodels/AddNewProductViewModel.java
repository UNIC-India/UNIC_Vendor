package com.unic.unic_vendor_final_1.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Product;
import com.unic.unic_vendor_final_1.datamodels.Shop;

public class AddNewProductViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository=new FirebaseRepository();
    public MutableLiveData<Product> product=new MutableLiveData<>();
    private MutableLiveData<Integer> productStatus= new MutableLiveData<>();
    private String shopId;

    public void saveProduct(){
        firebaseRepository.saveProduct(shopId,product.getValue()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                product.getValue().setFirestoreId(documentReference.getId());
                productStatus.setValue(1);
            }
        });
    }

    public void setProductId(){
        firebaseRepository.setProductId(shopId,product.getValue().getFirestoreId()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                productStatus.setValue(2);
            }
        });
    }

    public void uploadImage(byte[] data){
        firebaseRepository.saveProductImage(shopId,product.getValue().getFirestoreId(),data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                productStatus.setValue(3);
            }
        });
    }

    public void setProductImageLink(){
        firebaseRepository.getProductImageLink(shopId,product.getValue().getFirestoreId()).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                product.getValue().setImageid(uri.toString());
                setProductImageLinkonFirebase();
            }
        });
    }

    public void setProductImageLinkonFirebase(){
        firebaseRepository.setProductImage(shopId,product.getValue().getFirestoreId(),product.getValue().getImageid()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                productStatus.setValue(4);
            }
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
