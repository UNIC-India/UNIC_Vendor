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

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FirebaseRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mRef = FirebaseStorage.getInstance().getReference();

    public void startPhoneNumberVerification(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    public void resendVerificationCode(String phoneNumber,
                                       PhoneAuthProvider.ForceResendingToken token, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                10,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    public Task<Void> saveUser(User user) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        return db.collection("users").document(user.getId()).set(user);
    }

    public DocumentReference getUser() {
        return db.collection("users").document(mUser.getUid());
    }

    public Query getAllShops(String ownerID) {
        return db.collection("shops").whereEqualTo("ownerId", ownerID);
    }

    public Task<DocumentSnapshot> getShop(String shopId) {
        return db.collection("shops").document(shopId).get();
    }

    public Task<DocumentReference> saveShop(Shop shop) {
        return db.collection("shops").add(shop);
    }

    public Task<DocumentReference> saveProduct(String shopId,Product product){
        Map<String,Object> productMap=new HashMap<>();
        productMap.put("firestoreId",product.getFirestoreId());
        productMap.put("subcategory",product.getSubcategory());
        productMap.put("shopid",product.getShopid());
        productMap.put("tags",product.getTags());
        productMap.put("imageid",product.getImageid());
        productMap.put("id",product.getId());
        productMap.put("name",product.getName());
        productMap.put("company",product.getCompany());
        productMap.put("category",product.getCategory());
        productMap.put("price",product.getPrice());
        return db.collection("shops").document(shopId).collection("products").add(productMap);
    }

    public Task<Void> setShopId(String id) {
        return db.collection("shops").document(id).update("id", id);
    }

    public Task<Void> setProductId(String shopId,String id){
        return db.collection("shops").document(shopId).collection("products").document(id).update("firestoreId",id);
    }

    public UploadTask saveShopImage(String shopId, byte[] data) {
        return mRef.child("shops").child(shopId).child("shopimage").putBytes(data);
    }

    public UploadTask saveProductImage(String shopId,String productId, byte[] data){
        return mRef.child("shops").child(shopId).child("products").child(productId).child("productimage").putBytes(data);
    }

    public Task<Uri> getImageLink(String shopId) {
        return mRef.child("shops").child(shopId).child("shopimage").getDownloadUrl();
    }

    public Task<Uri> getProductImageLink(String shopId,String productId){
        return mRef.child("shops").child(shopId).child("products").child(productId).child("productimage").getDownloadUrl();
    }

    public Task<Void> setShopImage(String shopId, String imageLink) {
        return db.collection("shops").document(shopId).update("imageLink", imageLink);
    }

    public Task<Void> setProductImage(String shopId,String ProductId, String imageid){
        return db.collection("shops").document(shopId).collection("products").document(ProductId).update("imageid",imageid);
    }

    public UploadTask saveShopLogo(String shopId, byte[] data) {
        return mRef.child("shops").child(shopId).child("shoplogo").putBytes(data);
    }

    public Task<Uri> getLogoLink(String shopId) {
        return mRef.child("shops").child(shopId).child("shoplogo").getDownloadUrl();
    }

    public Task<Void> setShopLogo(String shopId, String logoLink) {
        return db.collection("shops").document(shopId).update("logoLink", logoLink);
    }

    public Task<QuerySnapshot> getProducts(String shopId) {
        return db.collection("shops").document(shopId).collection("products").get();
    }



    public Task<Void> setUserSplashStatus(String Uid, int status, boolean isNewUser) {
        if (isNewUser) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("status", 1);
            return db.collection("imp_data").document(Uid).set(data);
        } else
            return db.collection("imp_data").document(Uid).update("status", status);
    }

    public Task<DocumentSnapshot> getUserSplashStatus(String Uid) {
        return db.collection("imp_data").document(Uid).get();
    }

    public String getUserId() {
        return mUser.getUid();
    }

    public Task<Void> saveShopStructure(Structure structure) {
        return db.collection("structures").document(structure.getShopId()).set(structure);
    }

    public Task<DocumentSnapshot> getShopStructure(String shopId) {
        return db.collection("structures").document(shopId).get();
    }


    public UploadTask uploadSelectedImage(ByteArrayOutputStream baos){
        int time = (int) (System.currentTimeMillis());
        Timestamp tsTemp = new java.sql.Timestamp(time);
        String ts =  tsTemp.toString();
        return mRef.child(mUser.getUid()).child("images").child(ts).putBytes(baos.toByteArray());
    }

}
