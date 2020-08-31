package com.unic.unic_vendor_final_1.commons;


import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.unic.unic_vendor_final_1.datamodels.Product;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.datamodels.User;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        return db.collection("users").document(user.getId()).set(user,SetOptions.merge());
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

        shop.setOwnerId(mUser.getUid());

        return db.collection("shops").add(shop);
    }

    public Task<DocumentReference> saveProduct(String shopId, Product product){
        return db.collection("shops").document(shopId).collection("products").add(product);
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

    public UploadTask saveProductImage(String shopId,String productId, byte[] data,int position){
        return mRef.child("shops").child(shopId).child("products").child(productId + "_" + Integer.valueOf(position).toString()).putBytes(data);
    }

    public UploadTask saveViewImage(String shopId, int pageId, int viewCode,int position, byte[] data){
        return mRef.child("shops").child(shopId).child("view images").child(Integer.valueOf(pageId).toString()+Integer.valueOf(viewCode).toString()+Integer.valueOf(position).toString()).putBytes(data);
    }

    public Task<Uri> getViewImageLink(String shopId, int pageId, int viewCode,int position){
        return mRef.child("shops").child(shopId).child("view images").child(Integer.valueOf(pageId).toString()+Integer.valueOf(viewCode).toString()+Integer.valueOf(position).toString()).getDownloadUrl();
    }

    public Task<Void> deleteViewImage(String imageUrl){
        return mRef.getStorage().getReferenceFromUrl(imageUrl).delete();
    }

    public Task<Uri> getImageLink(String shopId) {
        return mRef.child("shops").child(shopId).child("shopimage").getDownloadUrl();
    }

    public Task<Uri> getProductImageLink(String shopId,String productId,int position){
        return mRef.child("shops").child(shopId).child("products").child(productId + "_" + Integer.valueOf(position).toString()).getDownloadUrl();
    }

    public Task<Void> setShopImage(String shopId, String imageLink) {
        return db.collection("shops").document(shopId).update("imageLink", imageLink);
    }

    public Task<Void> setProductImage(String shopId,String ProductId, String imageId,List<String> imageLinks){
        return db.collection("shops").document(shopId).collection("products").document(ProductId).update("imageId",imageId,"imageLinks",imageLinks);
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

    public Task<QuerySnapshot> checkUser(String phoneNo){
        return db.collection("users").whereEqualTo("phoneNo",phoneNo).get();
    }

    public Task<QuerySnapshot> getPaginatedProducts(String shopId, DocumentSnapshot lastDoc, boolean isFirst){
        if(isFirst)
            return db.collection("shops").document(shopId).collection("products").orderBy("name", Query.Direction.ASCENDING).limit(25).get();
        else
            return db.collection("shops").document(shopId).collection("products").orderBy("name", Query.Direction.ASCENDING).startAfter(lastDoc).limit(25).get();
    }

    public Task<QuerySnapshot> getPaginatedOrders(List<String> shopIds,DocumentSnapshot lastDoc,boolean isFirst){
        if(isFirst)
            return db.collection("orders").whereIn("shopId",shopIds).orderBy("time", Query.Direction.DESCENDING).limit(25).get();
        return db.collection("orders").whereIn("shopId",shopIds).orderBy("time", Query.Direction.DESCENDING).limit(25).startAfter(lastDoc).get();
    }

    public Task<HttpsCallableResult> deleteShop(String shopId){
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        data.put("push",true);
        return mFunctions.getHttpsCallable("removeShop")
                .call(data);
    }

    public Task<String> deleteOrders(String shopId){
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        return mFunctions.getHttpsCallable("removeOrders")
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    return (String) task.getResult().getData();
                });
    }

    public Task<Void> deleteProducts(String shopId, String productId){
        return db.collection("shops").document(shopId).collection("products").document(productId).delete();
    }

    public Task<String> prepareProduct(String shopId, String company, String category, String subcategory){
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        data.put("company",company);
        data.put("category",category);
        data.put("subcategory",subcategory);
        return mFunctions.getHttpsCallable("addProduct")
                .call(data)
                .continueWith(task -> {
                    return (String)task.getResult().getData();
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

    public Task<Void> setShopReady(String shopId,Boolean ready){
        Map<String,Object> data = new HashMap<>();
        data.put("ready",ready);
        return db.collection("shops").document(shopId).set(data,SetOptions.merge());
    }

    public Task<DocumentSnapshot> getShopStructure(String shopId) {
        return db.collection("structures").document(shopId).get();
    }

    public Task<Void> setSubscribeLink(String shopId,Uri link){
        Map<String,Object> data = new HashMap<>();
        data.put("dynSubscribeLink",link.toString());
        return db.collection("shops").document(shopId).set(data, SetOptions.merge());
    }

    public void setInstanceId(String Uid, String token){
        db.collection("users").document(Uid).update("vendorInstanceId", token);
    }

    public Task<Void> setOrderStatus(String orderId, int orderStatus){
        return db.collection("orders").document(orderId).update("updateTime", FieldValue.serverTimestamp(),
                "orderStatus",orderStatus);
    }

    public Task<Void> updateOrderTotal(String orderId, double total){
        return db.collection("orders").document(orderId).update("total",total);
    }

    public Query getOrders(String shopId){
        return db.collection("orders").whereEqualTo("shopId",shopId);
    }

    public Task<QuerySnapshot> searchProductsByName(String shopId, String nameKey){
        return  db.collection("shops").document(shopId).collection("products").whereArrayContains("nameKeywords",nameKey.toLowerCase()).get();
    }

    public Task<QuerySnapshot> searchProductByNameRefineCompany(String shopId,String nameKey,String company){
        return db.collection("shops").document(shopId).collection("products").whereEqualTo("company",company).whereArrayContains("nameKeywords",nameKey.toLowerCase()).get();
    }

    public Task<QuerySnapshot> searchProductByNameRefineCategory(String shopId, String nameKey, String  category){
        return db.collection("shops").document(shopId).collection("products").whereEqualTo("category",category).whereArrayContains("nameKeywords",nameKey).get();
    }

    public Query getShopExtras(String shopId){
        return db.collection("shops").document(shopId).collection("extraData");
    }

    public Task<DocumentSnapshot> getUserPermissions(String shopId){
        return db.collection("shops").document(shopId).collection("extraData").document("userPermissions").get();
    }

    public UploadTask uploadSelectedImage(ByteArrayOutputStream baos){
        int time = (int) (System.currentTimeMillis());
        Timestamp tsTemp = new java.sql.Timestamp(time);
        String ts =  tsTemp.toString();
        return mRef.child(mUser.getUid()).child("images").child(ts).putBytes(baos.toByteArray());
    }

    public Task<Void> setAvailability(String shopId,String productId,boolean available){
        HashMap<String,Object> data = new HashMap<>();
        data.put("availability",available?1:-1);
        return db.collection("shops").document(shopId).collection("products").document(productId).set(data,SetOptions.merge());
    }

    public Task<QuerySnapshot> getProductsFromCategories(String shopId, List<String> categories){
        return db.collection("shops").document(shopId).collection("products").whereIn("category",categories).orderBy("name", Query.Direction.ASCENDING).get();
    }

    public Task<QuerySnapshot> getProductsFromCompanies(String shopId, List<String> companies){
        return db.collection("shops").document(shopId).collection("products").whereIn("company",companies).orderBy("name", Query.Direction.ASCENDING).get();
    }

    public Task<QuerySnapshot> getPaginatedCategoryProducts(String shopId, String category,boolean isFirst, DocumentSnapshot lastDoc){
        if(isFirst)
            return db.collection("shops").document(shopId).collection("products").whereEqualTo("category",category).orderBy("name", Query.Direction.ASCENDING).limit(25).get();
        return db.collection("shops").document(shopId).collection("products").whereEqualTo("category",category).orderBy("name", Query.Direction.ASCENDING).startAfter(lastDoc).limit(25).get();
    }

    public Task<QuerySnapshot> getPaginatedCompanyProducts(String shopId, String company,boolean isFirst, DocumentSnapshot lastDoc){
        if(isFirst)
            return db.collection("shops").document(shopId).collection("products").whereEqualTo("company",company).orderBy("name", Query.Direction.ASCENDING).limit(25).get();
        return db.collection("shops").document(shopId).collection("products").whereEqualTo("company",company).orderBy("name", Query.Direction.ASCENDING).startAfter(lastDoc).limit(25).get();
    }

    public Task<QuerySnapshot> getProductsByCategoryRefineSubcategory(String shopId, String category, String subcategory){
        return db.collection("shops").document(shopId).collection("products").whereEqualTo("category",category).whereEqualTo("subcategory",subcategory).get();
    }

    public Task<QuerySnapshot> getProductsByCategoryRefineCompany(String shopId, String company, String category){
        return db.collection("shops").document(shopId).collection("products").whereEqualTo("company",company).whereEqualTo("category",category).get();
    }

    public Task<HttpsCallableResult> reportUser(String shopId,String userId){
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        data.put("userId",userId);
        return mFunctions.getHttpsCallable("reportUser")
                .call(data);
    }

    public Task<HttpsCallableResult> allowUserAccess(String userId, String shopId,String shopName){
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        data.put("userId",userId);
        data.put("shopName",shopName);
        return  mFunctions.getHttpsCallable("allowViewShop")
                .call(data);
    }

    public Task<HttpsCallableResult> rejectUserAccess(String userId,String shopId,String shopName) {
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        data.put("userId",userId);
        data.put("shopName",shopName);
        return  mFunctions.getHttpsCallable("rejectViewShop")
                .call(data);
    }

    public void setShopPrivacy(String shopId, boolean isPrivate) {
        Map<String,Object> data = new HashMap<>();
        data.put("isPrivate",isPrivate);
        db.collection("shops").document(shopId).set(data, SetOptions.merge());
    }

    public Task<QuerySnapshot> getOrderReportByDateAndStatus(String shopId, int status, Date startDate,Date endDate) {

        if(status == 0)
            return db.collection("orders").whereEqualTo("shopId",shopId).whereEqualTo("orderStatus",0).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

        if(status == 1)
            return db.collection("orders").whereEqualTo("shopId",shopId).whereIn("orderStatus", Arrays.asList(1,2,3)).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

        if(status == 2)
            return db.collection("orders").whereEqualTo("shopId",shopId).whereEqualTo("orderStatus",4).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

        if(status == 3)
            return db.collection("orders").whereEqualTo("shopId",shopId).whereEqualTo("orderStatus",-1).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

        return null;
    }

    public Task<QuerySnapshot> getOrderReportByDate(String shopId,Date startDate,Date endDate) {

        return  db.collection("orders").whereEqualTo("shopId",shopId).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

    }

    public Task<QuerySnapshot> getAllOrdersReportByDateAndStatus(List<String> shopIds, int status, Date startDate,Date endDate) {

        if(status == 0)
            return db.collection("orders").whereIn("shopId",shopIds).whereEqualTo("orderStatus",0).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

        if(status == 1)
            return db.collection("orders").whereIn("shopId",shopIds).whereIn("orderStatus", Arrays.asList(1,2,3)).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

        if(status == 2)
            return db.collection("orders").whereIn("shopId",shopIds).whereEqualTo("orderStatus",4).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

        if(status == 3)
            return db.collection("orders").whereIn("shopId",shopIds).whereEqualTo("orderStatus",-1).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

        return null;

    }

    public Task<QuerySnapshot> getAllOrdersReportByDate(List<String> shopIds, Date startDate, Date endDate) {

        return  db.collection("orders").whereIn("shopId",shopIds).whereGreaterThanOrEqualTo("time",startDate).whereLessThanOrEqualTo("time",endDate).get();

    }

    public Task<HttpsCallableResult> revokeUserAccess(String userId,String shopId,String shopName){
        Map<String,Object> data = new HashMap<>();
        data.put("shopId",shopId);
        data.put("userId",userId);
        data.put("shopName",shopName);
        return mFunctions.getHttpsCallable("revokeShopViewPermissions")
                .call(data);
    }

    public void updateProductData(String shopId,String productId,Map<String,Object> data) {
        db.collection("shops").document(shopId).collection("products").document(productId).set(data,SetOptions.merge());
    }

    public void updateReport(String orderId){
        Map<String,Object> data = new HashMap<>();
        data.put("reported",true);
        db.collection("orders").document(orderId).set(data,SetOptions.merge());
    }

    public Task<ShortDynamicLink> createSubscribeLink(String shopId, String shopName,String  imageLink){
        String link = "https://nisarg2104.github.io/"+"?shopId="+shopId;
        return mDynamicLinks.createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://uniccustomer.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.unic.cust_final_1")
                                .setMinimumVersion(1)
                                .setFallbackUrl(Uri.parse("https://uniccustomer.page.link"))
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
                        .setTitle("Follow "+shopName+" on UNIC")
                        .setDescription("Check out my shop on UNIC, a platform where I can host my own shop at my convenience")
                        .setImageUrl(Uri.parse(imageLink))
                        .build()
                )
                .buildShortDynamicLink();
    }
}