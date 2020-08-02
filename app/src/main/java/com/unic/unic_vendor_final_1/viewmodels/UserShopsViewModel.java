package com.unic.unic_vendor_final_1.viewmodels;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Notification;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class UserShopsViewModel extends ViewModel {
    private Fragment currentFragment;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private MutableLiveData<List<Shop>> shops = new MutableLiveData<>();
    private MutableLiveData<User> mUser = new MutableLiveData<>();
    private MutableLiveData<List<String>> shopIds = new MutableLiveData<>();
    private MutableLiveData<User> customer = new MutableLiveData<>();
    private MutableLiveData<Map<String,String>> qrLinks = new MutableLiveData<>();
    private MutableLiveData<Map<String,List<Map<String,String >>>> userRequests = new MutableLiveData<>();

    private MutableLiveData<Boolean> isFirstOrder = new MutableLiveData<>();
    private MutableLiveData<DocumentSnapshot> lastOrderDoc = new MutableLiveData<>();
    public MutableLiveData<Boolean> isOrderUpdating =new MutableLiveData<>();
    public MutableLiveData<Boolean> isVisible= new MutableLiveData<>();
    public MutableLiveData<Integer> titleSetter=new MutableLiveData<>();


    public MutableLiveData<Boolean> isMyAppsLoading = new MutableLiveData<>();

    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void getAllShops() {
        firebaseRepository.getAllShops(user.getUid()).addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Timber.tag("FirestoreViewModel").w(e, "Listen Failed");
                return;
            }
            ArrayList<Shop> data = new ArrayList<>();
            ArrayList<String> ids = new ArrayList<>();

            assert snapshots != null;
            for (DocumentSnapshot doc : snapshots.getDocuments()) {
                data.add(doc.toObject(Shop.class));
                ids.add(doc.getId());

            }

            shops.setValue(data);
            shopIds.setValue(ids);

            if(ids.size()>0) {
                getAllNotifications();
            }
        });
    }

    public void deleteShop(String shopId) {
        firebaseRepository.deleteShop(shopId);
    }

    public void getUser() {
        firebaseRepository.getUser().addSnapshotListener((documentSnapshot, e) -> mUser.setValue(documentSnapshot.toObject(User.class)));
    }

    public void buildSubscribeLink(String shopId,String shopName,String imageLink){
        firebaseRepository.createSubscribeLink(shopId,shopName,imageLink)
                .addOnSuccessListener(shortDynamicLink -> updateSubscribeLink(shopId,shortDynamicLink.getShortLink()))
                .addOnFailureListener(e -> e.printStackTrace());
    }

    public MutableLiveData<User> getCustomerData(String userId) {
        firebaseRepository.getCustomer(userId).addSnapshotListener((documentSnapshot, e) -> customer.setValue(documentSnapshot.toObject(User.class)));
        return customer;
    }

    private void updateSubscribeLink(String shopId, Uri link){

        firebaseRepository.setSubscribeLink(shopId,link)
                .addOnFailureListener(e -> e.printStackTrace());
    }

    public LiveData<List<Shop>> getShops() {
        return shops;
    }

    public LiveData<List<String>> getShopIds() {
        return shopIds;
    }

    //==============For Orders==============//

    private MutableLiveData<List<Order>> orders = new MutableLiveData<>();
    private MutableLiveData<Integer> orderstatuschangestatus = new MutableLiveData<>();
    private MutableLiveData<Order> currentOrder = new MutableLiveData<>();
    private void getAllOrders() {
        List<Order> ordersList = new ArrayList<>();

        for (Shop shop : shops.getValue()) {

            firebaseRepository.getOrders(shop.getId()).addSnapshotListener((queryDocumentSnapshots, e) -> {

                List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();

                for (DocumentChange documentChange : documentChanges) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            ordersList.add(documentChange.getDocument().toObject(Order.class));
                            ordersList.sort(Order.compareByDate);
                            break;
                        case MODIFIED:
                            Order modifiedOrder = documentChange.getDocument().toObject(Order.class);
                            for (int i = 0; i < ordersList.size(); i++) {

                                Order order = ordersList.get(i);

                                if (order.getId().equals(modifiedOrder.getId())) {
                                    ordersList.set(i, modifiedOrder);
                                }
                            }
                            break;
                        case REMOVED:
                            ordersList.remove(documentChange.getDocument().toObject(Order.class));
                    }
                }
                orders.setValue(ordersList);

            });
        }
    }

    public void getPaginatedOrders(boolean isFirst,DocumentSnapshot lastDoc){

        if (shopIds.getValue()==null||shopIds.getValue().size()==0) {
            orders.setValue(new ArrayList<>());
            return;
        }
        List<Order> orderList ;

        if(isFirst)
            orderList = new ArrayList<>();
        else
            orderList = orders.getValue();

        if(!isFirst &&lastDoc==null)
            return;

        firebaseRepository.getPaginatedOrders(shopIds.getValue(),lastDoc,isFirst)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots==null||queryDocumentSnapshots.getDocuments().size()==0) {
                        lastOrderDoc.setValue(null);
                        orders.setValue(orderList);
                        return;
                    }
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                        orderList.add(doc.toObject(Order.class));
                    }
                    isFirstOrder.setValue(Boolean.FALSE);
                    lastOrderDoc.setValue(queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.getDocumentChanges().size()-1));
                    orders.setValue(orderList);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }
    public void updateOrderItems(Order order){

        firebaseRepository.db.collection("orders").document(order.getId()).update("items",order.getItems()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void setOrderStatus(String orderId,int orderStatus){
        setOrderStatus(orderId, orderStatus,0.0);
    }

    public void reportUser(String shopId,String userId,String orderId){

        firebaseRepository.reportUser(shopId,userId)
                .addOnSuccessListener(result -> {
                    firebaseRepository.updateReport(orderId);
                });

    }

    public void setOrderStatus(String orderId, int orderStatus,double total) {

        firebaseRepository.setOrderStatus(orderId, orderStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(orderStatus!=5)
                            isOrderUpdating.setValue(Boolean.FALSE);
                        else updateOrderTotal(orderId,total);
                    }
                })
                .addOnFailureListener(e -> Timber.e(e, e.toString()));
    }

    public void updateOrderTotal(String orderId,double total){
        firebaseRepository.updateOrderTotal(orderId,total)
                .addOnSuccessListener(aVoid -> isOrderUpdating.setValue(Boolean.FALSE));
    }

    public MutableLiveData<Order> listenToOrder(Order order) {
        currentOrder.setValue(order);
        firebaseRepository.db.collection("orders").document(order.getId()).addSnapshotListener((documentSnapshot, e) -> {
            if (currentOrder.getValue().getOrderStatus() != documentSnapshot.toObject(Order.class).getOrderStatus())
                currentOrder.setValue(documentSnapshot.toObject(Order.class));
        });
        return currentOrder;
    }

    public MutableLiveData<List<Order>> getOrders() {
        return orders;
    }

    public MutableLiveData<Boolean> getIsFirstOrder() {
        return isFirstOrder;
    }

    public MutableLiveData<DocumentSnapshot> getLastOrderDoc() {
        return lastOrderDoc;
    }

    //==============For Notifications==============//
    public MutableLiveData<Integer> notificationStatus=new MutableLiveData<>();
    public MutableLiveData<List<Notification>> notifications=new MutableLiveData<>();

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public void sendNotification(Notification notification){
        notificationStatus.setValue(1);
        firebaseRepository.db.collection("notifications").add(notification).addOnSuccessListener(documentReference -> notificationStatus.setValue(0));

    }

    private void getAllNotifications(){
        firebaseRepository.db.collection("notifications").whereIn("shopId",shopIds.getValue()).orderBy("time", Query.Direction.DESCENDING).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots==null)
                    return;
                List<Notification> notificationsData=new ArrayList<>();
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments())
                    notificationsData.add(doc.toObject(Notification.class));

                notifications.setValue(notificationsData);
            }
        });
    }




    //==============For Settings/ManageTeams==============//
    public MutableLiveData<List<Map<String,String>>> members=new MutableLiveData<>();
    public MutableLiveData<Integer> memberAddStatus=new MutableLiveData<>();

    public void addMember(String phone, String role, String shopId){
        List<String> phones=new ArrayList<>();
        phones.add("+91"+phone);
        Map<String,Object> data=new HashMap<>();
        data.put(role,phones);

        firebaseRepository.db.collection("shops").document(shopId).collection("extraData").document("team").get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.getData()==null) {
                firebaseRepository.db.collection("shops").document(shopId).collection("extraData").document("team").set(data).addOnSuccessListener(aVoid -> memberAddStatus.setValue(1));
            }
            else {
                if(documentSnapshot.getData().get(role)==null) {
                    firebaseRepository.db.collection("shops").document(shopId).collection("extraData").document("team").set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            memberAddStatus.setValue(1);
                        }
                    });
                }
                else {
                    firebaseRepository.db.collection("shops").document(shopId).collection("extraData")
                            .document("team").update(role, FieldValue.arrayUnion("+91"+phone)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            memberAddStatus.setValue(1);
                        }
                    });
                }
            }


        });
    }

    public MutableLiveData<List<Map<String,String>>> getAllMembers(String shopId){
        firebaseRepository.db.collection("shops").document(shopId).collection("extraData").document("team")
                .addSnapshotListener((documentSnapshot, e) -> {
                    Map<String,Object> data=new HashMap<>();
                    List<Map<String,String>> memberData=new ArrayList<>();
                    if(e!=null){
                        return;
                    }
                    if(documentSnapshot!=null && documentSnapshot.exists()) {
                        data.putAll(documentSnapshot.getData());
                        if(data.containsKey("salesMan")) {
                            for (String i : (ArrayList<String>) data.get("salesMan")) {
                                Map<String, String> a = new HashMap<>();
                                a.put("phoneNo", i);
                                a.put("role","salesMan");
                                memberData.add(a);
                            }
                        }
                        if(data.containsKey("deliveryMan")) {
                            for (String i : (ArrayList<String>) data.get("deliveryMan")) {
                                Map<String, String> a = new HashMap<>();
                                a.put("phoneNo", i);
                                a.put("role","deliveryMan");
                                memberData.add(a);
                            }
                        }
                        if(data.containsKey("preparer")) {
                            for (String i : (ArrayList<String>) data.get("preparer")) {
                                Map<String, String> a = new HashMap<>();
                                a.put("phoneNo", i);
                                a.put("role","preparer");
                                memberData.add(a);
                            }
                        }
                        if(data.containsKey("dispatcher")) {
                            for (String i : (ArrayList<String>) data.get("dispatcher")) {
                                Map<String, String> a = new HashMap<>();
                                a.put("phoneNo", i);
                                a.put("role","dispatcher");
                                memberData.add(a);
                            }
                        }
                        members.setValue(memberData);
                    }
                });
        return members;
    }

    public void deleteMember(String phone, String role, String shopId){
        firebaseRepository.db.collection("shops").document(shopId).collection("extraData")
                .document("team").update(role, FieldValue.arrayRemove(phone)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void getUserPermissions(String shopId){
        firebaseRepository.getUserPermissions(shopId).addOnSuccessListener(doc -> {
            if(!doc.exists()){
                userRequests.setValue(null);
                return;
            }

            Map<String,List<Map<String,String>>> data = new HashMap<>();

            if(doc.get("pending")!=null)
                data.put("pending", ((List<Map<String ,String>>) doc.get("pending")));

            if(doc.get("approved")!=null)
                data.put("approved",((List<Map<String ,String>>) doc.get("approved")));

            userRequests.setValue(data);
        });
    }

    public void allowUserAccess(String shopId, String userId){
        firebaseRepository.allowUserAccess(userId,shopId).addOnSuccessListener(result -> {

        });
    }

    public void revokeUserAccess(String shopId, String userId){
        firebaseRepository.revokeUserAccess(userId,shopId).addOnSuccessListener( result -> {

        });
    }

    public MutableLiveData<Boolean> getIsMyAppsLoading() {
        return isMyAppsLoading;
    }

    public MutableLiveData<Map<String, List<Map<String, String >>>> getUserRequests() {
        return userRequests;
    }

    public void setShopPrivacy(String shopId,boolean isPrivate){
        firebaseRepository.setShopPrivacy(shopId,isPrivate);
    }
}
