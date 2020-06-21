package com.waitty.waiter.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager
import com.waitty.waiter.R
import com.waitty.waiter.activity.HomeActivity
import com.waitty.waiter.activity.SplashActivity
import com.waitty.waiter.constant.constant
import com.waitty.waiter.retrofit.API
import com.waitty.waiter.utility.Utility
import org.json.JSONObject

class PushNotificationMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    storeNewToken(task.result!!.token)
                })
    }


    private fun storeNewToken(token: String) {
        val sharedPreferenceManager = SharedPreferenceManager(context = this,name = constant.NOTIFICATION_SP)
        if(TextUtils.isEmpty(sharedPreferenceManager.getStringPreference(constant.USER_FCMTOKENID))) {
            sharedPreferenceManager.storeStringPreference(constant.USER_FCMTOKENID, token)
        }

        val uniqueId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        sharedPreferenceManager.storeStringPreference(constant.USER_DEVICEID,uniqueId)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            if (remoteMessage.data.isNotEmpty()) {
                val obj = JSONObject(remoteMessage.data["body"] ?: "")
                if (SharedPreferenceManager(this,constant.LOGIN_SP).getBooleanPreference(constant.KEY_IS_LOGGED_IN,false)) {
                    if (obj.getInt(API.ORDER_STATUS_ID) == constant.ORDER_PLACED) {
                        Utility.setSharedPreferencesBoolean(this, constant.NEW_RELOAD_BY_FCM, true)
                        var notiCount = Utility.getSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_NEW)
                        notiCount += 1
                        Utility.setSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_NEW, notiCount)
                    } else if (obj.getInt(API.ORDER_STATUS_ID) == constant.ORDER_PREPARING) {
                        Utility.setSharedPreferencesBoolean(this, constant.NEW_RELOAD_BY_FCM, true)
                        Utility.setSharedPreferencesBoolean(this, constant.PROCESSING_RELOAD_BY_FCM, true)
                        var notiCount = Utility.getSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_PROCESSING)
                        notiCount += 1
                        Utility.setSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_PROCESSING, notiCount)
                    } else if (obj.getInt(API.ORDER_STATUS_ID) == constant.ORDER_READY_SERVE) {
                        Utility.setSharedPreferencesBoolean(this, constant.PROCESSING_RELOAD_BY_FCM, true)
                        var notiCount = Utility.getSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_PROCESSING)
                        notiCount += 1
                        Utility.setSharedPreferencesInteger(this, constant.NOTIFICATION_COUNT_PROCESSING, notiCount)
                    }

                      showNotificationServer(obj);
                }
                Log.d("NOTI", "Notification server $obj")
            } else if (remoteMessage.notification != null) {
                    showNotificationFCM(remoteMessage.notification?.body ?: "");
                Log.d("NOTI", "Notification FCM " + remoteMessage.notification!!.body)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // Notification handle received from server
    private fun showNotificationServer(OBJ: JSONObject) {
        try {
            val timeInMillis = System.currentTimeMillis()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(API.DATA, OBJ.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, timeInMillis.toInt(), intent, PendingIntent.FLAG_ONE_SHOT)
            val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this,constant.PRIMARY_CHANNEL)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(OBJ.getString(API.MESSAGE).trim())
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(OBJ.getString(API.MESSAGE).trim()))
                    .setContentIntent(pendingIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.drawable.notification_icon)
                notificationBuilder.color = resources.getColor(R.color.colorBlack)
            } else notificationBuilder.setSmallIcon(R.mipmap.launcher_icon)

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(timeInMillis.toInt(), notificationBuilder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //
    //    // Notification handle received from FCM
    private fun showNotificationFCM(msg: String) {
        try {
            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this,constant.PRIMARY_CHANNEL)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.drawable.notification_icon)
                notificationBuilder.setColor(getResources().getColor(R.color.colorBlack))
            } else notificationBuilder.setSmallIcon(R.mipmap.launcher_icon)

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notificationBuilder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}