package youtuvideos.tranty.vn.youtuvideos.services;

/**
 * Created by TRUC-SIDA on 2/27/2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.activities.LearningActivity;
import youtuvideos.tranty.vn.youtuvideos.activities.ModulesActivity;
import youtuvideos.tranty.vn.youtuvideos.dialog.SettingDialog;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    static final String LARGE_ICON = "largeIcon";
    static final String IMAGE = "image";
    static final String BODY = "body";
    static final String TITLE = "title";
    static final String KNOWLEDGE_ID = "knowledge_id";
    static final String DESCRIPTION = "description";

    public static final String KNOWLEDGE_USER_ID = "knowledge_user_id";
    public static final String ID = "knowledge_id";
    public static final String NOTIFICATION_ID = "notification_id";
    static final int ACTIVITY_SETTING = 1;
    static final int ACTIVITY_PLAYVIDEO = 2;

    private int notiId;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private Uri alarmSound;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        setupManager();
        if (data.get(LARGE_ICON) != null)
            showNoti(data.get(LARGE_ICON), data.get(BODY), data.get(TITLE), Integer.parseInt(data.get(KNOWLEDGE_USER_ID)));
        else
            showNotiKnowledge(data);
    }

    private void setupManager() {
        notiId = (int) System.currentTimeMillis();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    }

    private void showNotiKnowledge(Map<String, String> data) {

        Intent detailIntent  = new Intent(this, ModulesActivity.class);
        int knowledgeId =Integer.valueOf(data.get(KNOWLEDGE_ID));
        detailIntent.putExtra(KNOWLEDGE_ID,knowledgeId);
        detailIntent.putExtra(NOTIFICATION_ID, notiId);
        // Khai báo TaskStackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(detailIntent);
        PendingIntent intentVideo = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent cancelIntent = cancelIntent();
        Bitmap bitmap = getBitmapFromURL(data.get(IMAGE));
        Notification notif = new Notification.Builder(this)
                .setContentTitle(data.get(TITLE))
                .setContentText(data.get(DESCRIPTION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setSound(alarmSound)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(bitmap)
                .setSummaryText(data.get(DESCRIPTION)))
                .addAction(R.drawable.ic_play_white, getString(R.string.btn_play), intentVideo)
                .addAction(R.drawable.ic_play_white, getString(R.string.btn_dissmiss), cancelIntent)
                .build();
        manager.notify(notiId, notif);
    }

    private void showNoti(final String image, String body, String title, int course_id) {
        Bitmap bitmap = getBitmapFromURL(image);
        //** Pending Intent Video**//*
        PendingIntent intentVideo = setPendingIntent(course_id, ACTIVITY_PLAYVIDEO);
        //** Pending Intent Setting**//*
        PendingIntent intentSetting = setPendingIntent(course_id, ACTIVITY_SETTING);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentText(body)
                .setContentTitle(title)
                .setLargeIcon(bitmap)
                .setContentIntent(intentVideo)
                .addAction(R.drawable.ic_play_white, getString(R.string.btn_play), intentVideo)
                .addAction(R.drawable.ic_setting_white,getString(R.string.btn_turn_off), intentSetting)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body));
        final Notification notification = builder.build();

        manager.notify(notiId, notification);

    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
          //  Bitmap resized = myBitmap.createScaledBitmap(myBitmap, 1050, 150, true);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PendingIntent setPendingIntent(int course_id, int activity) {
        // Khai báo intent cho Activity tương ứng
        Intent detailIntent;
        if (activity == ACTIVITY_PLAYVIDEO)
            detailIntent = new Intent(this, LearningActivity.class);
        else
            detailIntent = new Intent(this, SettingDialog.class);

        detailIntent.putExtra(KNOWLEDGE_USER_ID, course_id);
        detailIntent.putExtra(NOTIFICATION_ID, notiId);
        // Khai báo TaskStackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(detailIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent cancelIntent(){
        Intent intentHide = new Intent(this, NotificationActionReceiver.class);
        intentHide.putExtra(NOTIFICATION_ID,notiId);
        return PendingIntent.getBroadcast(this, notiId, intentHide, PendingIntent.FLAG_CANCEL_CURRENT);
    }

}
