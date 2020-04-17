package com.unic.unic_vendor_final_1.viewmodels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.unic.unic_vendor_final_1.datamodels.FirebaseRepository;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagePickerViewModel extends ViewModel {

    private MutableLiveData<List<Map<String,String>>> imageLinks = new MutableLiveData<>();
    private MutableLiveData<Integer> uploadStatus = new MutableLiveData<>();
    private MutableLiveData<Map<String,String>> currentSet = new MutableLiveData<>();

    private FirebaseRepository firebaseRepository = new FirebaseRepository();


    public void uploadImage(Bitmap bitmap) {
        uploadStatus.setValue(0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        firebaseRepository.uploadSelectedImage(baos).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String,String> data = new HashMap<>();
                        data.put("imageId",uri.toString());
                        currentSet.setValue(data);
                        uploadStatus.setValue(1);
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uploadStatus.setValue(-1);
                    }
                });
    }

    public LiveData<Integer> getUploadStatus() {
        return uploadStatus;
    }

    public MutableLiveData<List<Map<String, String>>> getImageLinks() {
        return imageLinks;
    }

    public MutableLiveData<Map<String, String>> getCurrentSet() {
        return currentSet;
    }
}
