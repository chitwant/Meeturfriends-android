package com.meetfriend.app.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityAudioCallBinding;
import com.meetfriend.app.network.API;
import com.meetfriend.app.network.ServerRequest;
import com.meetfriend.app.responseclasses.CommonResponseClass;

import java.util.Locale;

import contractorssmart.app.utilsclasses.CommonMethods;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import retrofit2.Call;
import retrofit2.Response;

public class AudioCallActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = AudioCallActivity.class.getSimpleName();
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    static Handler handler;
    static Context mContext;
    static ActivityAudioCallBinding binding;
    static Activity activity;
    private static Runnable myRunnable;
    private static MediaPlayer mediaPlayer;
    private static RtcEngine mRtcEngine; // Tutorial Step 1
    boolean isJoined = false;
    boolean isCall = false;
    boolean isSpeaker = false;
    int uId;
    String accessToken = "";
    private boolean isMute = false;
    private String TAG = "open_session_video";
    private String friendId = "";
    private String nick_name = "";
    private String from = "";
    private String image = "";
    private int seconds = 0;
    // Is the stopwatch running?
    private boolean running;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a goodbye message. When this message is received, the SDK determines that the user/host leaves the channel.
         *     Drop offline: When no data packet of the user or host is received for a certain period of time (20 seconds for the communication profile, and more for the live broadcast profile), the SDK assumes that the user/host drops offline. A poor network connection may lead to false detections, so we recommend using the Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who
         * leaves
         * the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        @Override
        public void onUserOffline(final int uid, final int reason) { // Tutorial Step 4
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isJoined = false;
                    onRemoteUserLeft(uid, reason);
                }
            });
        }


        /**
         * Occurs when a remote user stops/resumes sending the audio stream.
         * The SDK triggers this callback when the remote user stops or resumes sending the audio stream by calling the muteLocalAudioStream method.
         *
         * @param uid ID of the remote user.
         * @param muted Whether the remote user's audio stream is muted/unmuted:
         *
         *     true: Muted.
         *     false: Unmuted.
         */
        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) { // Tutorial Step 6
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVoiceMuted(uid, muted);
                }
            });
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
//            super.onUserJoined(uid, elapsed);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.mCall.setImageDrawable(getDrawable(R.drawable.ic_call_end));
                    binding.mCall.setBackground(getDrawable(R.drawable.red_round));
                    isJoined = true;
                    startTimer(elapsed);
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        mediaPlayer.release();
                        mediaPlayer = null;

                    }
                }
            });

        }
    };
    private String channelName = "";

    public static void disconnect(String from) {
        if (mRtcEngine != null)
            mRtcEngine.leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (handler != null)
            handler.removeCallbacks(myRunnable);
        try {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (!from.equalsIgnoreCase("my"))
                        CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "The user you have called is currently busy");
                    binding.mBack.performClick();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_call);
        mContext = this;
        activity = this;
        initViews();
    }

    private void getSessionId() {

        new ServerRequest<CommonResponseClass>(mContext, API.opentokSession(friendId, "audio", "calling"), true) {
            @Override
            public void onCompletion(Call<CommonResponseClass> call, Response<CommonResponseClass> response) {
                if (response.body() != null && response.code() == 200) {
                    isCall = true;
                    binding.mCall.setImageDrawable(getDrawable(R.drawable.ic_call_end));
                    binding.mCall.setBackground(getDrawable(R.drawable.red_round));
                    channelName = response.body().getChannelName();
                    uId = response.body().getUid();
                    accessToken = response.body().getToken();
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
                        initAgoraEngineAndJoinChannel();
                    }
//                    sendNotification(response.body().getSessiontoken(), response.body().getOpentok_token());
                    initMediaPlayer();
                }
            }
        };
    }

    private void missiedCall() {
        binding.mTime.setText("Disconnecting...");

        new ServerRequest<CommonResponseClass>(mContext, API.opentokSession(friendId, "audio", "missed"), true) {
            @Override
            public void onCompletion(Call<CommonResponseClass> call, Response<CommonResponseClass> response) {
                if (response.body() != null && response.code() == 200) {
                    disconnect("my");
                }
            }
        };
    }

    private void initMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        mediaPlayer = new MediaPlayer();


        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            // Set the data source to the mediaFile location
            Uri myUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.ringtone);
            System.out.println(myUri);
            mediaPlayer.setDataSource(mContext, myUri);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.pause();
                mediaPlayer.release();
                mediaPlayer = null;
                missiedCall();
            }
        });

        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.start();

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        from = getIntent().getStringExtra("from");
        friendId = getIntent().getStringExtra("id");
        image = getIntent().getStringExtra("image");
        nick_name = getIntent().getStringExtra("name");
        if (from.equals("call")) {
            getSessionId();
        } else if (from.equals("notification")) {
            uId = Integer.parseInt(getIntent().getStringExtra("uId"));
            channelName = getIntent().getStringExtra("channelName");
            accessToken = getIntent().getStringExtra("accessToken");
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
                initAgoraEngineAndJoinChannel();
            }
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
            for (StatusBarNotification notification : notifications) {
                if (notification.getId() == 1) {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(1);
//                    mSession = new Session.Builder(mContext, OpenTokConfig.API_KEY, sessionid).build();
//                    mSession.setSessionListener(AudioCallActivity.this);
//                    mSession.connect(sessionToken);
                }
                isCall = true;
                binding.mCall.setImageDrawable(getDrawable(R.drawable.ic_call_end));
                binding.mCall.setBackground(getDrawable(R.drawable.red_round));
            }
        }

        binding.mName.setText(nick_name);
        Glide.with(mContext).load(image).placeholder(R.drawable.ic_user_profile_pic_new).into(binding.mImage);
        binding.mCall.setOnClickListener(this);
        binding.mMute.setOnClickListener(this);
        binding.mSpeaker.setOnClickListener(this);
        binding.mBack.setOnClickListener(this);

    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();     // Tutorial Step 1
        joinChannel();               // Tutorial Step 2
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mCall:
                if (isJoined == false) {
                    missiedCall();
                } else {
                    if (isCall) {
                        isCall = false;
                        startActivity(new Intent(mContext, CallendedActivity.class).putExtra("name", nick_name).putExtra("image", image).putExtra("time", binding.mTime.getText().toString()).putExtra("id", friendId).putExtra("from", "call"));
                        finish();
//                    binding.mCall.setBackground(getDrawable(R.drawable.purple_round));
//                    binding.mCall.setImageDrawable(getDrawable(R.drawable.ic_cut_call));
                    } else {
                        isCall = true;
                        binding.mCall.setImageDrawable(getDrawable(R.drawable.ic_call_end));
                        binding.mCall.setBackground(getDrawable(R.drawable.red_round));
                    }
                }

                break;
            case R.id.mBack:
                finish();
                break;
            case R.id.mMute:
                if (isMute) {
                    isMute = false;
                    binding.mMute.setBackground(getDrawable(R.drawable.round_white_drawable));
                    binding.mMute.setImageDrawable(getDrawable(R.drawable.ic_unmute));
                } else {
                    isMute = true;
                    binding.mMute.setBackground(getDrawable(R.drawable.round_app_color));
                    binding.mMute.setImageDrawable(getDrawable(R.drawable.ic_mute));
                }
                mRtcEngine.muteLocalAudioStream(isMute);

                break;
            case R.id.mSpeaker:
                if (isSpeaker) {
                    isSpeaker = false;
                    binding.mSpeaker.setBackground(getDrawable(R.drawable.round_white_drawable));
                    binding.mSpeaker.setImageDrawable(getDrawable(R.drawable.ic_speaker_mute));
                } else {
                    isSpeaker = true;
                    binding.mSpeaker.setBackground(getDrawable(R.drawable.round_app_color));
                    binding.mSpeaker.setImageDrawable(getDrawable(R.drawable.ic_speaker));
                }
                mRtcEngine.setEnableSpeakerphone(isSpeaker);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isJoined == false) {
            missiedCall();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void startTimer(int elapsed) {
        seconds = 0;
        running = true;
        handler = new Handler();
        myRunnable = new Runnable() {
            public void run() {
                //Some interesting task
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%02d:%02d",
                                minutes, secs);

                // Set the text view text.
                binding.mTime.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }
                handler.postDelayed(myRunnable, 1000);
            }

        };
        handler.postDelayed(myRunnable, 1000);
    }

    private void removeSubscriberWithStream() {
//        SubscriberContainer container = findContainerForStream(stream);
//        if (container == null) {
//            return;
//        }
//        container.container.removeView(container.subscriber.getView());
//        container.subscriber = null;
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);

        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    finish();
                }
                break;
            }
        }
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (handler != null)
            handler.removeCallbacks(myRunnable);
    }

    // Tutorial Step 1
    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e("eorrrrrrrrrr", Log.getStackTraceString(e));

            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    // Tutorial Step 2
    private void joinChannel() {
//        if (TextUtils.equals(accessToken, "") /*|| TextUtils.equals(accessToken,)*/) {
//            accessToken = null; // default, no token
//        }

        // Sets the channel profile of the Agora RtcEngine.
        // CHANNEL_PROFILE_COMMUNICATION(0): (Default) The Communication profile. Use this profile in one-on-one calls or group calls, where all users can talk freely.
        // CHANNEL_PROFILE_LIVE_BROADCASTING(1): The Live-Broadcast profile. Users in a live-broadcast channel have a role as either broadcaster or audience. A broadcaster can both send and receive streams; an audience can only receive streams.
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);

        // Allows a user to join a channel.
        Log.e("audio_call_data", channelName + " " + uId + " " + accessToken);

        mRtcEngine.joinChannel(accessToken, channelName, "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
    }

    // Tutorial Step 3
    private void leaveChannel() {
        if (mRtcEngine != null)
            mRtcEngine.leaveChannel();
    }

    // Tutorial Step 4
    private void onRemoteUserLeft(int uid, int reason) {
        startActivity(new Intent(mContext, CallendedActivity.class).putExtra("name", nick_name).putExtra("image", image).putExtra("time", binding.mTime.getText().toString()).putExtra("id", friendId).putExtra("from", "call"));
        finish();
    }

    // Tutorial Step 6
    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
        if (muted) {
            binding.mMuteText.setVisibility(View.VISIBLE);
            binding.mMuteText.setText(nick_name + " muted his call");
        } else {
            binding.mMuteText.setVisibility(View.GONE);
        }
    }
}