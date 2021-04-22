package com.meetfriend.app.utilclasses;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.meetfriend.app.network.API;
import com.meetfriend.app.network.ServerRequest;
import com.meetfriend.app.responseclasses.CommonResponseClass;

import retrofit2.Call;
import retrofit2.Response;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        String friendId = intent.getStringExtra("friendId");
        String type = intent.getStringExtra("type");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(Integer.parseInt(action));
        getSessionId(context, friendId, type);
        Log.e("intent_in","ghghghh");
    }

    public void performAction1() {
        Log.e("clear", "true");
    }

    public void performAction2() {
        Log.e("accept", "true");
    }

    private void getSessionId(Context mContext, String friendId, String type) {

        new ServerRequest<CommonResponseClass>(mContext, API.opentokSession(friendId, type, "disconnect"), true) {
            @Override
            public void onCompletion(Call<CommonResponseClass> call, Response<CommonResponseClass> response) {
                if (response.body() != null && response.code() == 200) {

                }
            }
        };
    }

}