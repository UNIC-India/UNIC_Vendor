package com.unic.unic_vendor_final_1.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.Structure;
import com.unic.unic_vendor_final_1.datamodels.User;

import java.io.File;

public class FirestoreDataViewModel extends ViewModel {

    private MutableLiveData<Integer> userStatus = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Integer> userSplashStatus = new MutableLiveData<>();

    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void addUser(final User user){
        firebaseRepository.saveUser(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setUserSplashStatus(user.getId(),1,true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userStatus.setValue(-1);
                    }
                });
    }

    public void getUserData(){
        firebaseRepository.getUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                user.setValue(documentSnapshot.toObject(User.class));
            }
        });
    }

    public void setUserSplashStatus(String Uid, final int status,boolean isNewUser){
        firebaseRepository.setUserSplashStatus(Uid,status,isNewUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(status==1)
                    userStatus.setValue(1);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AddUser",e.toString());
            }
        });
    }

    public LiveData<Integer> getUserSplashStatus(String Uid){

        firebaseRepository.getUserSplashStatus(Uid)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                            userSplashStatus.setValue(Integer.valueOf(documentSnapshot.get("status").toString()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userStatus.setValue(-1);
                    }
                });

        return userSplashStatus;

    }

    public LiveData<Integer> getUserStatus(){
        return userStatus;
    }

    public LiveData<User> getUser(){
        return user;
    }

}
