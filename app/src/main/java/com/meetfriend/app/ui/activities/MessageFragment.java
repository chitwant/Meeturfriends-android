package com.meetfriend.app.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.FragmentMessagesBinding;
import com.meetfriend.app.databinding.ItemMessagesBinding;
import com.meetfriend.app.responseclasses.InboxPOJO;
import com.meetfriend.app.responseclasses.blocked.BlockedUsersResponseClass;
import com.meetfriend.app.responseclasses.friends.Data;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.HomeViewModal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;

public class MessageFragment extends Fragment {

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
    Context mContext;
    HomeViewModal homeViewModal;
    private FragmentMessagesBinding binding;
    private DatabaseReference fireMessages;
    private ValueEventListener fireMessagesListener;
    private List<InboxPOJO.Data> chatList = new ArrayList<>();
    private MessagesAdapter adapter;
    //    private boolean isCustLogin;
    private String loggedInID;
    private ArrayList<Data> blocklist = new ArrayList<>();


    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        ini();
        return binding.getRoot();
    }

    public void ini() {
//        Toast.makeText(mContext, chatList.size()+"", Toast.LENGTH_SHORT).show();

        mContext = getActivity();
        homeViewModal = new ViewModelProvider(getActivity()).get(HomeViewModal.class);
        initialObservers();
//        isCustLogin = Const.isCustLogin(mContext);
        loggedInID = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MessagesAdapter(mContext, chatList, blocklist);
        binding.recyclerView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
            getFriendsListApi();

        });
        ((HomeActivity) mContext).ivSearchIcon.setVisibility(View.GONE);
        binding.etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(binding.etSearchUser.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.mAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchUserListingActivity.class);
                intent.putExtra("from", "1");
                intent.putExtra("msg", "");
                startActivity(intent);
            }
        });
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
                    blocklist = response.getResult().getData();
                    getInbox();
                } else {
                    getInbox();
                }
            }
        };
        homeViewModal.getBlockedListResponse().observe(getActivity(), favsObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFirebaseListener();
        ((HomeActivity) mContext).ivSearchIcon.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFriendsListApi();

    }

    private void getInbox() {
//        if (Utils.isNetworkConnected(mContext)) {
        binding.swipeRefresh.setVisibility(View.VISIBLE);
//            dialog.show();
        fireMessages = FirebaseDatabase.getInstance().getReference().child(UtilsClass.FIREBASE_NODE);
        fireMessagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setChatList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

//                    if (dialog.isShowing())
//                        dialog.dismiss();
            }
        };
        fireMessages.addValueEventListener(fireMessagesListener);
//        } else {
//            showErrorMessage(Const.ERROR_NO_INTERNET, getString(R.string.no_internet_message));
//        }
    }

    private void removeFirebaseListener() {
        if (fireMessages != null && fireMessagesListener != null)
            fireMessages.removeEventListener(fireMessagesListener);
    }

    @SuppressLint("StaticFieldLeak")
    private void setChatList(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            chatList.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                String[] key = Objects.requireNonNull(data.getKey()).split("_");
                String userID1 = key[0];
                String userID = key[1];
                String friendId = "";
                String uid = "";
                if (userID.equalsIgnoreCase(loggedInID)) {
                    uid = userID;
                    friendId = userID1;
                } else if (userID1.equalsIgnoreCase(loggedInID)) {
                    uid = userID1;
                    friendId = userID;
                }
                if (uid.equals(loggedInID)) {
                    if (data.child(FIREBASE_MESSAGES + "_" + loggedInID).getValue() != null && uid.equals(loggedInID)) {
                        InboxPOJO.Data item = data.child(FIREBASE_DATA).getValue(InboxPOJO.Data.class);
                        item.isBlock = "0";
                        item.mainNode = data.getKey();
                        chatList.add(item);
                        /* */
                    }
                }
            }
            try {
                Collections.sort(chatList, (s1, s2) -> (s2.lastmsgTIME).compareTo(s1.lastmsgTIME));
            } catch (Exception e) {
            }
        }
        if (chatList != null) {
//            if (chatList.size()>0){
//                HashSet set=new HashSet();
//                set.addAll(chatList);
//                chatList.clear();
//                chatList.addAll(set);
//            }
        }


        adapter.notifyDataSetChanged();
//        if (dialog.isShowing())
//            dialog.dismiss();
//        Toast.makeText(mContext, chatList.size()+"", Toast.LENGTH_SHORT).show();
        if (chatList != null)
            if (chatList.size() <= 0) {
                binding.mNoMessage.setVisibility(View.VISIBLE);
                binding.etSearchUser.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.etSearchUser.setVisibility(View.VISIBLE);
                binding.mNoMessage.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
    }

    public void delete(String key) {
        new AlertDialog.Builder(mContext)
                .setTitle("")
                .setMessage("Want to delete all messages?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        fireMessages.child(key).child(FIREBASE_MESSAGES + "_" + loggedInID).removeValue();
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

        List<InboxPOJO.Data> filterList;
        List<InboxPOJO.Data> temp;
        ArrayList<Data> blocklist;
        private Context mContext;
        private List<InboxPOJO.Data> list;

        public MessagesAdapter(Context mContext, List<InboxPOJO.Data> list, ArrayList<Data> blocklist) {
            this.mContext = mContext;
            this.blocklist = blocklist;
            this.list = list;
            this.temp = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemMessagesBinding itemBinding = ItemMessagesBinding.inflate(LayoutInflater.from(mContext),
                    parent, false);
            return new Holder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Holder h = (Holder) holder;
            h.bind();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence dd) {
                    list = temp;
                    String fd = dd.toString();
                    if (fd.isEmpty()) {
                        filterList = list;
                    } else {
                        List<InboxPOJO.Data> filteredList1 = new ArrayList<>();
                        for (InboxPOJO.Data row : list) {
                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.users.getName(mContext).toLowerCase().contains(fd.toLowerCase())) {
                                filteredList1.add(row);
                            }
                        }
                        filterList = filteredList1;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    list = (List<InboxPOJO.Data>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        class Holder extends RecyclerView.ViewHolder {

            ItemMessagesBinding binding;

            Holder(ItemMessagesBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            @SuppressLint("SetTextI18n")
            public void bind() {
                binding.swipeRevel.close(true);
                InboxPOJO.Data item = list.get(getAdapterPosition());
                binding.setData(item);
                binding.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(item.mainNode);
                    }
                });
                binding.llOuter.setOnClickListener(v -> {
                    if (blocklist.size() > 0)
                        for (int i = 0; i < blocklist.size(); i++) {
                            if (blocklist.get(i).getIsadded() == 0)
                                if (blocklist.get(i).getFriend_id() == Integer.parseInt(item.users.getID(mContext))) {
                                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "You can not continue chat with blocked user");
                                    return;
                                } else if (blocklist.get(i).getFriend_id() == Integer.parseInt(item.users.getuID(mContext))) {
                                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "You can not continue chat with blocked user");
                                    return;
                                }
                        }
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("user_id", item.users.getID(mContext));
                    intent.putExtra("image", item.users.getImage(mContext));
                    intent.putExtra("name", item.users.getName(mContext));
                    intent.putExtra("msg", "");
                    intent.putExtra("type", "");
                    mContext.startActivity(intent);
                });
            }
        }
    }
}