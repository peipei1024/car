package com.js.car.myBroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.js.car.R;
import com.js.car.ui.BaseActivity;
import com.js.car.ui.LoginAndRegisterActivity;

import cn.bmob.push.PushConstants;

/**
 * Created by 景贝贝 on 2016/5/17.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Log.d("tishi", "客户端收到推送内容：" + intent.getStringExtra("msg"));

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Intent myintent = new Intent(context,LoginAndRegisterActivity.class);
            myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            PendingIntent pIntent = PendingIntent.getActivity(context,0,myintent,PendingIntent.FLAG_UPDATE_CURRENT);
//// Creates an explicit intent for an Activity in your app
//            Intent resultIntent = new Intent(context,LoginAndRegisterActivity.class);
//// The stack builder object will contain an artificial back stack for the
//// started Activity.
//// This ensures that navigating backward from the Activity leads out of
//// your application to the Home screen.
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//// Adds the back stack for the Intent (but not the Intent itself)
//            stackBuilder.addParentStack(LoginAndRegisterActivity.class);
//// Adds the Intent that starts the Activity to the top of the stack
//            stackBuilder.addNextIntent(resultIntent);
//            PendingIntent resultPendingIntent =
//                    stackBuilder.getPendingIntent(
//                            0,
//                            PendingIntent.FLAG_UPDATE_CURRENT
//                    );


            Notification notification = builder
                    .setContentTitle("APP名字：温馨提示")
                    .setContentText(intent.getStringExtra("msg"))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setColor(Color.parseColor("#EAA935"))
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pIntent)
                    .build();
            manager.notify(1, notification);


        }
    }
}
