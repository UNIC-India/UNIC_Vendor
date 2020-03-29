package com.unic.unic_vendor_final_1.datamodels;


import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

public class FirebaseRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mRef = FirebaseStorage.getInstance().getReference();

    public void startPhoneNumberVerification(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    public void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token,PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                10,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    public Task<Void> saveUser(User user){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        return db.collection("users").document(user.getId()).set(user);
    }

    public DocumentReference getUser(){
        return db.collection("users").document(mUser.getUid());
    }

    public Query getAllShops(String ownerID){
        return db.collection("shops").whereEqualTo("ownerId",ownerID);
    }

    public Task<DocumentSnapshot> getShop(String shopId){
        return db.collection("shops").document(shopId).get();
    }

    public Task<DocumentReference> saveShop(Shop shop){
        return db.collection("shops").add(shop);
    }

    public Task<Void> setShopId(String id){
        return db.collection("shops").document(id).update("id",id);
    }

    public UploadTask saveShopImage(String shopId,byte[] data){
        return mRef.child("shops").child(shopId).child("shopimage").putBytes(data);
    }

    public Task<Uri> getImageLink(String shopId){
        return mRef.child("shops").child(shopId).child("shopimage").getDownloadUrl();
    }

    public Task<Void> setShopImage(String shopId,String imageLink){
        return db.collection("shops").document(shopId).update("imageLink",imageLink);
    }

    public UploadTask saveShopLogo(String shopId,byte[] data){
        return mRef.child("shops").child(shopId).child("shoplogo").putBytes(data);
    }

    public Task<Uri> getLogoLink(String shopId){
        return mRef.child("shops").child(shopId).child("shoplogo").getDownloadUrl();
    }

    public Task<Void> setShopLogo(String shopId,String logoLink){
        return db.collection("shops").document(shopId).update("logoLink",logoLink);
    }

    public Query getProducts(String shopId){
        return db.collection("products").whereEqualTo("shopId",shopId);
    }

    public Task<Void> setUserSplashStatus(String Uid,int status){
        return db.collection("imp_data").document(Uid).update("status",status);
    }

    public Task<DocumentSnapshot> getUserSplashStatus(){
        if (mUser == null)
            throw new NullPointerException();
        return db.collection("imp_data").document(mUser.getUid()).get();
    }

    public String getUserId(){
        return mUser.getUid();
    }

}
