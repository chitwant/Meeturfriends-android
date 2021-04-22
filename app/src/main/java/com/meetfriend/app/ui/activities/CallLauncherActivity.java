package com.meetfriend.app.ui.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityCallLauncherBinding;
import com.meetfriend.app.network.API;
import com.meetfriend.app.network.ServerRequest;
import com.meetfriend.app.responseclasses.CommonResponseClass;

import contractorssmart.app.utilsclasses.PreferenceHandler;
import retrofit2.Call;
import retrofit2.Response;

public class CallLauncherActivity extends AppCompatActivity implements View.OnClickListener {
  public   static ActivityCallLauncherBinding binding;
    Context mContext;
    private String friendId, type, call_type, from, accessToken, channelName, image, name, uId;
    private Intent intent;

    public static void back() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_launcher);
        initViews();
        uId = getIntent().getStringExtra("uId");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        channelName = getIntent().getStringExtra("channelName");
        accessToken = getIntent().getStringExtra("accessToken");
        from = getIntent().getStringExtra("from");
        call_type = getIntent().getStringExtra("call_type");
        friendId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        binding.mName.setText(name);
        Glide.with(mContext).load(image).placeholder(R.drawable.ic_user_profile_pic_new).into(binding.mImage);
        if (call_type.equalsIgnoreCase("video"))
            binding.mTime.setText("MeetUrFriends Video Call..");
        PreferenceHandler.INSTANCE.writeString(mContext, "isOpen", "1");

    }

    private void initViews() {
        mContext = this;
        binding.mBack.setOnClickListener(this);
        binding.mDiscount.setOnClickListener(this);
        binding.mCall.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceHandler.INSTANCE.writeString(mContext, "isOpen", "0");

    }

    private void getSessionId(Context mContext, String friendId, String type) {

        new ServerRequest<CommonResponseClass>(mContext, API.opentokSession(friendId, type, "disconnect"), true) {
            @Override
            public void onCompletion(Call<CommonResponseClass> call, Response<CommonResponseClass> response) {
                if (response.body() != null && response.code() == 200) {
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(1);
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mCall:
                if (call_type.equalsIgnoreCase("video")) {
                    intent = new Intent(this, VideoCallActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    try {
                        intent.putExtra("uId", uId);
                        intent.putExtra("name", name);
                        intent.putExtra("image", image);
                        intent.putExtra("id", friendId);
                        intent.putExtra("channelName", channelName);
                        intent.putExtra("accessToken", accessToken);
                        intent.putExtra("from", from);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    intent = new Intent(this, AudioCallActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    try {
                        intent.putExtra("uId", uId);
                        intent.putExtra("name", name);
                        intent.putExtra("image", image);
                        intent.putExtra("id", friendId);
                        intent.putExtra("channelName", channelName);
                        intent.putExtra("accessToken", accessToken);
                        intent.putExtra("from", from);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.mDiscount:
                getSessionId(mContext, friendId, type);
                break;
            case R.id.mBack:
                finish();
                break;
        }
    }
}