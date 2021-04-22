package com.meetfriend.app.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityChatBinding;
import com.meetfriend.app.databinding.ItemLeftChatBinding;
import com.meetfriend.app.databinding.ItemRightChatBinding;
import com.meetfriend.app.network.API;
import com.meetfriend.app.network.ProgressRequestBody;
import com.meetfriend.app.network.ServerRequest;
import com.meetfriend.app.responseclasses.ChatPOJO;
import com.meetfriend.app.responseclasses.CommonDataModel;
import com.meetfriend.app.responseclasses.CommonResponseClass;
import com.meetfriend.app.responseclasses.MyEarningDataModel;
import com.meetfriend.app.responseclasses.blocked.BlockedUsersResponseClass;
import com.meetfriend.app.responseclasses.friends.Data;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.Util;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.GiftViewModel;
import com.meetfriend.app.viewmodal.HomeViewModal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;
import retrofit2.Call;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int VIDEO_REQUEST_CODE = 10001;
    private static final int NOTIFICATION_ID = 101;
    public static String outputFile;
    public static MediaPlayer mediaPlayer;
    static Context mContext;
    static ActivityChatBinding binding;
    private static DownloadManager downloadManager;
    final String MEDIA_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "";
    private final String CHANNEL_ID = "11111111";
    public String CHAT_TEXT = "1";
    public String CHAT_AUDIO = "2";
    public String CHAT_VIDEO = "3";
    public String CHAT_IMAGE = "4";
    public String CHAT_GIFT = "5";
    public String CHAT_GIFT_REQUEST = "6";
    public String CHAT_GIFT_ACCEPT = "7";
    public String CHAT_GIFT_REJECTED = "8";
    public String FIREBASE_NODE = "chat";
    public String FIREBASE_DATA = "data";
    public String FIREBASE_USERS = "users";
    public String FIREBASE_USER_STATUS = "user_status";
    public String FIREBASE_MESSAGES = "messages";
    public String FIREBASE_LASTMSG = "lastMSG";
    public String FIREBASE_LASTMSG_TIME = "lastmsgTIME";
    public String NODE_STATUS = "status";
    public String NODE_LAST_SEEN = "last_seen";
    public String msg = "";
    public boolean isTyping = false;
    Handler handler;
    GiftViewModel giftViewModel;

    ChatAdapter adapter;
    String mainNode = "", userStatus = "", userLastSeen = "";
    DatabaseReference fireMessages, fireMessages_Other, fireUserStatus, fireUserLastSeen;
    ValueEventListener fireMessagesListener, fireUserStatusListener, fireUserLastSeenListener;
    List<ChatPOJO.Message> chatList = new ArrayList<>();
    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.e("IN", "" + referenceId);
