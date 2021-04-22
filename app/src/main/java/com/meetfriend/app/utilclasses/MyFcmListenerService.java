package com.meetfriend.app.utilclasses;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meetfriend.app.R;
import com.meetfriend.app.ui.activities.AudioCallActivity;
import com.meetfriend.app.ui.activities.CallLauncherActivity;
import com.meetfriend.app.ui.activities.HomeActivity;
import com.meetfriend.app.ui.activities.VideoCallActivity;

import org.json.JSONException;
import org.json.JSONObject;

import contractorssmart.app.utilsclasses.PreferenceHandler;

public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyFcmListenerService";
    Intent resultIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.e("fcm_message", remoteMessage.getData().toString());
   /*     if (remoteMessage.getData().size() > 0) {
        }
        if (remoteMessage.getNotification().getBody().equalsIgnoreCase("call notification")) {
            Map<String, String> map = remoteMessage.getData();
            String map1 = map.get("extra_data");
            JSONObject map2 = null;
            try {
                map2 = new JSONObject(map1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("map_data", map.toString());
            try {
                if (map2.getString("type").equalsIgnoreCase("missed")) {
                    if (map2.getString("call_type").equalsIgnoreCase("audio")) {
                        generateNotification(this, "Missed call", "You missed a voice call");

                    } else {
                        generateNotification(this, "Missed call", "You missed a video call");
                    }
                } else if (map2.getString("type").equalsIgnoreCase("disconnect")) {
                    if (map2.getString("call_type").equalsIgnoreCase("audio")) {
                        AudioCallActivity.disconnect("missed");
                    } else {
                        VideoCallActivity.disconnect("missed");
                    }
                } else if (map2.getString("call_type").equalsIgnoreCase("audio"))
                    AudioNotification(map2);
                else VideoNotification(map2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            generateNotification(this, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
  */
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        KeyguardManager myKM = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {
            //it is locked
            Log.e("fcm_message", intent.getExtras().toString());
            Bundle bundle = intent.getExtras();
            String map1 = bundle.getString("extra_data");
            if (map1 != null) {
                JSONObject map2 = null;
                try {
                    map2 = new JSONObject(map1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String map = bundle.getString("message");
                JSONObject map3 = null;
                try {
                    map3 = new JSONObject(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (map3.optString("body").equalsIgnoreCase("call notification")) {
                    try {
                        if (map2.getString("type").equalsIgnoreCase("missed")) {
                            if (PreferenceHandler.INSTANCE.readString(getApplicationContext(), "isOpen", "0").equalsIgnoreCase("1")) {
                                PreferenceHandler.INSTANCE.writeString(getApplicationContext(), "isOpen", "0");
                                new CallLauncherActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        CallLauncherActivity.binding.mBack.performClick();
                                    }
                                });
                            }
                            if (map2.getString("call_type").equalsIgnoreCase("audio")) {
                                generateNotification(this, "Missed call", "You missed a voice call");

                            } else {
                                generateNotification(this, "Missed call", "You missed a video call");
                            }
                        } else if (map2.getString("type").equalsIgnoreCase("disconnect")) {
                            if (map2.getString("call_type").equalsIgnoreCase("audio")) {
                                AudioCallActivity.disconnect("missed");
                            } else {
                                VideoCallActivity.disconnect("missed");
                            }
                        } else if (map2.getString("call_type").equalsIgnoreCase("audio")) {
                            AudioNotification(map2);
//                            notificationManager.cancel(1);
                        } else VideoNotification(map2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    generateNotification(this, map3.optString("title"), map3.optString("body"));
                }
            }
        } else {
            //it is not locked
            Log.e("fcm_message", intent.getExtras().toString());
            Bundle bundle = intent.getExtras();
            String map1 = bundle.getString("extra_data");
            if (map1 != null) {
                JSONObject map2 = null;
                try {
                    map2 = new JSONObject(map1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String map = bundle.getString("message");
                JSONObject map3 = null;
                try {
                    map3 = new JSONObject(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (map3.optString("body").equalsIgnoreCase("call notification")) {
                    try {
                        if (map2.getString("type").equalsIgnoreCase("missed")) {
                            if (map2.getString("call_type").equalsIgnoreCase("audio")) {
                                generateNotification(this, "Missed call", "You missed a voice call");

                            } else {
                                generateNotification(this, "Missed call", "You missed a video call");
                            }
                        } else if (map2.getString("type").equalsIgnoreCase("disconnect")) {
                            if (map2.getString("call_type").equalsIgnoreCase("audio")) {
                                AudioCallActivity.disconnect("missed");
                            } else {
                                VideoCallActivity.disconnect("missed");
                            }
                        } else if (map2.getString("call_type").equalsIgnoreCase("audio"))
                            AudioNotification(map2);
                        else VideoNotification(map2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (map2.getString("type").equalsIgnoreCase("chat")) {
                            if (!PreferenceHandler.INSTANCE.readString(getApplicationContext(), "user_id", "").equalsIgnoreCase(map2.getString("from_user"))) {
                                generateNotification(this, map3.optString("title"), map3.optString("body"));

                            }
                        } else
                            generateNotification(this, map3.optString("title"), map3.optString("body"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public void AudioNotification(JSONObject map) {
        final int NOTIFY_ID = 1;
        NotificationManager notifManager = null;
        // There are hardcoding only for show it's just strings
        String name = "my_package_channel";
        String id = "12456234546744658"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.
        Uri notification = Uri.parse(
                "android.resource://" +
                        getApplicationContext().getPackageName() + "/" + R.raw.ringtonecall);

        Intent intent;
        Intent intent1;
        PendingIntent pendingIntent;
        PendingIntent pendingIntent1;
        NotificationCompat.Builder builder = null;
        Intent intentd = new Intent(this, ActionReceiver.class);
        intentd.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentd.putExtra("action", "1");
        try {
            intentd.putExtra("friendId", map.getString("from_user"));
            intentd.putExtra("type", map.getString("call_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PendingIntent dismissIntent = PendingIntent.getBroadcast(this, 0, intentd, PendingIntent.FLAG_UPDATE_CURRENT);

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.setShowBadge(true);
                mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                mChannel.setImportance(importance);
                mChannel.setSound(notification, new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build());
//                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            intent1 = new Intent(this, CallLauncherActivity.class);
            intent = new Intent(this, AudioCallActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            try {
                intent.putExtra("uId", map.getString("uid"));
                intent.putExtra("name", map.getString("from_user_name"));
                intent.putExtra("image", map.getString("from_user_image"));
                intent.putExtra("id", map.getString("from_user"));
                intent.putExtra("channelName", map.getString("channelName"));
                intent.putExtra("accessToken", map.getString("token"));
                intent.putExtra("from", "notification");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                intent1.putExtra("uId", map.getString("uid"));
                intent1.putExtra("name", map.getString("from_user_name"));
                intent1.putExtra("image", map.getString("from_user_image"));
                intent1.putExtra("id", map.getString("from_user"));
                intent1.putExtra("channelName", map.getString("channelName"));
                intent1.putExtra("accessToken", map.getString("token"));
                intent1.putExtra("type", map.getString("call_type"));
                intent1.putExtra("from", "notification");
                intent1.putExtra("call_type", "audio");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            pendingIntent1 = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            long[] pattern = {500, 400, 300, 200, 400};
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(pattern, -1);
            v.cancel();

            try {
                builder.setContentTitle("Voice Call...")  // required
                        .setSmallIcon(R.drawable.ic_meet_friend_logo_white) // required
                        .setContentText(map.getString("from_user_name") + " is Calling you....")  // required
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setSound(notification)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setFullScreenIntent(pendingIntent1, true)
                        .setTicker(map.getString("from_user_name") + " is Calling you....")
                        .addAction(0, "Accept", pendingIntent)
                        .addAction(0, "Reject", dismissIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            builder = new NotificationCompat.Builder(this);

            intent1 = new Intent(this, CallLauncherActivity.class);
            intent = new Intent(this, AudioCallActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            try {
                intent.putExtra("uId", map.getString("uid"));
                intent.putExtra("name", map.getString("from_user_name"));
                intent.putExtra("image", map.getString("from_user_image"));
                intent.putExtra("id", map.getString("from_user"));
                intent.putExtra("channelName", map.getString("channelName"));
                intent.putExtra("accessToken", map.getString("token"));
                intent.putExtra("from", "notification");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                intent1.putExtra("uId", map.getString("uid"));
                intent1.putExtra("name", map.getString("from_user_name"));
                intent1.putExtra("image", map.getString("from_user_image"));
                intent1.putExtra("id", map.getString("from_user"));
                intent1.putExtra("channelName", map.getString("channelName"));
                intent1.putExtra("accessToken", map.getString("token"));
                intent1.putExtra("from", "notification");
                intent1.putExtra("type", map.getString("call_type"));
                intent1.putExtra("call_type", "audio");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            pendingIntent1 = PendingIntent.getActivity(this, 0, intent1, 0);

            try {
                builder.setContentTitle("Voice Call..")                           // required
                        .setSmallIcon(R.drawable.ic_meet_friend_logo_white) // required
                        .setContentText(map.getString("from_user_name") + " is Calling you....")  // required
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setSound(notification)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setFullScreenIntent(pendingIntent1, true)
                        .setTicker(map.getString("from_user_name") + " is Calling you....")
                        .addAction(0, "Accept", pendingIntent)
                        .addAction(0, "Reject", dismissIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Notification notifications = builder.build();
        notifManager.notify(NOTIFY_ID, notifications);
//        startForeground(12121, notifications);

    }

    public void VideoNotification(JSONObject map) {
        final int NOTIFY_ID = 1;
        NotificationManager notifManager = null;
        // There are hardcoding only for show it's just strings
        String name = "my_package_channel";
        String id = "12456234546744658"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.
        Uri notification = Uri.parse(
                "android.resource://" +
                        getApplicationContext().getPackageName() + "/" + R.raw.ringtonecall);


        Intent intent;
        Intent intent1;
        PendingIntent pendingIntent;
        PendingIntent pendingIntent1;
        NotificationCompat.Builder builder = null;
        Intent intentd = new Intent(this, ActionReceiver.class);
        intentd.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentd.putExtra("action", "1");
        try {
            intentd.putExtra("friendId", map.getString("from_user"));
            intentd.putExtra("type", map.getString("call_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PendingIntent dismissIntent = PendingIntent.getBroadcast(this, 0, intentd, PendingIntent.FLAG_UPDATE_CURRENT);

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.setShowBadge(true);
                mChannel.setSound(notification, new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build());
                mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

//                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            intent1 = new Intent(this, CallLauncherActivity.class);
            intent = new Intent(this, VideoCallActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            try {
                intent.putExtra("uId", map.getString("uid"));
                intent.putExtra("name", map.getString("from_user_name"));
                intent.putExtra("image", map.getString("from_user_image"));
                intent.putExtra("id", map.getString("from_user"));
                intent.putExtra("channelName", map.getString("channelName"));
                intent.putExtra("accessToken", map.getString("token"));
                intent.putExtra("from", "notification");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                intent1.putExtra("uId", map.getString("uid"));
                intent1.putExtra("name", map.getString("from_user_name"));
                intent1.putExtra("image", map.getString("from_user_image"));
                intent1.putExtra("id", map.getString("from_user"));
                intent1.putExtra("channelName", map.getString("channelName"));
                intent1.putExtra("type", map.getString("call_type"));
                intent1.putExtra("accessToken", map.getString("token"));
                intent1.putExtra("from", "notification");
                intent1.putExtra("call_type", "video");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            pendingIntent1 = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            long[] pattern = {500, 400, 300, 200, 400};
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(pattern, -1);
            v.cancel();

            try {
                builder.setContentTitle("Video Call...")  // required
                        .setSmallIcon(R.drawable.ic_meet_friend_logo_white) // required
                        .setContentText(map.getString("from_user_name") + " is Calling you....")  // required
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSound(notification)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setFullScreenIntent(pendingIntent1, true)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setTicker(map.getString("from_user_name") + " is Calling you....")
                        .addAction(0, "Accept", pendingIntent)
                        .addAction(0, "Reject", dismissIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            builder.build();
        } else {

            builder = new NotificationCompat.Builder(this);

            intent1 = new Intent(this, CallLauncherActivity.class);
            intent = new Intent(this, VideoCallActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            try {
                intent.putExtra("uId", map.getString("uid"));
                intent.putExtra("name", map.getString("from_user_name"));
                intent.putExtra("image", map.getString("from_user_image"));
                intent.putExtra("id", map.getString("from_user"));
                intent.putExtra("channelName", map.getString("channelName"));
                intent.putExtra("accessToken", map.getString("token"));
                intent.putExtra("from", "notification");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                intent1.putExtra("uId", map.getString("uid"));
                intent1.putExtra("name", map.getString("from_user_name"));
                intent1.putExtra("image", map.getString("from_user_image"));
                intent1.putExtra("id", map.getString("from_user"));
                intent1.putExtra("type", map.getString("call_type"));
                intent1.putExtra("channelName", map.getString("channelName"));
                intent1.putExtra("accessToken", map.getString("token"));
                intent1.putExtra("from", "notification");
                intent1.putExtra("call_type", "video");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            pendingIntent1 = PendingIntent.getActivity(this, 0, intent1, 0);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            try {
                builder.setContentTitle("Video Call..")                           // required
                        .setSmallIcon(R.drawable.ic_meet_friend_logo_white) // required
                        .setContentText(map.getString("from_user_name") + " is Calling you....")  // required
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setSound(notification)
                        .setFullScreenIntent(pendingIntent1, true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                        .setPriority(Notification.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setTicker(map.getString("from_user_name") + " is Calling you....")
                        .addAction(0, "Accept", pendingIntent)
                        .addAction(0, "Reject", dismissIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Notification notifications = builder.build();
        notifManager.notify(NOTIFY_ID, notifications);
//        startForeground(12121, notifications);
    }

    public void generateNotification(Context context, String title, String body) {
//        int id = 0;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "124562345467446589")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);
        Uri soundUri = Uri.parse(
                "android.resource://" +
                        getApplicationContext().getPackageName() + "/" + R.raw.wuwup);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("124562345467446589", "meeturfriends", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(soundUri, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            assert mNotificationManager != null;

            mBuilder.setChannelId("124562345467446589");
            mNotificationManager.createNotificationChannel(notificationChannel);
        } else {

        }

        resultIntent = new Intent(context, HomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSound(soundUri);
//        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(11, mBuilder.build());
    }
}