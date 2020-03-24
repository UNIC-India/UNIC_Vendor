package com.unic.unic_vendor_final_1.viewmodels;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Shop;
import com.unic.unic_vendor_final_1.datamodels.User;

import java.util.ArrayList;
import java.util.List;

public class UserShopsViewModel extends ViewModel {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private MutableLiveData<List<Shop>> shops = new MutableLiveData<>();
    private MutableLiveData<User> mUser = new MutableLiveData<>();

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
                for(DocumentSnapshot doc : snapshots.getDocuments())
                    data.add(doc.toObject(Shop.class));

                shops.setValue(data);
            }
        });
        return shops;
    }

    public void getUser(){
        firebaseRepository.getUser()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mUser.setValue(documentSnapshot.toObject(User.class));
                    }
                });
    }

}
