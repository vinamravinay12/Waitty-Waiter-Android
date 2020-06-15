package com.waitty.waiter.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.waitty.waiter.R;
import com.waitty.waiter.activity.HomeActivity;
import com.waitty.waiter.activity.SplashActivity;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.utility.Utility;
import org.json.JSONObject;

public class PushNotificationMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {

                if (remoteMessage.getData().size() > 0) {

                    JSONObject obj = new JSONObject(remoteMessage.getData().get("body"));

                    if(Utility.getSharedPreferencesBoolean(this, constant.NOTIFICATIONS_SHOW) && Utility.getSharedPreferencesBoolean(this,constant.IS_LOGIN)) {

                        if (obj.getInt(API.ORDER_STATUS_ID) == constant.ORDER_PLACED) {
                            Utility.setSharedPreferencesBoolean(this, constant.NEW_RELOAD_BY_FCM, true);
                            int notiCount = Utility.getSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_NEW);
                            notiCount=notiCount+1;
                            Utility.setSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_NEW, notiCount);
                        } else if (obj.getInt(API.ORDER_STATUS_ID) == constant.ORDER_PREPARING) {
                            Utility.setSharedPreferencesBoolean(this, constant.NEW_RELOAD_BY_FCM, true);
                            Utility.setSharedPreferencesBoolean(this, constant.PROCESSING_RELOAD_BY_FCM, true);
                            int notiCount = Utility.getSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_PROCESSING);
                            notiCount=notiCount+1;
                            Utility.setSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_PROCESSING, notiCount);
                        } else if (obj.getInt(API.ORDER_STATUS_ID) == constant.ORDER_READY_SERVE) {
                            Utility.setSharedPreferencesBoolean(this, constant.PROCESSING_RELOAD_BY_FCM, true);
                            int notiCount = Utility.getSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_PROCESSING);
                            notiCount=notiCount+1;
                            Utility.setSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_PROCESSING, notiCount);
                        }

                      //  ShowNotificationServer(obj);
                    }

                    Log.d("NOTI", "Notification server " + obj);

                }else if (remoteMessage.getNotification() != null) {
                //    ShowNotificationFCM(remoteMessage.getNotification().getBody());
                    Log.d("NOTI", "Notification FCM " + remoteMessage.getNotification().getBody());
                }

        }catch(Exception e){e.printStackTrace(); }

    }

    // Notification handle received from server
//    private void ShowNotificationServer(JSONObject OBJ) {
//
//        try{
//
//            long when = System.currentTimeMillis();
//
//            Intent intent =  new Intent(this, HomeActivity.class);
//            intent.putExtra(API.DATA,OBJ.toString());
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            PendingIntent pendingIntent=PendingIntent.getActivity(this,(int) when, intent, PendingIntent.FLAG_ONE_SHOT);;
//
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(OBJ.getString(API.MESSAGE).trim())
//                    .setSound(defaultSoundUri)
//                    .setAutoCancel(true)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(OBJ.getString(API.MESSAGE).trim()))
//                    .setContentIntent(pendingIntent);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                notificationBuilder.setSmallIcon(R.drawable.notification_icon);
//                notificationBuilder.setColor(getResources().getColor(R.color.colorBlack));
//            }else
//                notificationBuilder.setSmallIcon(R.mipmap.launcher_icon);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    notificationBuilder.setChannelId(constant.PRIMARY_CHANNEL);
//                }
//
//            NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify((int) when, notificationBuilder.build());
//
//        }catch(Exception e){e.printStackTrace(); }
//    }
//
//    // Notification handle received from FCM
//    private void ShowNotificationFCM(String msg) {
//
//        try{
//
//            Intent intent = new Intent(this, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(msg)
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//                    .setContentIntent(pendingIntent);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                notificationBuilder.setSmallIcon(R.drawable.notification_icon);
//                notificationBuilder.setColor(getResources().getColor(R.color.colorBlack));
//            }else
//                notificationBuilder.setSmallIcon(R.mipmap.launcher_icon);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationBuilder.setChannelId(constant.PRIMARY_CHANNEL);
//            }
//
//            NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, notificationBuilder.build());
//
//        }catch(Exception e){e.printStackTrace(); }
//    }

}
