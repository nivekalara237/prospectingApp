package com.niveka_team_dev.prospectingapp.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.activities.MainActivity;

import java.util.Map;

public class NewMessageNotification {
    private static final String NOTIFICATION_TAG = "NewMessage";
    private static final int NOTIFICATION_ID = 197;
    private static final int SUMMARY_ID = 0;

    @SuppressLint("RestrictedApi")
    public static void notify(final Context context, Map<String,String> map, final int number) {
        final Resources res = context.getResources();
        //Log.e("MAP<STRING,STRING>",map.toString());
        String channelId = res.getString(R.string.default_notification_channel_id); // default_channel_id
        String channeldesc = context.getString(R.string.default_notification_channel_title); // Default Channel
        String GROUP_KEY_WORK_TRANSACTION = context.getPackageName()+".WORK_TRANSACTION";
        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("marchand",map.get("marchand"));
        intent.putExtra("terminal",map.get("terminal"));
        intent.putExtra("prix",map.get("prix"));
        intent.putExtra("carte",map.get("carte"));
        intent.putExtra("transaction",map.get("transaction"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestID,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //final String ticker = notification.getBody();
        final String title = map.get("title");
        final String text = map.get("body");
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);
        inboxStyle.setSummaryText("Transaction");
        inboxStyle.addLine(text);

        //notificationChannel API 27

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(channelId);
            //NotificationChannelGroup mChannelGroup = notificationManager.get
            if (mChannel == null) {
                mChannel = new NotificationChannel(channelId, channeldesc, importance);
                mChannel.enableVibration(true);
                //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setLightColor(res.getColor(R.color.colorApp1));
                //mChannel.setGroup(GROUP_KEY_WORK_TRANSACTION);
                notificationManager.createNotificationChannel(mChannel);
            }
        }



        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId)
                //.setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColorized(true)
                .setColor(context.getResources().getColor(R.color.colorApp1))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(picture)
                //.setTicker(ticker)
                .setNumber(requestID)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setGroup(GROUP_KEY_WORK_TRANSACTION)
                .setGroupSummary(true)
                .setAutoCancel(true);
        Notification noti = builder.build();
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        noti.defaults |= Notification.DEFAULT_SOUND;

        notify(context, noti, notificationManager);
    }

    private static void notify(final Context context, final Notification notification,NotificationManager nm) {
        nm.notify(context.getString(R.string.app_name),NOTIFICATION_ID, notification);
    }

    /**
     * Cancels any notifications of this type previously shown using
     * .
     */

    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(context.getString(R.string.app_name), NOTIFICATION_ID);
    }


}
