package com.unic.unic_vendor_final_1.viewmodels;

import android.util.Log;

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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
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
    private MutableLiveData<List<Order>> orders = new MutableLiveData<>();
    private MutableLiveData<User> mUser = new MutableLiveData<>();
    private MutableLiveData<List<String>> shopids = new MutableLiveData<>();
    private MutableLiveData<User> customer = new MutableLiveData<>();
    private MutableLiveData<Integer> orderstatuschangestatus = new MutableLiveData<>();
    private MutableLiveData<Order> currentOrder = new MutableLiveData<>();


    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void getAllShops() {
        firebaseRepository.getAllShops(user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("FirestoreViewModel", "Listen Failed", e);
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

    public void getAllOrders() {
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

    public void updateOrderStatus(Order order, String status) {
        firebaseRepository.db.collection("shops").document(order.getShopId()).collection("orders").document(order.getId()).update("status", status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getAllOrders();
            }
        });
    }

    public void deleteShop(String shopId) {
        firebaseRepository.deleteOrders(shopId);
    }

    public void getUser() {
        firebaseRepository.getUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mUser.setValue(documentSnapshot.toObject(User.class));
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

    public LiveData<List<Order>> getOrders() {
        return orders;
    }
}
