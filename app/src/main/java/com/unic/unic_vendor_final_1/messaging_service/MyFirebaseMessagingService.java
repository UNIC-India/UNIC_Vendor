package com.unic.unic_vendor_final_1.messaging_service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unic.unic_vendor_final_1.R;
import com.unic.unic_vendor_final_1.views.activities.UserHome;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int ORDER_NOTIFICATION = 1001;
    private static final int REQUESTS_NOTIFICATION = 3001;

    private static final String ORDER_CHANNEL = "Orders";
    private static final String USER_REQUESTS_CHANNEL = "User Requests";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.unic_buisness_splash_screen_logo);

        Map<String, String> data = remoteMessage.getData();

        String channelName = "Default";
        String channelId = "Default";

        Intent intent;
        intent = new Intent(this, UserHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        switch (Integer.parseInt(data.get("channel"))) {
            case ORDER_NOTIFICATION:
                intent.putExtra("load","order");
                channelId = ORDER_CHANNEL;
                channelName = ORDER_CHANNEL;
                break;
            case REQUESTS_NOTIFICATION:
                intent.putExtra("load","settings");
                intent.putExtra("shopId",data.get("id"));
                channelId = USER_REQUESTS_CHANNEL;
                channelName = USER_REQUESTS_CHANNEL;
                break;

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(data.get("channel")), intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.app_logo)
                        .setLargeIcon(logo)
                        .setContentTitle(data.get("title"))
                        .setContentText(data.get("text"))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(data.get("text")))
                        .setGroup(channelId)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Orders",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(data.get("id").hashCode(), notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Timber.d("Refreshed token: %s", token);
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token){
        Map<String,String> data = new HashMap<>();
        data.put("vendorInstanceId",token);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getUid() != null)
            FirebaseFirestore.getInstance().collection("users").document(mAuth.getUid()).set(data, SetOptions.merge());
    }

}
