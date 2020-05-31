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
    private MutableLiveData<List<String>> shopids = new MutableLiveData<>();
    private MutableLiveData<User> customer = new MutableLiveData<>();
    private MutableLiveData<Map<String,String>> qrLinks = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFirstOrder = new MutableLiveData<>();
    private MutableLiveData<DocumentSnapshot> lastOrderDoc = new MutableLiveData<>();
    public MutableLiveData<Boolean> isOrderUpdating =new MutableLiveData<>();

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
            shopids.setValue(ids);

            if(ids.size()>0) {
                //getAllOrders();
                getAllNotifications();
            }
        });
    }

    public void deleteShop(String shopId) {
        firebaseRepository.deleteShop(shopId);
    }

    public void getUser() {
        firebaseRepository.getUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mUser.setValue(documentSnapshot.toObject(User.class));
            }
        });
    }

    public void buildSubscribeLink(String shopId,String shopName){
        firebaseRepository.createSubscribeLink(shopId,shopName)
                .addOnSuccessListener(shortDynamicLink -> updateSubscribeLink(shopId,shortDynamicLink.getShortLink()))
                .addOnFailureListener(e -> e.printStackTrace());
    }

    public MutableLiveData<User> getCustomerData(String userId) {
        firebaseRepository.getCustomer(userId).addSnapshotListener((documentSnapshot, e) -> customer.setValue(documentSnapshot.toObject(User.class)));
        return customer;
    }

    private void updateSubscribeLink(String shopId, Uri link){

        firebaseRepository.setSubscribeLink(shopId,link)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public LiveData<List<Shop>> getShops() {
        return shops;
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

        if (shopids.getValue()==null||shopids.getValue().size()==0)
            return;
        List<Order> orderList ;

        if(isFirst)
            orderList = new ArrayList<>();
        else
            orderList = orders.getValue();

        if(!isFirst &&lastDoc==null)
            return;

        firebaseRepository.getPaginatedOrders(shopids.getValue(),lastDoc,isFirst)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots==null||queryDocumentSnapshots.getDocuments().size()==0) {
                        lastOrderDoc.setValue(null);
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

    public void setOrderStatus(String  orderId, int orderStatus) {

        firebaseRepository.setOrderStatus(orderId, orderStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isOrderUpdating.setValue(Boolean.FALSE);
                    }
                })
                .addOnFailureListener(e -> Timber.e(e, e.toString()));
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
        notificationStatus.setValue(0);
        firebaseRepository.db.collection("notifications").add(notification).addOnSuccessListener(documentReference -> notificationStatus.setValue(1));

    }

    private void getAllNotifications(){
        firebaseRepository.db.collection("notifications").whereIn("shopId",shopids.getValue()).orderBy("time", Query.Direction.DESCENDING).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

        firebaseRepository.db.collection("shops").document(shopId).collection("extraData").document("team").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData()==null) {
                    firebaseRepository.db.collection("shops").document(shopId).collection("extraData").document("team").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            memberAddStatus.setValue(1);
                        }
                    });
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


            }
        });
    }

    public MutableLiveData<List<Map<String,String>>> getAllMembers(String shopId){
        firebaseRepository.db.collection("shops").document(shopId).collection("extraData").document("team")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {

                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
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
}
