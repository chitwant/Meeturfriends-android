package com.meetfriend.app.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meetfriend.app.databinding.ItemMessagesBinding;
import com.meetfriend.app.responseclasses.InboxPOJO;
import com.meetfriend.app.ui.activities.ChatActivity;

import java.util.List;


public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<InboxPOJO.Data> list;

    public MessagesAdapter(Context mContext, List<InboxPOJO.Data> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessagesBinding itemBinding =
                ItemMessagesBinding.inflate(LayoutInflater.from(mContext), parent, false);
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

    class Holder extends RecyclerView.ViewHolder {

        ItemMessagesBinding binding;

        Holder(ItemMessagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind() {
            InboxPOJO.Data item = list.get(getAdapterPosition());
            binding.setData(item);

            binding.llOuter.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user_id", item.users.getID(mContext));
                intent.putExtra("image", item.users.getImage(mContext));
                intent.putExtra("name", item.users.getName(mContext));
                intent.putExtra("msg","");
                intent.putExtra("type", "");
                mContext.startActivity(intent);
            });
        }
    }
}
