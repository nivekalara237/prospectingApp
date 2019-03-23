package com.niveka_team_dev.prospectingapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.niveka_team_dev.prospectingapp.ui.NewMessageNotification;

public class FireDFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = FireDFirebaseMessagingService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Message Notification Body: " + remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0) {
            //Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //NewMessageNotification.notify(getApplicationContext(),remoteMessage.getData(),0);
        }

    }

}
