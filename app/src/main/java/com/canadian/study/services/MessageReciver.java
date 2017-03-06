package com.canadian.study.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.canadian.study.R;
import com.canadian.study.bean.Message;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;

/**
 * Created by zheng on 16/9/18.
 */

public class MessageReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Message message = new Message();
            message.time = System.currentTimeMillis();
            int index = msg.indexOf("]");
            int start = msg.indexOf("[");
            if (index >= 0 && start >= 0) {
                message.title = msg.substring(start + 1, index);
            }
            message.content = msg.substring(index + 1);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Number id = realm.where(Message.class).max("id");
            message.id = id.longValue() + 1;
            realm.copyToRealm(message);
            realm.commitTransaction();

            Notification notification  = new NotificationCompat.Builder(context).setAutoCancel(true).setContentTitle(message.title).setContentText(message.content).setSmallIcon(R.drawable.ic_stat_name).build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0x000111, notification);
        }

    }
}
