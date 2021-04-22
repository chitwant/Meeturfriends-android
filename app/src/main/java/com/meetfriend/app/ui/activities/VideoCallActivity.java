package com.meetfriend.app.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityVideoCallBinding;
import com.meetfriend.app.network.API;
import com.meetfriend.app.network.ServerRequest;
import com.meetfriend.app.responseclasses.CommonResponseClass;

import java.util.Locale;

import contractorssmart.app.utilsclasses.CommonMethods;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import retrofit2.Call;
import retrofit2.Response;

public class VideoCallActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQ_ID = 22;
    // Permission WRITE_EXTERNAL_STORAGE is not mandatory
    // for Agora RTC SDK, just in case if you wanna save
    // logs to external sdcard.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    static ActivityVideoCallBinding binding;
    static Activity activity;
    static Context mContext;
    static Handler handler;
    private static Runnable myRunnable;
    private static MediaPlayer mediaPlayer;
    private static RtcEngine mRtcEngine;
    private static VideoCanvas mLocalVideo;
    private static VideoCanvas mRemoteVideo;
    boolean isSpeaker = false;
    int uId;
    String accessToken = "";
    private boolean sessionConnected = false;
    private boolean isMute = false;
    private String TAG = "open_session_video";
    private String friendId = "";
    private String nick_name = "";
    private String from = "";
    private String image = "";
    private int seconds = 0;
    // Is the stopwatch running?
    private boolean running;
    private boolean isVideoOn = true;
    private boolean mCallEnd;
    private String channelName = "";
    private boolean isJoined = false;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        /**
         * Occurs when the local user joins a specified channel.
         * The channel name assignment is based on channelName specified in the joinChannel method.
         * If the uid is not specified when joinChannel is called, the server automatically assigns a uid.
         *
         * @param channel Channel name.
         * @param uid User ID.
         * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered.
         */
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        @Override
        public void onRemoteAudioStateChanged(int uid, int state, int reason, int elapsed) {
            super.onRemoteAudioStateChanged(uid, state, reason, elapsed);
        }

        /**
         * Occurs when the first remote video frame is received and decoded.
         * This callback is triggered in either of the following scenarios:
         *
         *     The remote user joins the channel and sends the video stream.
         *     The remote user stops sending the video stream and re-sends it after 15 seconds. Possible reasons include:
         *         The remote user leaves channel.
         *         The remote user drops offline.
         *         The remote user calls the muteLocalVideoStream method.
         *         The remote user calls the disableVideo method.
         *
         * @param uid User ID of the remote user sending the video streams.
         * @param width Width (pixels) of the video stream.
         * @param height Height (pixels) of the video stream.
         * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
         */
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }

            });
        }

        @Override
        public void onUserMuteVideo(int uid, boolean muted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVoiceMuted(uid, muted);
                }
            });
        }

        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) { // Tutorial Step 6
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVoiceMuted(uid, muted);
                }
            });
        }

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a
         *     goodbye message. When this message is received, the SDK determines that the
         *     user/host leaves the channel.
         *
         *     Drop offline: When no data packet of the user or host is received for a certain
         *     period of time (20 seconds for the communication profile, and more for the live
         *     broadcast profile), the SDK assumes that the user/host drops offline. A poor
         *     network connection may lead to false detections, so we recommend using the
         *     Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who leaves the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft(uid);
                }
            });
        }
    };

    public static void disconnect(String from) {

        leaveChannel();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        try {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (!from.equalsIgnoreCase("my")) {
                        CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "The user you have called is currently busy");
                    } else {
                        removeFromParent(mLocalVideo);
                        mLocalVideo = null;
                        removeFromParent(mRemoteVideo);
                        mRemoteVideo = null;
                    }
                    binding.mBack.performClick();
                }
            });
            if (handler != null)
                handler.removeCallbacks(myRunnable);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    private static ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
        if (muted) {
            binding.mMuteText.setVisibility(View.VISIBLE);
            binding.mMuteText.setText(nick_name + " muted his call");
        } else {
            binding.mMuteText.setVisibility(View.GONE);
        }
    }

    private void setupRemoteVideo(int uid) {
        hideButtons();
        binding.mRinging.setVisibility(View.GONE);
        isJoined = true;
        ViewGroup parent = binding.remoteVideoViewContainer;
        if (parent.indexOfChild(mLocalVideo.view) > -1) {
            parent = binding.localVideoViewContainer;
        }

        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        if (mRemoteVideo != null) {
            return;
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == binding.localVideoViewContainer);
        parent.addView(view);
        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(mRemoteVideo);
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        startTimer(0);

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


    private void hideButtons() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.mButtonLayout.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void onRemoteUserLeft(int uid) {
        if (mRemoteVideo != null && mRemoteVideo.uid == uid) {
            removeFromParent(mRemoteVideo);
            // Destroys remote view
            mRemoteVideo = null;
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED

        );
        getWindow().addFlags(

                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );
        getWindow().addFlags(

                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
        getWindow().addFlags(

                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_call);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(1);
        activity = this;
        mContext = this;
        initViews();
    }

    private void getSessionId() {
        new ServerRequest<CommonResponseClass>(mContext, API.opentokSession(friendId, "video", "calling"), true) {
            @Override
            public void onCompletion(Call<CommonResponseClass> call, Response<CommonResponseClass> response) {
                if (response.body() != null && response.code() == 200) {
                    mCallEnd = false;
                    channelName = response.body().getChannelName();
                    uId = response.body().getUid();
                    accessToken = response.body().getToken();
                    if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                            checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                        initEngineAndJoinChannel();
                    }
                    binding.mCall.setImageDrawable(getDrawable(R.drawable.ic_call_end));
                    binding.mCall.setBackground(getDrawable(R.drawable.red_round));
                    initMediaPlayer();
                }
            }
        };
    }

    private void missiedCall() {

        new ServerRequest<CommonResponseClass>(mContext, API.opentokSession(friendId, "video", "missed"), true) {
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
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
            binding.mRinging.setText("Disconnecting...");
            missiedCall();
        });

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

        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.start();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        binding.mCall.setOnClickListener(this);
        binding.mChangeCam.setOnClickListener(this);
        binding.mBack.setOnClickListener(this);
        binding.mMute.setOnClickListener(this);
        binding.mVideo.setOnClickListener(this);
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
            mCallEnd = false;
            binding.mCall.setImageDrawable(getDrawable(R.drawable.ic_call_end));
            binding.mCall.setBackground(getDrawable(R.drawable.red_round));
            if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                    checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                    checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                initEngineAndJoinChannel();
            }
        }

        binding.mName.setText(nick_name);
