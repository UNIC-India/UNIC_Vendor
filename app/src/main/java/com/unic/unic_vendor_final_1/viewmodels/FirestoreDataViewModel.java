package com.unic.unic_vendor_final_1.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;
import com.unic.unic_vendor_final_1.datamodels.User;

public class FirestoreDataViewModel extends ViewModel {

    private MutableLiveData<Integer> userStatus = new MutableLiveData<>();


    private FirebaseRepository firebaseRepository = new FirebaseRepository();

    public void addUser(User user){
        firebaseRepository.saveUser(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userStatus.setValue(1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userStatus.setValue(-1);
                    }
                });
    }

    public LiveData<Integer> getUserStatus(){
        return userStatus;
    }

}
