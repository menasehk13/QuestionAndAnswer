package com.example.questionandanswer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    private static final int NOTIFICATION_ID = 0;

    NotificationCompat.Builder notificationCompat;
    private NotificationManager notificationManager;
    private String channelId;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String name = remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");
        channelId = getString(R.string.default_notification_channel_id);
        notificationCompat = new NotificationCompat.Builder(this, channelId);

        Intent intent = new Intent(this, AnswerView.class);
        intent.putExtra("Questiion",name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        String channelName = getString(R.string.channel_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationCompat.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.questionlogo_round)
                .setSound(defaultSoundUri)
                .setContentTitle(name)
                .setContentText(body)
                .setTicker("High Tech")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setLights(Color.MAGENTA,300,100)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.MAGENTA);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationCompat.setChannelId(channelId);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(NOTIFICATION_ID, notificationCompat.build());
    }

    }