//        Glide.with(mContext).load(Constants.IMAGE_URL + image).placeholder(R.drawable.default_image_square).into(binding.mImage);
        binding.activityVideoChatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.mButtonLayout.setVisibility(View.VISIBLE);
                hideButtons();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (isFinishing()) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mChangeCam:
                mRtcEngine.switchCamera();
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
            case R.id.mVideo:

                if (!isVideoOn) {
                    isVideoOn = true;
                    mRtcEngine.enableLocalVideo(true);

                    binding.mVideo.setBackground(getDrawable(R.drawable.round_white_drawable));
                } else {
                    isVideoOn = false;
                    mRtcEngine.enableLocalVideo(false);
                    binding.mVideo.setBackground(getDrawable(R.drawable.round_app_color));
                }
                break;
            case R.id.mCall:
                if (isJoined == false) {
                    binding.mRinging.setText("Disconnecting...");
                    missiedCall();
                } else if (mCallEnd) {
                    startCall();
                    mCallEnd = false;
                    binding.mCall.setImageDrawable(getDrawable(R.drawable.ic_call_end));
                    binding.mCall.setBackground(getDrawable(R.drawable.red_round));
                } else {
                    endCall();
                }
                break;
        }
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            initEngineAndJoinChannel();
        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        binding.localVideoViewContainer.addView(view);
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalVideo);
    }

    private void joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
//        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
//            token = null; // default, no token
//        }
        Log.e("video_call_data", channelName + " " + uId + " " + accessToken);
        mRtcEngine.joinChannel(accessToken, channelName, "Extra Optional Data", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCallEnd) {
            leaveChannel();

        }
        if (handler != null)
            handler.removeCallbacks(myRunnable);
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.
          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy();
    }

    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        removeFromParent(mLocalVideo);
        mLocalVideo = null;
        removeFromParent(mRemoteVideo);
        mRemoteVideo = null;
        leaveChannel();
        finish();
    }

    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == binding.localVideoViewContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            binding.remoteVideoViewContainer.addView(canvas.view);
        } else if (parent == binding.remoteVideoViewContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            binding.localVideoViewContainer.addView(canvas.view);
        }
    }

    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }
}