package com.unic.unic_vendor_final_1.messaging_service;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification()!=null){
            Toast.makeText(getApplicationContext(), remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Timber.d("Refreshed token: %s", token);
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token){
        Map<String,String> data = new HashMap<>();
        data.put("token",token);
        FirebaseFirestore.getInstance().collection("userTokens").document(token).set(data);
    }

}
