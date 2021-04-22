package com.meetfriend.app.ui.fragments.profile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.FragmentChatBinding;
import com.meetfriend.app.responseclasses.ChatPOJO;
import com.meetfriend.app.responseclasses.viewprofile.Result;
import com.meetfriend.app.ui.adapters.ChatAdapter;
import com.meetfriend.app.utilclasses.UtilsClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import contractorssmart.app.utilsclasses.PreferenceHandler;

public class ChatFragment extends Fragment {
    public String CHAT_TEXT = "1";
    public String FIREBASE_NODE = "chat";
    public String FIREBASE_DATA = "data";
    public String FIREBASE_USERS = "users";
    public String FIREBASE_USER_STATUS = "user_status";
    public String FIREBASE_MESSAGES = "messages";
    public String FIREBASE_LASTMSG = "lastMSG";
    public String FIREBASE_LASTMSG_TIME = "lastmsgTIME";
    public String NODE_STATUS = "status";
    public String NODE_LAST_SEEN = "last_seen";
    FragmentChatBinding binding;
    Context mContext;
    ChatAdapter adapter;
    String mainNode = "", userStatus = "", userLastSeen = "";
    DatabaseReference fireMessages, fireUserStatus, fireUserLastSeen;
    ValueEventListener fireMessagesListener, fireUserStatusListener, fireUserLastSeenListener;
    List<ChatPOJO.Message> chatList = new ArrayList<>();
    Result result;
    private String other_id = "", user_id = "", otherUserName = "", userName = "", userImage = "", otherUserImage = "";

    public ChatFragment(String otherUserId, Result result) {
        other_id = otherUserId;
        this.result = result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_chat, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        initViews();
        addUser();
        addFirebaseListener();
    }

    private void initViews() {
        Toast.makeText(mContext, result.getFirstName(), Toast.LENGTH_SHORT).show();
        user_id = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
        userName = PreferenceHandler.INSTANCE.readString(mContext, "FIRSTNAME", "") + " " + PreferenceHandler.INSTANCE.readString(mContext, "LASTNAME", "");
        userImage = PreferenceHandler.INSTANCE.readString(mContext, "PROFILE_PHOTO", "");
        mainNode = user_id + "_" + other_id;
        otherUserName=result.getFirstName()+" "+result.getLastName();
        otherUserImage="https://meeturfriends.s3.amazonaws.com"+result.getProfile_photo();
        binding.ivSend.setOnClickListener(v -> {
//            if (Utils.isNetworkConnected(mContext)) {
            if (!binding.etMessage.getText().toString().isEmpty()) {
                sendMessages();
            }
//            } else {
//                Utils.showMessageDialog(mContext, getString(R.string.no_internet_message), null);
//            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setItemPrefetchEnabled(false);
        binding.rvChat.setLayoutManager(manager);
        adapter = new ChatAdapter(mContext, chatList,"","");
        binding.rvChat.setAdapter(adapter);
        binding.rvChat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                final int lastAdapterItem = adapter.getItemCount() - 1;
                binding.rvChat.post(() -> {
                    int recyclerViewPositionOffset = -1000000;
                    View bottomView = manager.findViewByPosition(lastAdapterItem);
                    if (bottomView != null) recyclerViewPositionOffset = 0 - bottomView.getHeight();
                    manager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
                });
            }
        });
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
        binding.rlOuter.setVisibility(View.VISIBLE);
//            dialog.show();
        fireMessages = FirebaseDatabase.getInstance().getReference().child(FIREBASE_NODE)
                .child(mainNode).child(FIREBASE_MESSAGES);
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
                String seenText = getString(R.string.last_seen) + " : " + UtilsClass.getDateTimeFromTimeStamp(userLastSeen);
//                binding.tvStatus.setText(userStatus.equals(getString(R.string.online)) ? userStatus : seenText);
//                binding.tvStatus.setVisibility(View.VISIBLE);
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

    public void sendMessages() {
        DatabaseReference message_root = fireMessages.child(Objects.requireNonNull(fireMessages.push().getKey()));
        HashMap<String, Object> map = new HashMap<>();
        map.put("fromID", user_id);
        map.put("toID", other_id);
        map.put("msgTEXT", binding.etMessage.getText().toString());
        map.put("msgTIME", ServerValue.TIMESTAMP);
        map.put("msgTYPE", CHAT_TEXT);
        message_root.updateChildren(map);

        DatabaseReference fireTable = FirebaseDatabase.getInstance().getReference().child(FIREBASE_NODE).child(mainNode).child(FIREBASE_DATA);
        fireTable.child(FIREBASE_LASTMSG).setValue(binding.etMessage.getText().toString());
        fireTable.child(FIREBASE_LASTMSG_TIME).setValue(ServerValue.TIMESTAMP);


        binding.etMessage.setText("");
    }

    public void setChatList(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            chatList.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                ChatPOJO.Message item = data.getValue(ChatPOJO.Message.class);
                chatList.add(item);
            }
            adapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> binding.rvChat.scrollToPosition(chatList.size() - 1), 100);
        }
    }

}