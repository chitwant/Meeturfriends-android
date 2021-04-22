package com.meetfriend.app.utilclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.meetfriend.app.ui.activities.AudioCallActivity;
import com.meetfriend.app.ui.activities.VideoCallActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MyBroadcastReceiver extends BroadcastReceiver {


    private Bundle bundle;

    @Override
    public void onReceive(Context mContext, Intent intent) {
        if (intent.hasExtra("type")) {
            Log.e("notification_Data", intent.getExtras().toString());
            JSONObject map= null;
            try {
                map = new JSONObject(intent.getExtras().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }  JSONObject map1= null;
            try {
                map1 = map.optJSONObject("extra_data");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (map1.getString("call_type").equals("video")) {
                    bundle = new Bundle();
                    bundle.putString("uId", map1.getString("uid"));
                    bundle.putString("id", map1.getString("from_user"));
                    bundle.putString("nick_name", map1.getString("from_user_name"));
                    bundle.putString("from", "notification");
                    bundle.putString("image", "");
                    bundle.putString("token", map1.getString("accessToken"));
                    bundle.putString("channelName", map1.getString("channelName"));
                    Util.intentToActivityWithBundle(mContext, VideoCallActivity.class, bundle, false);
                } else if (map1.getString("call_type").equals("audio")) {
                    bundle = new Bundle();
                    bundle.putString("uId", map1.getString("uid"));
                    bundle.putString("id", map1.getString("from_user"));
                    bundle.putString("nick_name", map1.getString("from_user_name"));
                    bundle.putString("from", "notification");
                    bundle.putString("image", "");
                    bundle.putString("token", map1.getString("accessToken"));
                    bundle.putString("channelName", map1.getString("channelName"));
                    Util.intentToActivityWithBundle(mContext, AudioCallActivity.class, bundle, false);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}