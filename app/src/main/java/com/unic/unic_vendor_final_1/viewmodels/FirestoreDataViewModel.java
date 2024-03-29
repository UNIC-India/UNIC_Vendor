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
import com.unic.unic_vendor_final_1.commons.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.User;

import java.util.Objects;

import timber.log.Timber;

public class FirestoreDataViewModel extends ViewModel {

    private MutableLiveData<Integer> userStatus = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Integer> userSplashStatus = new MutableLiveData<>();
    private MutableLiveData<Exception> error = new MutableLiveData<>();

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
        firebaseRepository.getUser().get()
                .addOnSuccessListener(doc -> user.setValue(doc.toObject(User.class)))
                .addOnFailureListener(e -> error.setValue(e));
    }

    private void setUserSplashStatus(String Uid, final int status, boolean isNewUser){
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
                Timber.d(e.toString());
            }
        });
    }

    public LiveData<Integer> getUserSplashStatus(String Uid){

        firebaseRepository.getUserSplashStatus(Uid)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                            userSplashStatus.setValue(Integer.valueOf(Objects.requireNonNull(documentSnapshot.get("status")).toString()));
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

    public MutableLiveData<Exception> getError() {
        return error;
    }

    public LiveData<User> getUser(){
        return user;
    }

}
