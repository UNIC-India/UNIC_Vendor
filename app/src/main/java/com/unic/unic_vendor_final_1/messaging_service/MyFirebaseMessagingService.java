package com.unic.unic_vendor_final_1.messaging_service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.activities.UserHome;
import com.unic.unic_vendor_final_1.views.nav_fragments.MyOrders;

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

        if(remoteMessage.getData().get("load").equals("order")){
            Intent intent = new Intent(this, UserHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("load","order");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = "fcm_default_channel";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.logonotext)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
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
