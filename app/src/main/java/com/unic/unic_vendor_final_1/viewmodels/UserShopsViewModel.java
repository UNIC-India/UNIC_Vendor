package com.unic.unic_vendor_final_1.viewmodels;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Notification;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.User;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class UserShopsViewModel extends ViewModel {
    private Fragment currentFragment;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private MutableLiveData<List<Shop>> shops = new MutableLiveData<>();
    private MutableLiveData<List<Order>> orders = new MutableLiveData<>();
    private MutableLiveData<User> mUser = new MutableLiveData<>();
    private MutableLiveData<List<String>> shopids = new MutableLiveData<>();
    private MutableLiveData<User> customer = new MutableLiveData<>();
    private MutableLiveData<Integer> orderstatuschangestatus = new MutableLiveData<>();
    private MutableLiveData<Order> currentOrder = new MutableLiveData<>();
    private MutableLiveData<Map<String,String>> qrLinks = new MutableLiveData<>();
    private boolean isFirst = true;
    private DocumentSnapshot lastDoc;
    private MutableLiveData<String> selectedShopId= new MutableLiveData<>();
    public MutableLiveData<Integer> notificationStatus=new MutableLiveData<>();
    public MutableLiveData<List<Notification>> notifications=new MutableLiveData<>();
    public MutableLiveData<List<Map<String,String>>> members=new MutableLiveData<>();
    public MutableLiveData<Integer> memberAddStatus=new MutableLiveData<>();


    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void getAllShops() {
        firebaseRepository.getAllShops(user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
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
                getAllOrders();

            }
        });
    }

    private void getAllOrders() {
        List<Order> ordersList = new ArrayList<>();

        for (Shop shop : shops.getValue()) {
            firebaseRepository.getOrders(shop.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

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

                }
            });
        }
    }

    public void getPaginatedOrders(){
        List<Order> orderList ;

        if(isFirst)
            orderList = new ArrayList<>();
        else
            orderList = orders.getValue();

        firebaseRepository.getPaginatedOrders(shopids.getValue(),lastDoc,isFirst)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size()==0)
                            return;
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            orderList.add(doc.toObject(Order.class));
                        }
                        isFirst = false;
                        lastDoc = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.getDocumentChanges().size()-1);
                    }
                });

    }

    public void updateOrderStatus(Order order, String status) {
        firebaseRepository.db.collection("shops").document(order.getShopId()).collection("orders").document(order.getId()).update("status", status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getAllOrders();
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
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        updateSubscribeLink(shopId,shortDynamicLink.getShortLink());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public MutableLiveData<User> getCustomerData(String userId) {
        firebaseRepository.getCustomer(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                customer.setValue(documentSnapshot.toObject(User.class));
            }
        });
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

    public void setOrderStatus(String  orderId, int orderStatus) {

        firebaseRepository.setOrderStatus(orderId, orderStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        orderstatuschangestatus.setValue(5);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e, e.toString());
                    }
                });
    }
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

    public void delelteMember(String phone, String role, String shopId){
        firebaseRepository.db.collection("shops").document(shopId).collection("extraData")
                .document("team").update(role, FieldValue.arrayRemove(phone)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }
    public void sendNotification(Notification notification){
        notificationStatus.setValue(0);
        firebaseRepository.db.collection("notifications").add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                notificationStatus.setValue(1);
            }
        });

    }
    public MutableLiveData<List<Notification>> getNotificaions(){
        firebaseRepository.db.collection("notifications").whereIn("shopId",shopids.getValue()).orderBy("time", Query.Direction.DESCENDING).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            List<Notification> notificationsData=new ArrayList<>();
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentChange> documentChanges=new ArrayList<>();
                if(queryDocumentSnapshots!=null)
                    documentChanges = queryDocumentSnapshots.getDocumentChanges();

                for (DocumentChange documentChange : documentChanges) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            notificationsData.add(documentChange.getDocument().toObject(Notification.class));

                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            notificationsData.remove(documentChange.getDocument().toObject(Order.class));
                    }
                }
                notifications.setValue(notificationsData);
            }
        });
        return notifications;
    }

    public LiveData<List<Shop>> getShops() {
        return shops;
    }

    public MutableLiveData<Order> listenToOrder(Order order) {
        currentOrder.setValue(order);
        firebaseRepository.db.collection("orders").document(order.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (currentOrder.getValue().getOrderStatus() != documentSnapshot.toObject(Order.class).getOrderStatus())
                    currentOrder.setValue(documentSnapshot.toObject(Order.class));
            }
        });
        return currentOrder;
    }


    public MutableLiveData<String> getSelectedShopId() {
        return selectedShopId;
    }

    public void setSelectedShopId(MutableLiveData<String> selectedShopId) {
        this.selectedShopId = selectedShopId;
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
    }
}
