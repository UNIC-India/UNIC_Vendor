package com.unic.unic_vendor_final_1.viewmodels;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;

public class AddShopViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();

    private MutableLiveData<Integer> shopAddStatus = new MutableLiveData<>();

    private FirebaseRepository firebaseRepository=new FirebaseRepository();

    public void saveShop(){

        shop.getValue().setOwnerId(firebaseRepository.getUserId());


        firebaseRepository.saveShop(shop.getValue())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        shop.getValue().setId(documentReference.getId());
                        shopAddStatus.setValue(1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Add Shop",e.toString());
                        shopAddStatus.setValue(-1);
                    }
                });
    }

    public void setShopId(){
        firebaseRepository.setShopId(shop.getValue().getId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        shopAddStatus.setValue(2);
                    }
                });
    }

    public void saveShopImage(byte[] data){
        firebaseRepository.saveShopImage(shop.getValue().getId(),data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        shopAddStatus.setValue(3);
                    }
                });
    }

    public void setShopImageLink(){
        firebaseRepository.getImageLink(shop.getValue().getId()).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                shop.getValue().setImageLink(uri.toString());
                setImageLink();
            }
        });
    }

    private void setImageLink(){
        firebaseRepository.setShopImage(shop.getValue().getId(),shop.getValue().getImageLink()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                shopAddStatus.setValue(4);
            }
        });
    }

    public LiveData<Integer> getShopAddStatus(){
        return shopAddStatus;
    }

    public MutableLiveData<Shop> getShop(){
        return shop;
    }

}