//            list.remove(referenceId);
//            if (list.isEmpty()) {
            Log.e("INSIDE", "" + referenceId);
            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Download Completed");
            /*NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "MeetUrFriends",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Download Completed");
                channel.setSound(null, null);
                channel.setShowBadge(false);
                mNotificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
            notificationBuilder.setColor(getResources().getColor(R.color.colorAccent))
                    // Set the large and small icons
                    .setSmallIcon(R.mipmap.ic_launcher)
                    // Set Notification content information
                    .setContentText("Download Completed")
                    // Add playback actions
                    .setChannelId(CHANNEL_ID);
            notificationBuilder.setOngoing(false);
            mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());*/

        }
    };
    int position;
    HomeViewModal homeViewModal;
    private boolean isRecord = false;
    private boolean isVisible = false;
    private boolean isPlaying = false;
    private boolean isRecorded = false;
    private MediaRecorder myAudioRecorder;
    private int seconds = 0;
    // Is the stopwatch running?
    private boolean running;
    private Runnable myRunnable;
    private String other_id = "", user_id = "", otherUserName = "", userName = "", userImage = "", otherUserImage = "", key = "";
    private String filePath = "";
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private String filetype = CHAT_TEXT;
    private String giftId = "";
    private Double totalCoins = 0.0;
    private boolean isEdit = false;
    private ChatPOJO.Message editmessageData;

    public static void downloadPermission(String url) {
        new AlertDialog.Builder(mContext)
                .setTitle("")
                .setMessage("Do you want to download this media?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        download(url);
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void download(String url) {
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        String name = getFileNameFromURL(url);
        request.setTitle("MeetUrFriends Downloading");
        request.setDescription(name);
        request.setVisibleInDownloadsUi(false);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/MeetUrFriends/" + name);
        downloadManager.enqueue(request);
    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    public void delete(String key, String from) {
        new AlertDialog.Builder(mContext)
                .setTitle("")
                .setMessage("Delete Message?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (from.equalsIgnoreCase("my")) {
                            fireMessages.child(key).removeValue();
//                            fireMessages_Other.child(key).removeValue();
                        } else {
                            fireMessages.child(key).removeValue();
                            fireMessages_Other.child(key).removeValue();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void updateRequest(String key, String from, int position) {
        this.key = key;
        this.position = position;
        DatabaseReference fireTable = FirebaseDatabase.getInstance().getReference().child(FIREBASE_NODE).child(mainNode).child(FIREBASE_DATA).child(FIREBASE_MESSAGES + "_" + user_id);
        ChatPOJO item;
        item = new ChatPOJO(user_id, other_id,
                userName, otherUserName,
                userImage, otherUserImage);

        fireTable.setValue(item);
        String title = "Want to reject the gift request?";
        if (from.equalsIgnoreCase(CHAT_GIFT_ACCEPT)) {
            title = "Want to send this gift?";
        }
        new AlertDialog.Builder(mContext)
                .setTitle("")
                .setMessage(title)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (from.equalsIgnoreCase(CHAT_GIFT_ACCEPT)) {

                            if (totalCoins >= Double.parseDouble(chatList.get(position).msgTEXT)) {
                                CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
                                HashMap<String, Object> mHashMap = new HashMap<>();
                                mHashMap.put("to_id", other_id);
                                mHashMap.put("post_id", "");
                                mHashMap.put("coins", chatList.get(position).msgTEXT);
                                mHashMap.put("gift_id", chatList.get(position).gift_id);
                                giftViewModel.giftSent(mHashMap);
                                sendNoti("Accept your gift request", "", "chat");
                            } else {
                                CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "You don't have enough coins to send this gift.");
                                startActivity(new Intent(mContext, CoinPlanPriceActivity.class));
                            }
                        } else {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("file", chatList.get(position).file);
                            map.put("fromID", chatList.get(position).fromID);
                            map.put("gift_id", chatList.get(position).gift_id);
                            map.put("id", chatList.get(position).id);
                            map.put("msgTYPE", CHAT_GIFT_REJECTED);
                            map.put("msgTIME", chatList.get(position).msgTIME);
                            map.put("msgTEXT", chatList.get(position).msgTEXT);
                            map.put("toID", chatList.get(position).toID);
                            map.put("isSave", chatList.get(position).isSave);
                            map.put("isRead", chatList.get(position).isRead);
                            map.put("senderMsgId", chatList.get(position).senderMsgId);
                            fireMessages.child(key).updateChildren(map);
                            map = new HashMap<>();
                            map.put("file", chatList.get(position).file);
                            map.put("fromID", chatList.get(position).fromID);
                            map.put("gift_id", chatList.get(position).gift_id);
                            map.put("id", chatList.get(position).id);
                            map.put("msgTYPE", CHAT_GIFT_REJECTED);
                            map.put("msgTIME", chatList.get(position).msgTIME);
                            map.put("msgTEXT", chatList.get(position).msgTEXT);
                            map.put("toID", chatList.get(position).toID);
                            map.put("isSave", chatList.get(position).isSave);
                            map.put("isRead", chatList.get(position).isRead);
                            map.put("senderMsgId", key);
                            fireMessages_Other.child(chatList.get(position).senderMsgId).updateChildren(map);
                            sendNoti("Reject your gift request", "", "chat");
                        }
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        mContext = this;
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        homeViewModal = new ViewModelProvider(this).get(HomeViewModal.class);

        initViews();
        initialObservers();
        getFriendsListApi();
    }

    private void getFriendsListApi() {
        HashMap<String, Object> mHashMap = new HashMap<>();
        mHashMap.put("page", "1");
        mHashMap.put("per_page", 1000);
        homeViewModal.blockedList(mHashMap);
    }

    private void initialObservers() {
        final Observer<BlockedUsersResponseClass> favsObserver = new Observer<BlockedUsersResponseClass>() {
            @Override
            public void onChanged(BlockedUsersResponseClass response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();
                if (response.getStatus() == true) {
                    ArrayList<Data> blocklist = response.getResult().getData();
                    if (blocklist.size() > 0)
                        for (int i = 0; i < blocklist.size(); i++) {
                            if (blocklist.get(i).getFriend_id() == Integer.parseInt(other_id)) {
                                CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "You can not continue chat with blocked user");
                                finish();
                            } else if (blocklist.get(i).getFriend_id() == Integer.parseInt(user_id)) {
                                CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "You can not continue chat with blocked user");
                                finish();
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getFriendsListApi();
                                    }
                                }, 5000);
                            }
                        }
                    else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFriendsListApi();
                            }
                        }, 2000);
                    }

                } else {
                }
            }
        };
        homeViewModal.getBlockedListResponse().observe(this, favsObserver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getMyEarning();

        PreferenceHandler.INSTANCE.writeString(getApplicationContext(), "user_id", other_id);

        UtilsClass.updateUserStatus(mContext, true);
        if (filetype == CHAT_GIFT) {
            if (PreferenceHandler.INSTANCE.readString(mContext, "isSend", "").equalsIgnoreCase("1")) {
                String coin = PreferenceHandler.INSTANCE.readString(mContext, "coin", "");
                filePath = PreferenceHandler.INSTANCE.readString(mContext, "filePath", "");
                sendMessages(coin);
                PreferenceHandler.INSTANCE.writeString(mContext, "isSend", "0");
            } else {
                filetype = CHAT_TEXT;
            }
        } else if (filetype == CHAT_GIFT_REQUEST) {
            if (PreferenceHandler.INSTANCE.readString(mContext, "isSend", "").equalsIgnoreCase("1")) {
                String coin = PreferenceHandler.INSTANCE.readString(mContext, "coin", "");
                filePath = PreferenceHandler.INSTANCE.readString(mContext, "filePath", "");
                giftId = PreferenceHandler.INSTANCE.readString(mContext, "coin_id", "");
                CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Gift requested successfully.");
                sendMessages(coin);
                PreferenceHandler.INSTANCE.writeString(mContext, "isSend", "0");
            } else {
                filetype = CHAT_TEXT;
            }
        }
    }

    private void getMyEarning() {
        giftViewModel.myearning();
//        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);

    }

    private void myEarninginitializeObservers() {
        final Observer<MyEarningDataModel> favsObserver = new Observer<MyEarningDataModel>() {
            @Override
            public void onChanged(MyEarningDataModel response) {
//                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    totalCoins = response.getResult().getTotalPurchasedCoins();
                }
            }
        };
        giftViewModel.getMyEarningResponse().observe(this, favsObserver);
    }

    private void giftSendinitializeObservers() {
        final Observer<CommonResponseClass> favsObserver = new Observer<CommonResponseClass>() {
            @Override
            public void onChanged(CommonResponseClass response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();
                if (response.getStatus() == true) {
                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, response.getMessage());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("file", chatList.get(position).file);
                    map.put("fromID", chatList.get(position).fromID);
                    map.put("gift_id", chatList.get(position).gift_id);
                    map.put("id", chatList.get(position).id);
                    map.put("msgTYPE", CHAT_GIFT_ACCEPT);
                    map.put("msgTIME", chatList.get(position).msgTIME);
                    map.put("msgTEXT", chatList.get(position).msgTEXT);
                    map.put("toID", chatList.get(position).toID);
                    map.put("isSave", chatList.get(position).isSave);
                    map.put("isRead", chatList.get(position).isRead);
                    map.put("senderMsgId", chatList.get(position).senderMsgId);
                    fireMessages.child(key).updateChildren(map);
                    map = new HashMap<>();
                    map.put("file", chatList.get(position).file);
                    map.put("fromID", chatList.get(position).fromID);
                    map.put("gift_id", chatList.get(position).gift_id);
                    map.put("id", chatList.get(position).id);
                    map.put("msgTYPE", CHAT_GIFT_ACCEPT);
                    map.put("msgTIME", chatList.get(position).msgTIME);
                    map.put("msgTEXT", chatList.get(position).msgTEXT);
                    map.put("toID", chatList.get(position).toID);
                    map.put("isSave", chatList.get(position).isSave);
                    map.put("isRead", chatList.get(position).isRead);
                    map.put("senderMsgId", key);
                    fireMessages_Other.child(chatList.get(position).senderMsgId).updateChildren(map);
//                    if (filetype.equalsIgnoreCase(CHAT_GIFT))
//                        sendNoti("Send you a Gift", chatList.get(position).gift_id, CHAT_GIFT);
                }
            }
        };
        giftViewModel.getGiftSendResponse().observe(this, favsObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mediaPlayer.release();
                mediaPlayer = null;
                binding.rvChat.getAdapter().notifyDataSetChanged();
            }
        }
    }

    private void initViews() {
        giftViewModel = new ViewModelProvider(this).get(GiftViewModel.class);
        other_id = getIntent().getStringExtra("user_id");
        otherUserImage = getIntent().getStringExtra("image");
        otherUserName = getIntent().getStringExtra("name");
        msg = getIntent().getStringExtra("msg");
        filetype = getIntent().getStringExtra("type");
        user_id = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
        userName = PreferenceHandler.INSTANCE.readString(mContext, "FIRSTNAME", "") + " " + PreferenceHandler.INSTANCE.readString(mContext, "LASTNAME", "");
        userImage = PreferenceHandler.INSTANCE.readString(mContext, "PROFILE_PHOTO", "");
        myEarninginitializeObservers();
        giftSendinitializeObservers();

        if (user_id != null) {
            if (other_id != null) {
                if (Integer.parseInt(user_id) < Integer.parseInt(other_id))
                    mainNode = user_id + "_" + other_id;
                else
                    mainNode = other_id + "_" + user_id;
                binding.tvName.setText(otherUserName);
                CommonMethods.INSTANCE.setUserImage(binding.mUserImage, otherUserImage);
                binding.etMessage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        filetype = CHAT_TEXT;
                        if (binding.etMessage.getText().toString().trim().equalsIgnoreCase("")) {
                            binding.ivSend.setVisibility(View.GONE);
                            binding.mMediaLayout.setVisibility(View.VISIBLE);
                            isEdit = false;
                        } else {
                            binding.ivSend.setVisibility(View.VISIBLE);
                            binding.mMediaLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                binding.ivSend.setOnClickListener(v -> {
//            if (Utils.isNetworkConnected(mContext)) {
                    if (filetype.equalsIgnoreCase(CHAT_TEXT)) {
                        if (isEdit) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("file", editmessageData.file);
                            map.put("fromID", editmessageData.fromID);
                            map.put("gift_id", editmessageData.gift_id);
                            map.put("id", editmessageData.id);
                            map.put("msgTYPE", editmessageData.msgTYPE);
                            map.put("msgTIME", editmessageData.msgTIME);
                            map.put("msgTEXT", binding.etMessage.getText().toString());
                            map.put("toID", editmessageData.toID);
                            map.put("isSave", editmessageData.isSave);
                            map.put("isRead", editmessageData.isRead);
                            map.put("senderMsgId", editmessageData.senderMsgId);
                            fireMessages.child(editmessageData.id).updateChildren(map);
                            binding.etMessage.setText("");
                        } else if (!binding.etMessage.getText().toString().trim().isEmpty()) {
                            binding.ivSend.setVisibility(View.GONE);
                            binding.mMediaLayout.setVisibility(View.VISIBLE);
                            sendMessages(binding.etMessage.getText().toString().trim());
                        }
                    } else if (filetype.equalsIgnoreCase(CHAT_AUDIO)) {
                        isVisible = false;
                        binding.mRecordView.setVisibility(View.GONE);
                        binding.ivSend.setVisibility(View.GONE);
                        binding.mMediaLayout.setVisibility(View.VISIBLE);
                        binding.etMessage.setVisibility(View.VISIBLE);

                        isRecord = false;
                        if (myAudioRecorder != null)
                            myAudioRecorder.release();
                        if (handler != null)
                            handler.removeCallbacks(myRunnable);
                        seconds = 0;
//                        filePath = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
                        filePath = outputFile;
                        uploadVideo(filetype);

                    }
//            } else {
//                Utils.showMessageDialog(mContext, getString(R.string.no_internet_message), null);
//            }
                });
                binding.ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isVisible) {
                            isVisible = false;
                            binding.mRecordView.setVisibility(View.GONE);
                            binding.ivSend.setVisibility(View.GONE);
                            binding.mMediaLayout.setVisibility(View.VISIBLE);
                            binding.etMessage.setVisibility(View.VISIBLE);

                            isRecord = false;
                            if (myAudioRecorder != null)
                                myAudioRecorder.release();
                            if (handler != null)
                                handler.removeCallbacks(myRunnable);
                            binding.mTime.setText("00:00");
                            seconds = 0;
                            filetype = CHAT_TEXT;
                        }
                        finish();
                    }
                });
                binding.mVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, VideoCallActivity.class).putExtra("from", "call")
                                .putExtra("id", other_id)
                                .putExtra("name", otherUserName)
                                .putExtra("image", otherUserImage));
                    }
                });
                binding.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isVisible = false;
                        binding.mRecordView.setVisibility(View.GONE);
                        binding.ivSend.setVisibility(View.GONE);
                        binding.mMediaLayout.setVisibility(View.VISIBLE);
                        binding.etMessage.setVisibility(View.VISIBLE);

                        isRecord = false;
                        if (myAudioRecorder != null)
                            myAudioRecorder.release();
                        if (handler != null)
                            handler.removeCallbacks(myRunnable);
                        binding.mTime.setText("00:00");
                        seconds = 0;
                        filetype = CHAT_TEXT;
                    }
                });
                binding.mMic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callAudioPerMission();
                    }
                });
                binding.mGift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filetype = CHAT_GIFT;
                        PreferenceHandler.INSTANCE.writeString(mContext, "isSend", "0");
                        Intent intent = new Intent(mContext, GiftGalleryActivity.class);
                        intent.putExtra("from", "chat");
                        intent.putExtra("post_id", "");
                        intent.putExtra("to_id", other_id);
                        startActivity(intent);
                    }
                });
                binding.mAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, AudioCallActivity.class)
                                .putExtra("from", "call")
                                .putExtra("id", other_id)
                                .putExtra("name", otherUserName)
                                .putExtra("image", otherUserImage)
                        );
                    }
                });
                binding.mAttachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FilePickerBuilder.getInstance()
                                .setMaxCount(1)
                                .enableVideoPicker(true)
                                .enableImagePicker(true)
                                .setActivityTheme(R.style.LibAppTheme)
                                .enableCameraSupport(true)
                                .pickPhoto(ChatActivity.this, 101);
                    }
                });
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setItemPrefetchEnabled(false);
                binding.rvChat.setLayoutManager(manager);
                adapter = new ChatAdapter(mContext, chatList, userImage, otherUserImage);
                binding.rvChat.setAdapter(adapter);
                binding.rvChat.hasFixedSize();
                binding.rvChat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                    if (bottom < oldBottom) {
                        final int lastAdapterItem = adapter.getItemCount() - 1;
                        binding.rvChat.post(() -> {
                            int recyclerViewPositionOffset = -10000;
                            View bottomView = manager.findViewByPosition(lastAdapterItem);
                            if (bottomView != null)
                                recyclerViewPositionOffset = 0 - bottomView.getHeight();
                            manager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
                        });
                    }
                });
                addUser();
                addFirebaseListener();
            }
        }
        binding.mGiftRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filetype = CHAT_GIFT_REQUEST;
                PreferenceHandler.INSTANCE.writeString(mContext, "isSend", "0");
                Intent intent = new Intent(mContext, GiftGalleryActivity.class);
                intent.putExtra("from", "request");
                intent.putExtra("post_id", "");
                intent.putExtra("to_id", other_id);
                startActivity(intent);
            }
        });
        if (!msg.equalsIgnoreCase("")) {
            if (filetype.equalsIgnoreCase(CHAT_TEXT)) {
                sendMessages(msg);
            } else if (filetype.equalsIgnoreCase(CHAT_VIDEO)) {
                filePath = msg;
                sendMessages("Video");
            } else if (filetype.equalsIgnoreCase(CHAT_AUDIO)) {
                filePath = msg;
                sendMessages("Audio");
            } else if (filetype.equalsIgnoreCase(CHAT_IMAGE)) {
                filePath = msg;
                sendMessages("Image");
            }
        }
    }

    private void callAudioPerMission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
            else {
                recordAudio();
            }
        } else {
            recordAudio();

        }
    }

    @Override
    public void onBackPressed() {
        if (isVisible) {
            isVisible = false;
            binding.mRecordView.setVisibility(View.GONE);
            binding.ivSend.setVisibility(View.GONE);
            binding.mMediaLayout.setVisibility(View.VISIBLE);
            binding.etMessage.setVisibility(View.VISIBLE);

            isRecord = false;
            if (myAudioRecorder != null)
                myAudioRecorder.release();
            if (handler != null)
                handler.removeCallbacks(myRunnable);
            binding.mTime.setText("00:00");
            seconds = 0;
            filetype = CHAT_TEXT;
        }
        super.onBackPressed();
    }

    private void recordAudio() {
        binding.mTime.setText("00:00");
        filetype = CHAT_AUDIO;
        isRecorded = false;
        isPlaying = false;
        isRecord = false;
        if (isVisible) {
            isVisible = false;
            binding.mRecordView.setVisibility(View.GONE);
            binding.ivSend.setVisibility(View.GONE);
            binding.etMessage.setVisibility(View.VISIBLE);
            binding.mMediaLayout.setVisibility(View.VISIBLE);
        } else {
            isVisible = true;
            binding.mRecordView.setVisibility(View.VISIBLE);
            binding.ivSend.setVisibility(View.VISIBLE);
            binding.etMessage.setVisibility(View.GONE);
            binding.mMediaLayout.setVisibility(View.GONE);
            record();
        }
    }

    private void record() {
        if (isRecord == false) {
            initRecorder();
            try {
                isRecord = true;
            } catch (Exception ise) {
                // make something ...
                isRecord = false;
                Toast.makeText(mContext, "MIC Exception " + ise.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
            isRecord = false;
        }
    }

    private void initRecorder() {
        isRecorded = false;
        outputFile = MEDIA_PATH + "/" + "meeturFriends_recording.m4p";
        File ff = new File(outputFile);
        if (ff.exists()) {
            ff.delete();
        }
        if (isRecorded) {
        }
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myAudioRecorder.setOutputFile(outputFile);
        try {
            myAudioRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("mic", e.getMessage());
        }
        try {
            myAudioRecorder.start();
        } catch (Exception e) {
        }
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

    public void addUser() {
        DatabaseReference fireTable = FirebaseDatabase.getInstance().getReference().child(FIREBASE_NODE).child(mainNode).child(FIREBASE_DATA).child(FIREBASE_USERS);
        ChatPOJO item;
        item = new ChatPOJO(user_id, other_id,
                userName, otherUserName,
                userImage, otherUserImage);

        fireTable.setValue(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFirebaseListener();
    }

    public void addFirebaseListener() {
//        binding.rlOuter.setVisibility(View.VISIBLE);
//            dialog.show();
        fireMessages = FirebaseDatabase.getInstance().getReference().child(FIREBASE_NODE)
                .child(mainNode).child(FIREBASE_MESSAGES + "_" + user_id);
        fireMessages_Other = FirebaseDatabase.getInstance().getReference().child(FIREBASE_NODE)
                .child(mainNode).child(FIREBASE_MESSAGES + "_" + other_id);
        fireMessagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dialog.isShowing())
//                        dialog.dismiss();
                setChatList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Utils.showLog("In messages cancelled");
//                    if (dialog.isShowing())
//                        dialog.dismiss();
            }
        };
        fireMessages.addValueEventListener(fireMessagesListener);

        fireUserStatus = FirebaseDatabase.getInstance().getReference().child(FIREBASE_USER_STATUS)
                .child(other_id).child(NODE_STATUS);
        fireUserStatusListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    boolean status = (boolean) dataSnapshot.getValue();
                    userStatus = status ? getString(R.string.online) : getString(R.string.offline);
                    setUserStatus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        fireUserStatus.addValueEventListener(fireUserStatusListener);

        fireUserLastSeen = FirebaseDatabase.getInstance().getReference().child(FIREBASE_USER_STATUS)
                .child(other_id).child(NODE_LAST_SEEN);
        fireUserLastSeenListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    userLastSeen = "" + (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        fireUserLastSeen.addValueEventListener(fireUserLastSeenListener);
    }

    public void setUserStatus() {
        new Handler().postDelayed(() -> {
            if (!userLastSeen.isEmpty()) {
                String seenText = getString(R.string.last_seen) + " : " + UtilsClass.getPassedTimeString(mContext, userLastSeen);
                binding.tvStatus.setText(userStatus.equals(getString(R.string.online)) ? userStatus : seenText);
                binding.tvStatus.setVisibility(View.VISIBLE);
                UtilsClass.animateView(binding.root);
            }
        }, 2000);
    }

    public void removeFirebaseListener() {
        if (fireMessages != null && fireMessagesListener != null)
            fireMessages.removeEventListener(fireMessagesListener);
        if (fireUserStatus != null && fireUserStatusListener != null)
            fireUserStatus.removeEventListener(fireUserStatusListener);
        if (fireUserLastSeen != null && fireUserLastSeenListener != null)
            fireUserLastSeen.removeEventListener(fireUserLastSeenListener);
    }

    public void sendMessages(String mesage) {
        DatabaseReference message_root = fireMessages.child(Objects.requireNonNull(fireMessages.push().getKey()));
        DatabaseReference message_root1 = fireMessages_Other.child(Objects.requireNonNull(fireMessages.push().getKey()));
        HashMap<String, Object> map = new HashMap<>();
        map.put("fromID", user_id);
        map.put("toID", other_id);
        map.put("msgTEXT", mesage);
        map.put("msgTIME", ServerValue.TIMESTAMP);
        map.put("msgTYPE", filetype);
        map.put("file", filePath);
        map.put("gift_id", giftId);
        map.put("isSave", "0");
        map.put("isRead", "0");
        map.put("senderMsgId", message_root1.getKey());
        message_root.updateChildren(map);
        map = new HashMap<>();
        map.put("fromID", user_id);
        map.put("toID", other_id);
        map.put("msgTEXT", mesage);
        map.put("msgTIME", ServerValue.TIMESTAMP);
        map.put("msgTYPE", filetype);
        map.put("file", filePath);
        map.put("gift_id", giftId);
        map.put("isSave", "0");
        map.put("isRead", "0");
        map.put("senderMsgId", message_root.getKey());
        message_root1.updateChildren(map);
        Log.e("audio_file", filePath);

        DatabaseReference fireTable = FirebaseDatabase.getInstance().getReference().child(FIREBASE_NODE).child(mainNode).child(FIREBASE_DATA);
        fireTable.child(FIREBASE_LASTMSG).setValue(binding.etMessage.getText().toString());
        fireTable.child(FIREBASE_LASTMSG_TIME).setValue(ServerValue.TIMESTAMP);
        if (filetype.equalsIgnoreCase(CHAT_TEXT)) {
            giftId = "";
            sendNoti(binding.etMessage.getText().toString(), giftId, "chat");
            binding.etMessage.setText("");
        } else if (filetype.equalsIgnoreCase(CHAT_GIFT_REQUEST)) {
            sendNoti("Request you a Gift", giftId, "chat");
            binding.etMessage.setText("");
        } else if (filetype.equalsIgnoreCase(CHAT_AUDIO)) {
            giftId = "";
            sendNoti("Send you a audio message", giftId, "chat");
            binding.etMessage.setText("");
        } else if (filetype.equalsIgnoreCase(CHAT_IMAGE)) {
            giftId = "";
            sendNoti("Send you a Image", giftId, "chat");
            binding.etMessage.setText("");
        } else if (filetype.equalsIgnoreCase(CHAT_VIDEO)) {
            giftId = "";
            sendNoti("Send you a Video", giftId, "chat");
            binding.etMessage.setText("");
        } else if (filetype.equalsIgnoreCase(CHAT_GIFT)) {
            giftId = "";
            sendNoti("Send you a Gift", giftId, "chat");
            binding.etMessage.setText("");
        }
        filetype = CHAT_TEXT;
        filePath = "";
    }

    public void setChatList(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            chatList.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {

                ChatPOJO.Message item = data.getValue(ChatPOJO.Message.class);
                item.id = data.getKey();
                chatList.add(item);
                ReadMessage(item);
            }
            adapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> binding.rvChat.scrollToPosition(chatList.size() - 1), 100);
        }
    }

    private void ReadMessage(ChatPOJO.Message item) {
        if (item.isRead != null)
            if (item.isRead.equalsIgnoreCase("0") && !item.fromID.equalsIgnoreCase(user_id)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("file", item.file);
                map.put("fromID", item.fromID);
                map.put("gift_id", item.gift_id);
                map.put("id", item.id);
                map.put("msgTYPE", item.msgTYPE);
                map.put("msgTIME", item.msgTIME);
                map.put("msgTEXT", item.msgTEXT);
                map.put("toID", item.toID);
                map.put("isSave", item.isSave);
                map.put("isRead", "1");
                map.put("senderMsgId", item.senderMsgId);
                fireMessages_Other.child(item.senderMsgId).updateChildren(map);
            }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111 && Util.hasAllPermissionsGranted(grantResults)) {
            recordAudio();
        } else {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            ArrayList<Uri> pathVideo = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            if (checkIsImage(pathVideo.get(0))) {
                try {
                    filePath = ContentUriUtils.INSTANCE.getFilePath(mContext, pathVideo.get(0));
                    filetype = CHAT_IMAGE;
//                    filePath = "https://homepages.cae.wisc.edu/~ece533/images/airplane.png";
                    uploadVideo(filetype);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    filetype = CHAT_VIDEO;
                    filePath = ContentUriUtils.INSTANCE.getFilePath(mContext, pathVideo.get(0));
//                    filePath = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";
//                    sendMessages("video");
                    uploadVideo(filetype);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean checkIsImage(Uri uri) {
        ContentResolver contentResolver = mContext.getContentResolver();
        String type = contentResolver.getType(uri);
        if (type != null) {
            return type.startsWith("image/");
        } else {
            // try to decode as image (bounds only)
            InputStream inputStream = null;
            try {
                try {
                    inputStream = contentResolver.openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (inputStream != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream, null, options);
                    return options.outWidth > 0 && options.outHeight > 0;
                }
            } catch (Exception e) {
                // ignore
            } finally {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    FileUtils.closeQuietly(inputStream);
                }
            }
        }
        // default outcome if image not confirmed
        return false;
    }

    private void uploadVideo(String from) {
        sendNotification(0);

        CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Uploading start..");
        new ServerRequest<CommonDataModel>(mContext, API.uploadVideo(filePath, this), false) {
            @Override
            public void onCompletion(Call<CommonDataModel> call, Response<CommonDataModel> response) {
                notificationManager.cancel(01);
                if (response.body() != null && response.code() == 200) {
                    filePath = response.body().getMediaUrl() + response.body().getResult();
                    if (filetype.equalsIgnoreCase(CHAT_AUDIO))
                        sendMessages(binding.mTime.getText().toString());
                    if (filetype.equalsIgnoreCase(CHAT_IMAGE))
                        sendMessages("Image");
                    if (filetype.equalsIgnoreCase(CHAT_VIDEO))
                        sendMessages("Video");
                } else {
                    notificationManager.cancel(01);
                }
            }
        };
    }

    private void sendNoti(String from, String giftId, String type) {
        new ServerRequest<CommonDataModel>(mContext, API.sendNoti(other_id, from, giftId, type), false) {
            @Override
            public void onCompletion(Call<CommonDataModel> call, Response<CommonDataModel> response) {
                if (response.body() != null && response.code() == 200) {
                } else {
                }
            }
        };
    }

    @Override
    public void onProgressUpdate(int percentage, int totla) {
        notificationBuilder.setProgress(totla, percentage, false);
        notificationManager.notify(01, notificationBuilder.build());
    }

    private void sendNotification(int percentage) {
        notificationBuilder = new NotificationCompat.Builder(this, "01")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setContentText("Uploading File")
                .setAutoCancel(true);
        notificationBuilder.setVibrate(new long[]{0L});
        notificationBuilder.setSound(null);
        notificationBuilder.setProgress(100, percentage, false);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("01",
                    "MeetUrFriend",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);
            channel.setVibrationPattern(new long[]{0L});
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(01, notificationBuilder.build());
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceHandler.INSTANCE.writeString(getApplicationContext(), "user_id", "");

    }

    @Override
    public void onError() {
        notificationBuilder.setContentText("Upload Failed")
                // Removes the progress bar
                .setProgress(0, 0, false);
        notificationManager.notify(01, notificationBuilder.build());
    }

    @Override
    public void onFinish() {
        notificationManager.cancel(0);
    }

    public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        Handler handler = new Handler();
        private Context mContext;
        private List<ChatPOJO.Message> list;
        private String loggedInUser, otherUserImage, userImage;
        private int oldPosition = -1;
        private AppCompatSeekBar mSeekBarRight;

        public ChatAdapter(Context mContext, List<ChatPOJO.Message> list, String userImage, String otherUserImage) {
            this.mContext = mContext;
            this.list = list;
            this.otherUserImage = otherUserImage;
            this.userImage = userImage;
            loggedInUser = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
            hasStableIds();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return viewType == 0 ? new LeftHolder(ItemLeftChatBinding.inflate(LayoutInflater.from(mContext), parent, false)) :
                    new RightHolder(ItemRightChatBinding.inflate(LayoutInflater.from(mContext), parent, false));
        }

        @Override
        public void setHasStableIds(boolean hasStableIds) {
            super.setHasStableIds(hasStableIds);
            setHasStableIds(true);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (!list.get(position).fromID.equals(loggedInUser)) {
                LeftHolder h = (LeftHolder) holder;
                h.bind();
            } else {
                RightHolder h = (RightHolder) holder;
                h.bind();
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).fromID.equals(loggedInUser) ? 1 : 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private void initMediaPlayer(String from, String outputFile, ImageView mPlay, AppCompatSeekBar mSeekBar) {
            if (mediaPlayer != null) {
                mediaPlayer.reset();

//                mSeekBarRight.setMax(0);
//                mSeekBarRight.setEnabled(false);
            }
            mSeekBarRight = mSeekBar;
            handler = new Handler();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mPlay.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                }
            });

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                // Set the data source to the mediaFile location
                mediaPlayer.setDataSource(outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaPlayer.setOnPreparedListener(mp -> {
                mPlay.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));
                mediaPlayer.start();
                mSeekBarRight.setEnabled(true);
                mSeekBarRight.setMax(mediaPlayer.getDuration() / 1000);
            });
            Runnable mUpdateTimeTask = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        mSeekBarRight.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            };
            handler.postDelayed(mUpdateTimeTask, 1000);


            mSeekBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying() && fromUser) {
                            mediaPlayer.seekTo(progress * 1000);
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        private void more(ImageView optionMenu, String from, ChatPOJO.Message item, String type) {
            ArrayList list = new ArrayList();
            if (type.equalsIgnoreCase("right"))
                if (!from.equalsIgnoreCase("gift")) {
                    if (item.isSave.equalsIgnoreCase("1"))
                        list.add("Unsave");
                    else
                        list.add("Save");
                    list.add("Forward");
                    if (from.equalsIgnoreCase("text")) {
                        list.add("Copy");
                        list.add("Edit");
                    }
                } else {
                    if (item.isSave.equalsIgnoreCase("1"))
                        list.add("Unsave");
                    else
                        list.add("Save");
                }
            else {
                if (!from.equalsIgnoreCase("gift")) {
                    list.add("Forward");
                    if (from.equalsIgnoreCase("text")) {
                        list.add("Copy");
                    }
                }
            }

            final PopupMenu popup = new PopupMenu(mContext, optionMenu);
            for (int i = 0; i < list.size(); i++) {
                popup.getMenu().add(Menu.NONE, (i + 1), Menu.NONE, list.get(i).toString());
            }
            popup.setOnMenuItemClickListener(items -> {
                if (items.getTitle().equals("Edit")) {
                    isEdit = true;
                    editmessageData = item;
                    ChatActivity.binding.etMessage.setText(item.msgTEXT);
                } else if (items.getTitle().equals("Forward")) {
                    Intent intent = new Intent(mContext, SearchUserListingActivity.class);
                    intent.putExtra("from", "1");
                    if (from.equalsIgnoreCase("text")) {
                        intent.putExtra("msg", item.msgTEXT);
                    } else {
                        intent.putExtra("msg", item.file);
                    }
                    intent.putExtra("type", item.msgTYPE);

                    startActivity(intent);

                } else if (items.getTitle().equals("Copy")) {
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(item.msgTEXT);
                    Toast.makeText(mContext, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                } else if (items.getTitle().equals("Save")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("file", item.file);
                    map.put("fromID", item.fromID);
                    map.put("gift_id", item.gift_id);
                    map.put("id", item.id);
                    map.put("msgTYPE", item.msgTYPE);
                    map.put("msgTIME", item.msgTIME);
                    map.put("msgTEXT", item.msgTEXT);
                    map.put("toID", item.toID);
                    map.put("isSave", "1");
                    map.put("isRead", item.isRead);
                    map.put("senderMsgId", item.senderMsgId);
                    fireMessages.child(item.id).updateChildren(map);


                } else if (items.getTitle().equals("Unsave")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("file", item.file);
                    map.put("fromID", item.fromID);
                    map.put("gift_id", item.gift_id);
                    map.put("id", item.id);
                    map.put("msgTYPE", item.msgTYPE);
                    map.put("msgTIME", item.msgTIME);
                    map.put("msgTEXT", item.msgTEXT);
                    map.put("toID", item.toID);
                    map.put("isSave", "0");
                    map.put("isRead", item.isRead);
                    map.put("senderMsgId", item.senderMsgId);
                    fireMessages.child(item.id).updateChildren(map);
                }
                return false;
            });
            popup.show();
        }

        class LeftHolder extends RecyclerView.ViewHolder {

            ItemLeftChatBinding binding;

            LeftHolder(ItemLeftChatBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind() {
                binding.swipeRevelRight.close(true);
                ChatPOJO.Message item = list.get(getAdapterPosition());
                binding.setData(item);
                CommonMethods.INSTANCE.setUserImage(binding.mUserImageLeft, otherUserImage);
                if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT_REJECTED)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "gift", item, "left");

                        }
                    });
                    binding.mGiftReject.setVisibility(View.VISIBLE);
                    binding.mGiftLayout.setVisibility(View.VISIBLE);
                    binding.mMessageLeft.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                    binding.mRequestLayout.setVisibility(View.GONE);
                    binding.mCoin.setText(item.msgTEXT);
                    binding.mImageLeft.setVisibility(View.GONE);
                    binding.mAudioLayoutLeft.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT_ACCEPT)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "gift", item, "left");

                        }
                    });
                    binding.mGiftReject.setVisibility(View.VISIBLE);
                    binding.mGiftReject.setText("Request Accepted");
                    binding.mGiftLayout.setVisibility(View.VISIBLE);
                    binding.mMessageLeft.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                    binding.mRequestLayout.setVisibility(View.GONE);
                    binding.mCoin.setText(item.msgTEXT);
                    binding.mImageLeft.setVisibility(View.GONE);
                    binding.mAudioLayoutLeft.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "gift", item, "left");

                        }
                    });
                    binding.mGiftReject.setVisibility(View.GONE);
                    binding.mGiftLayout.setVisibility(View.VISIBLE);
                    binding.mMessageLeft.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                    binding.mRequestLayout.setVisibility(View.GONE);
                    binding.mCoin.setText(item.msgTEXT);
                    binding.mImageLeft.setVisibility(View.GONE);
                    binding.mAudioLayoutLeft.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT_REQUEST)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "gift", item, "left");

                        }
                    });
                    binding.mGiftLayout.setVisibility(View.VISIBLE);
                    binding.mRequestLayout.setVisibility(View.VISIBLE);
                    binding.mGiftReject.setVisibility(View.GONE);
                    binding.mMessageLeft.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                    binding.mCoin.setText(item.msgTEXT);
                    binding.mImageLeft.setVisibility(View.GONE);
                    binding.mAudioLayoutLeft.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_AUDIO)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "audio", item, "left");

                        }
                    });
                    binding.mGiftLayout.setVisibility(View.GONE);
                    binding.mPlayVideo.setVisibility(View.GONE);
                    binding.mMessageLeft.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                    binding.mAudioLayoutLeft.setVisibility(View.VISIBLE);
                    binding.mImageLeft.setVisibility(View.GONE);
                    binding.mAudioTimeLeft.setText(item.msgTEXT);
                    binding.mSeekBarLeft.setEnabled(false);
                    binding.mPlayLeft.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                    binding.mPlayLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (oldPosition == getAdapterPosition()) {
                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause();
                                        binding.mPlayLeft.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                                    } else {
                                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                                        mediaPlayer.start();
                                        binding.mPlayLeft.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));
                                    }
                                }
                            } else if (oldPosition != -1) {
                                notifyItemChanged(oldPosition);
                                if (mediaPlayer != null) {
                                    mediaPlayer.reset();
                                }
                                oldPosition = getAdapterPosition();
                                binding.mPlayLeft.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));
                                initMediaPlayer("left", item.file, binding.mPlayLeft, binding.mSeekBarLeft);
                            } else {
                                oldPosition = getAdapterPosition();
                                binding.mPlayLeft.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));
                                initMediaPlayer("left", item.file, binding.mPlayLeft, binding.mSeekBarLeft);

                            }

                        }
                    });
                    binding.mAudioLayoutLeft.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ChatActivity.downloadPermission(item.file);
                            return false;
                        }
                    });
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_IMAGE)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "image", item, "left");

                        }
                    });
                    binding.mGiftLayout.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImageLeft, item.file);
                    binding.mMessageLeft.setVisibility(View.GONE);
                    binding.mImageLeft.setVisibility(View.VISIBLE);
                    binding.mImageLayout.setVisibility(View.VISIBLE);
                    binding.mPlayVideo.setVisibility(View.GONE);
                    binding.mAudioLayoutLeft.setVisibility(View.GONE);
                    binding.mImageLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, FullScreenActivity.class);
                            intent.putExtra("url", item.file);
                            mContext.startActivity(intent);
                        }
                    });
                    binding.mImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ChatActivity.downloadPermission(item.file);
                            return false;
                        }
                    });
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_VIDEO)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "video", item, "left");

                        }
                    });
                    binding.mGiftLayout.setVisibility(View.GONE);
                    binding.mMessageLeft.setVisibility(View.GONE);
                    binding.mImageLeft.setVisibility(View.VISIBLE);
                    binding.mImageLayout.setVisibility(View.VISIBLE);
                    binding.mPlayVideo.setVisibility(View.VISIBLE);
                    binding.mAudioLayoutLeft.setVisibility(View.GONE);
                    Glide.with(mContext).load(item.file).placeholder(R.drawable.image_placeholder).into(binding.mImageLeft);
                    binding.mImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ChatActivity.downloadPermission(item.file);
                            return false;
                        }
                    });
                    binding.mImageLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, VideoPlayActivity.class);
                            intent.putExtra("path", item.file);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    binding.mGiftLayout.setVisibility(View.GONE);
                    binding.mMessageLeft.setVisibility(View.VISIBLE);
                    binding.mImageLeft.setVisibility(View.GONE);
                    binding.mAudioLayoutLeft.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "text", item, "left");

                        }
                    });
                }
                binding.mMessageLeft.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(binding.mMessageLeft.getText());
                        Toast.makeText(mContext, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                binding.mGiftRequestAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRequest(item.id, CHAT_GIFT_ACCEPT, getAdapterPosition());
                    }
                });
                binding.mGiftRequestReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRequest(item.id, CHAT_GIFT_REJECTED, getAdapterPosition());
                    }
                });
                binding.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(item.id, "my");
                    }
                });
            }
        }

        class RightHolder extends RecyclerView.ViewHolder {

            ItemRightChatBinding binding;

            RightHolder(ItemRightChatBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind() {
                binding.swipeRevelRight.close(true);

                ChatPOJO.Message item = list.get(getAdapterPosition());
                binding.setData(item);
                CommonMethods.INSTANCE.setUserImage(binding.mUserImageRight, userImage);
                if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT_ACCEPT)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "gift", item, "right");

                        }
                    });
                    binding.mGiftLayout.setVisibility(View.VISIBLE);
                    binding.mGiftRequestTXT.setVisibility(View.VISIBLE);
                    binding.mGiftRequestTXT.setText("Request Accepted");
                    binding.mMessageRight.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                    binding.mCoin.setText(item.msgTEXT);
                    binding.mAudioLayoutRight.setVisibility(View.GONE);
                    binding.mImageRight.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT_REJECTED)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "gift", item, "right");

                        }
                    });
                    binding.mGiftLayout.setVisibility(View.VISIBLE);
                    binding.mGiftRequestTXT.setVisibility(View.VISIBLE);
                    binding.mGiftRequestTXT.setText("Request Declined");
                    binding.mMessageRight.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                    binding.mCoin.setText(item.msgTEXT);
                    binding.mAudioLayoutRight.setVisibility(View.GONE);
                    binding.mImageRight.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT_REQUEST)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "gift", item, "right");

                        }
                    });
                    binding.mGiftLayout.setVisibility(View.VISIBLE);
                    binding.mGiftRequestTXT.setVisibility(View.VISIBLE);
                    binding.mMessageRight.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                    binding.mCoin.setText(item.msgTEXT);
                    binding.mAudioLayoutRight.setVisibility(View.GONE);
                    binding.mImageRight.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "gift", item, "right");

                        }
                    });
                    binding.mGiftLayout.setVisibility(View.VISIBLE);
                    binding.mMessageRight.setVisibility(View.GONE);
                    binding.mGiftRequestTXT.setVisibility(View.GONE);
                    CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                    binding.mCoin.setText(item.msgTEXT);
                    binding.mAudioLayoutRight.setVisibility(View.GONE);
                    binding.mImageRight.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.GONE);
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_AUDIO)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "audio", item, "right");

                        }
                    });
                    binding.mMessageRight.setVisibility(View.GONE);
                    binding.mAudioLayoutRight.setVisibility(View.VISIBLE);
                    binding.mGiftLayout.setVisibility(View.GONE);
                    binding.mImageRight.setVisibility(View.GONE);
                    binding.mAudioTimeRight.setText(item.msgTEXT);
                    binding.mImageLayout.setVisibility(View.GONE);
                    binding.mPlayRight.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                    binding.mSeekBarRight.setEnabled(false);
                    binding.mPlayRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (oldPosition == getAdapterPosition()) {
                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause();
                                        binding.mPlayRight.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                                    } else {
                                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                                        mediaPlayer.start();
                                        binding.mPlayRight.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));
                                    }
                                }
                            } else if (oldPosition != -1) {
                                notifyItemChanged(oldPosition);
                                if (mediaPlayer != null) {
                                    mediaPlayer.reset();
                                }
                                oldPosition = getAdapterPosition();
                                binding.mPlayRight.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));

                                initMediaPlayer("right", item.file, binding.mPlayRight, binding.mSeekBarRight);
                            } else {
                                oldPosition = getAdapterPosition();
                                binding.mPlayRight.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));

                                initMediaPlayer("right", item.file, binding.mPlayRight, binding.mSeekBarRight);

                            }

                        }
                    });
                    binding.mAudioLayoutRight.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ChatActivity.downloadPermission(item.file);
                            return false;
                        }
                    });
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_IMAGE)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "image", item, "right");

                        }
                    });
                    binding.mPlayVideo.setVisibility(View.GONE);
                    binding.mMessageRight.setVisibility(View.GONE);
                    binding.mImageLayout.setVisibility(View.VISIBLE);
                    binding.mImageRight.setVisibility(View.VISIBLE);
                    CommonMethods.INSTANCE.setImage(binding.mImageRight, item.file);
                    binding.mAudioLayoutRight.setVisibility(View.GONE);
                    binding.mGiftLayout.setVisibility(View.GONE);
                    binding.mImageLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, FullScreenActivity.class);
                            intent.putExtra("url", item.file);
                            mContext.startActivity(intent);
                        }
                    });
                    binding.mImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ChatActivity.downloadPermission(item.file);
                            return false;
                        }
                    });
                } else if (item.msgTYPE.equalsIgnoreCase(CHAT_VIDEO)) {
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "video", item, "right");

                        }
                    });
                    binding.mImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ChatActivity.downloadPermission(item.file);
                            return false;
                        }
                    });
                    binding.mMessageRight.setVisibility(View.GONE);
                    binding.mPlayVideo.setVisibility(View.VISIBLE);
                    binding.mImageLayout.setVisibility(View.VISIBLE);
                    binding.mImageRight.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(item.file).placeholder(R.drawable.image_placeholder).into(binding.mImageRight);
                    binding.mAudioLayoutRight.setVisibility(View.GONE);
                    binding.mGiftLayout.setVisibility(View.GONE);
                    binding.mImageLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, VideoPlayActivity.class);
                            intent.putExtra("path", item.file);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    binding.mImageLayout.setVisibility(View.GONE);
                    binding.mGiftLayout.setVisibility(View.GONE);
                    binding.mMessageRight.setVisibility(View.VISIBLE);
                    binding.mAudioLayoutRight.setVisibility(View.GONE);
                    binding.mImageRight.setVisibility(View.GONE);
                    binding.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            more(binding.more, "text", item, "right");
                        }
                    });
                }
                if (item.isRead != null)
                    if (item.isRead.equalsIgnoreCase("1")) {
                        binding.mRead.setImageDrawable(getDrawable(R.drawable.ic_baseline_double_check_24));
                    } else {
                        binding.mRead.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_24));
                    }
                else {
                    binding.mRead.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_24));
                }
                if (item.isSave != null) if (item.isSave.equalsIgnoreCase("1")) {
                    binding.mSave.setVisibility(View.VISIBLE);
                } else
                    binding.mSave.setVisibility(View.GONE);
                binding.mMessageRight.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(binding.mMessageRight.getText());
                        Toast.makeText(mContext, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                binding.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(item.id, "other");
                    }
                });
            }
        }

    }

}