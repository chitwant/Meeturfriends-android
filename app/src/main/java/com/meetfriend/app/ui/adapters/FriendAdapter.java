package com.meetfriend.app.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.meetfriend.app.databinding.OnlineFriendsRowBinding;
import com.meetfriend.app.responseclasses.InboxPOJO;
import com.meetfriend.app.ui.activities.ChatActivity;

import java.util.List;

import contractorssmart.app.utilsclasses.CommonMethods;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    List<InboxPOJO> friendsList;
    Activity requireActivity;
    String baseUrl;

    public FriendAdapter(List<InboxPOJO> friendsList, Activity requireActivity, String baseUrl) {
        this.friendsList = friendsList;
        this.requireActivity = requireActivity;
        this.baseUrl = baseUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(OnlineFriendsRowBinding.inflate(LayoutInflater.from(requireActivity), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommonMethods.INSTANCE.setUserImage(holder.binding.mUserImage, friendsList.get(position).users.custImage);
        holder.binding.mName.setText(friendsList.get(position).users.custNAME);
        if (friendsList.get(position).status){
            holder.binding.mOnline.setVisibility(View.VISIBLE);
        }else
            holder.binding.mOnline.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity, ChatActivity.class);
                intent.putExtra("user_id", friendsList.get(position).users.custID + "");
                intent.putExtra(
                        "image",
                        friendsList.get(position).users.custImage
                );
                intent.putExtra("name", friendsList.get(position).users.custNAME);
                intent.putExtra("msg","");
                intent.putExtra("type", "");
                requireActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OnlineFriendsRowBinding binding;

        public ViewHolder(@NonNull OnlineFriendsRowBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
