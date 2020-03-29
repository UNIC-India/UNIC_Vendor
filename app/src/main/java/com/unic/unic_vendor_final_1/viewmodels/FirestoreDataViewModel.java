package com.unic.unic_vendor_final_1.viewmodels;

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
                        userStatus.setValue(1);
                        setUserSplashStatus(user.getId());
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

    public void setUserSplashStatus(String Uid){
        firebaseRepository.setUserSplashStatus(Uid,1);
    }

    public LiveData<Integer> getUserSplashStatus(){

        try {

            firebaseRepository.getUserSplashStatus()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userSplashStatus.setValue((Integer) documentSnapshot.get("status"));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            userStatus.setValue(-1);
                        }
                    });
        }
        catch (NullPointerException e){
            userSplashStatus.setValue(-1);
        }
        return userSplashStatus;

    }

    public LiveData<Integer> getUserStatus(){
        return userStatus;
    }

    public LiveData<User> getUser(){
        return user;
    }

}
