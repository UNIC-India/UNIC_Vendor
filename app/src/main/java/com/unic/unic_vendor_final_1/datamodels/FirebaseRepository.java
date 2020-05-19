package com.unic.unic_vendor_final_1.datamodels;


import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FirebaseRepository {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mRef = FirebaseStorage.getInstance().getReference();
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance("asia-east2");
    private FirebaseDynamicLinks mDynamicLinks = FirebaseDynamicLinks.getInstance();

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
    public DocumentReference getCustomer(String userId){
        return db.collection("users").document(userId);
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

    public Task<QuerySnapshot> getPaginatedProducts(String shopId, DocumentSnapshot lastDoc, boolean isFirst){
        if(isFirst)
            return db.collection("shops").document(shopId).collection("products").orderBy("name", Query.Direction.ASCENDING).limit(25).get();
        else
            return db.collection("shops").document(shopId).collection("products").orderBy("name", Query.Direction.ASCENDING).startAfter(lastDoc).limit(25).get();
    }

    public Task<String> deleteShop(String shopId){
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        data.put("push",true);
        return mFunctions.getHttpsCallable("removeShop")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return (String) task.getResult().getData();
                    }
                });
    }

    public Task<String> deleteOrders(String shopId){
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        return mFunctions.getHttpsCallable("removeOrders")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return (String) task.getResult().getData();
                    }
                });
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

    public Task<Void> setInstanceId(String Uid,String token){
        return db.collection("users").document(Uid).update("vendorInstanceId",token);
    }

    public Task<Void> setOrderStatus(String orderId, int orderStatus){
        return db.collection("orders").document(orderId).update("orderStatus",orderStatus,
                "updateTime", FieldValue.serverTimestamp());
    }

    public Query getOrders(String shopId){
        return db.collection("orders").whereEqualTo("shopId",shopId);
    }

    public Query searchProductsByName(String nameKey,String shopId){
        return  db.collection("shops").document(shopId).collection("products").whereArrayContains("nameKeywords",nameKey.toLowerCase());
    }

    public Query getShopExtras(String shopId){
        return db.collection("shops").document(shopId).collection("extraData");
    }

    public UploadTask uploadSelectedImage(ByteArrayOutputStream baos){
        int time = (int) (System.currentTimeMillis());
        Timestamp tsTemp = new java.sql.Timestamp(time);
        String ts =  tsTemp.toString();
        return mRef.child(mUser.getUid()).child("images").child(ts).putBytes(baos.toByteArray());
    }

    public DynamicLink createSubscribeLink(String shopId, String shopName){
        String link = "https://nisarg2104.github.io/"+"?shopId="+shopId;
        return mDynamicLinks.createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://uniccustomer.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.unic.cust_final_1")
                        .setMinimumVersion(1)
                        .build()
                )
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                        .setSource("user")
                        .setMedium("social")
                        .setCampaign("shop-subscription")
                        .build()
                )
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                        .setTitle("Subscribe to "+shopName+" on UNIC")
                        .setDescription("Check out my shop on UNIC, a platform where I can host my own shop at my convenience")
                        .build()
                )
                .buildDynamicLink();

    }

}
