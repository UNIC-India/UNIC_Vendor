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
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;

public class AddShopViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();

    private MutableLiveData<Integer> shopAddStatus = new MutableLiveData<>();
    public MutableLiveData<Integer> logoStatus=new MutableLiveData<>();

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
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                shopAddStatus.setValue(-2);
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
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                shopAddStatus.setValue(-3);
            }
        });
    }

    private void setImageLink(){
        firebaseRepository.setShopImage(shop.getValue().getId(),shop.getValue().getImageLink()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                buildSubscribeLink();
                shopAddStatus.setValue(4);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                shopAddStatus.setValue(-4);
            }
        });
    }
    public void saveShopLogo(String shopId,byte[] data){
        firebaseRepository.saveShopLogo(shopId,data)
                .addOnSuccessListener(taskSnapshot -> logoStatus.setValue(3))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    logoStatus.setValue(-2);
                });
    }

    public void setShopLogoLink(String shopId){
        firebaseRepository.getLogoLink(shopId).addOnSuccessListener(uri -> setLogoLink(shopId,uri.toString()))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    logoStatus.setValue(-3);
                });
    }

    private void setLogoLink(String shopId, String logoLink){
        firebaseRepository.setShopLogo(shopId,logoLink)
                .addOnSuccessListener(aVoid -> logoStatus.setValue(4))
                .addOnFailureListener(e -> logoStatus.setValue(-1));
    }

    public void buildSubscribeLink(){
        firebaseRepository.createSubscribeLink(shop.getValue().getId(),shop.getValue().getName(),shop.getValue().getImageLink())
                .addOnSuccessListener(shortDynamicLink -> updateSubscribeLink(shortDynamicLink.getShortLink()))
                .addOnFailureListener(e -> e.printStackTrace());
    }

    private void updateSubscribeLink(Uri link){

        firebaseRepository.setSubscribeLink(shop.getValue().getId(),link)
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    shopAddStatus.setValue(-1);
                })
                .addOnSuccessListener(aVoid -> shopAddStatus.setValue(5));
    }

    public LiveData<Integer> getShopAddStatus(){
        return shopAddStatus;
    }

    public MutableLiveData<Shop> getShop(){
        return shop;
    }

}
