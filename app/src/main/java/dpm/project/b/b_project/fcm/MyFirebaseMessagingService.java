package dpm.project.b.b_project.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import dpm.project.b.b_project.R;
import dpm.project.b.b_project.main.MainActivity;
import dpm.project.b.b_project.util.Const;
import dpm.project.b.b_project.util.Log;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private SharedPreferences sharedPreferences;
    @Override
    public void onNewToken(String token) {
        Log.e("Refreshed token: " + token);

    }

    // 메시지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("onMessageReceived : " + remoteMessage.getNotification().getBody());
        sharedPreferences = getSharedPreferences("bproject",MODE_PRIVATE);
        if(isFirstInfo(Const.ENTER_DATE) && isFirstInfo(Const.MONTHLY_PAY) && isFirstInfo(Const.SALARY_DAY) && isFirstInfo(Const.WORK_START_AND_END_AT)){
            return;
        }
        String title = remoteMessage.getNotification().getTitle();
        String messagae = remoteMessage.getNotification().getBody();

        sendNotification(title, messagae);
    }

    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Log.e(title + "//" + message);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("blife", "blife", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "blife")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private boolean isFirstInfo(String key){
        return !sharedPreferences.contains(key);
    }
}