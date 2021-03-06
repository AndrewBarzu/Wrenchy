package com.appist.xghos.Wrenchy.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;
import android.util.Log;

import com.appist.xghos.Wrenchy.R;
import com.appist.xghos.Wrenchy.start_activity.StartActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationService extends FirebaseMessagingService {

    public static  int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("+++", remoteMessage.toString());
        //Call method to generate notification
        if (remoteMessage.getData().size() > 0) {
            Log.d("dataa", "Data Payload: " + remoteMessage.getData());
            try {
                JSONObject dataObject = new JSONObject(remoteMessage.getData());
//                String imageURL = dataObject.getString("image");
                String title = dataObject.getString("title");
                //String message = dataObject.getString("name") + ", " + dataObject.getString("position") + ", has taken your offer!";
                String message = dataObject.getString("text");

                generateNotification(title, message);

//                if(imageURL.equals("")){
//                    generateNotification(title, message);
//                }else {
//                    Bitmap bitmap = getBitmapFromURL(imageURL);
//                    notificationWithImage(bitmap, title, message);
//                }
            } catch (Exception e) {
                Log.e("exc", "Exception: " + e.getMessage());
            }
        }

    }

    private void generateNotification(String title, String message) {
        Log.d("+++", "generate notif");
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Intent dismissIntent = new Intent(this, BroadcastReceiver.class);
        dismissIntent.putExtra("notification_id", NOTIFICATION_ID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piDismissIntent = PendingIntent.getBroadcast(this, 0, dismissIntent, 0);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .addAction(R.drawable.ic_close_24dp, "dismiss", piDismissIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (NOTIFICATION_ID > 1073741824) {
            NOTIFICATION_ID = 0;
        }
        Log.d("+++", NOTIFICATION_ID+"");
        notificationManager.notify(NOTIFICATION_ID++ , mNotifyBuilder.build());
    }

    private void notificationWithImage(Bitmap bitmap, String title, String message) {

        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Intent dismissIntent = new Intent(this, BroadcastReceiver.class);
        dismissIntent.setAction(Intent.ACTION_DELETE);
        dismissIntent.putExtra("notification_id", 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piDismissIntent = PendingIntent.getBroadcast(this, 0, dismissIntent, 0);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .addAction(R.drawable.ic_close_24dp, "dismiss", piDismissIntent)
                .setContentText(message);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (NOTIFICATION_ID > 1073741824) {
            NOTIFICATION_ID = 0;
        }
        notificationManager.notify(NOTIFICATION_ID++ , mNotifyBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
