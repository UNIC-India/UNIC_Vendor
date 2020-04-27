package com.unic.unic_vendor_final_1.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Order;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.User;

import java.util.ArrayList;
import java.util.List;

public class UserShopsViewModel extends ViewModel {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private MutableLiveData<List<Shop>> shops = new MutableLiveData<>();
    private MutableLiveData<List<Order>> orders=new MutableLiveData<>();
    private MutableLiveData<User> mUser = new MutableLiveData<>();
    private MutableLiveData<List<String>> shopids=new MutableLiveData<>();


    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public MutableLiveData<List<Shop>> getAllShops(){
        firebaseRepository.getAllShops(user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.w("FirestoreViewModel","Listen Failed",e);
                    return;
                }
                ArrayList<Shop> data = new ArrayList<>();
                ArrayList<String> ids=new ArrayList<>();

                assert snapshots != null;
                for(DocumentSnapshot doc : snapshots.getDocuments()) {
                    data.add(doc.toObject(Shop.class));
                    ids.add(doc.getId());
                }

                shops.setValue(data);
                shopids.setValue(ids);

            }
        });
        return shops;
    }

    public MutableLiveData<List<Order>> getAllOrders(){
        ArrayList<Order> orderdata=new ArrayList<>();
        firebaseRepository.getAllShops(user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                assert snapshots != null;
                ArrayList<Order> data = new ArrayList<>();
                for(DocumentSnapshot doc : snapshots.getDocuments()) {
                    firebaseRepository.db.collection("shops").document(doc.getId()).collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if(e!=null){
                                Log.w("FirestoreViewModel","Listen Failed",e);
                                return;
                            }



                            for(DocumentSnapshot doc2 :queryDocumentSnapshots.getDocuments()) {
                                data.add(doc2.toObject(Order.class));
                            }

                            orders.setValue(data);
                        }
                    });/*get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot doc2:task.getResult())
                                    orderdata.add(doc2.toObject(Order.class));
                                orders.setValue(orderdata);
                            }

                        }
                    });*/
                }

            }
        });

        return orders;

    }



    public void getUser(){
        firebaseRepository.getUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mUser.setValue(documentSnapshot.toObject(User.class));
            }
        });
    }

}
