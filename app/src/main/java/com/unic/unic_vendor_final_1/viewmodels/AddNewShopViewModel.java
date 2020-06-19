package com.unic.unic_vendor_final_1.viewmodels;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;

public class AddNewShopViewModel extends ViewModel {

    private MutableLiveData<Shop> shop = new MutableLiveData<>();
    private MutableLiveData<Integer> status = new MutableLiveData<>();
    private MutableLiveData<Uri> shopImageUri = new MutableLiveData<>();
    private MutableLiveData<Uri> addressImageUri = new MutableLiveData<>();

    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void uploadShopData(){

        firebaseRepository.saveShop(shop.getValue())
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    status.setValue(1);
                    shop.getValue().setId(id);
                    setShopId(id);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    status.setValue(-1);
                });
    }

    private void setShopId(String id){
        firebaseRepository.setShopId(id)
                .addOnSuccessListener(aVoid -> setShopReady())
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    status.setValue(-1);
                });
    }

    private void setShopReady(){
        firebaseRepository.setShopReady(shop.getValue().getId(),false)
                .addOnSuccessListener(aVoid -> status.setValue(2))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    status.setValue(-1);
                });
    }

    public void uploadShopImage(byte[] data){

        firebaseRepository.saveShopImage(shop.getValue().getId(),data)
                .addOnSuccessListener(taskSnapshot -> {
                    status.setValue(3);
                    getShopImageLink();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    status.setValue(-1);
                });

    }

    public void getShopImageLink(){
        firebaseRepository.getImageLink(shop.getValue().getId())
                .addOnSuccessListener(uri -> {
                    shop.getValue().setImageLink(uri.toString());
                    saveShopImageLink(uri.toString());
                    status.setValue(4);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    status.setValue(-1);
                });
    }

    public void saveShopImageLink(String imageLink){
        firebaseRepository.setShopImage(shop.getValue().getId(),imageLink)
                .addOnSuccessListener(aVoid -> status.setValue(5))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    status.setValue(-1);
                });
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
                    status.setValue(-1);
                })
                .addOnSuccessListener(aVoid -> status.setValue(6));
    }

    public MutableLiveData<Shop> getShop() {
        return shop;
    }

    public MutableLiveData<Integer> getStatus() {
        return status;
    }

    public MutableLiveData<Uri> getAddressImageUri() {
        return addressImageUri;
    }

    public MutableLiveData<Uri> getShopImageUri() {
        return shopImageUri;
    }
}
