package com.waitty.waiter.utility;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;

import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager;
import com.waitty.waiter.R;
import com.waitty.waiter.constant.constant;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager manager;

    // Class constructor
    public NotificationHelper(Context ctx) {
        super(ctx);

     createNotificationChannel();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(constant.PRIMARY_CHANNEL, name, importance);
            channel.setDescription(description);

            channel.setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            createNotificationChannelGroup();
        }
    }

    private void createNotificationChannelGroup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String groupId = constant.NOTIFICATION_CHANNEL_GROUP_ID;
// The user-visible name of the group.
            CharSequence groupName = getString(R.string.channel_group_name);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));
        }
    }
}