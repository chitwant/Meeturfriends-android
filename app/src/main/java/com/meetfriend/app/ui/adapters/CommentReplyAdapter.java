package com.meetfriend.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ItemviewHomePostsCommentsItemBinding;
import com.meetfriend.app.responseclasses.homeposts.ChildComments;

import java.util.List;

import contractorssmart.app.utilsclasses.CommonMethods;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.ViewHolder> {
    Context context;
    List<ChildComments> childComments;
    String baseurl;
    String myUserId;
    HomePostsCommentsNewAdapter.HomePostCommentsAdapterAdapterCallback callBack;
    int pp;

    public CommentReplyAdapter(Context context, List<ChildComments> childComments, String baseurl, String myUserId, HomePostsCommentsNewAdapter.HomePostCommentsAdapterAdapterCallback callBack, int pp) {
        this.context = context;
        this.pp = pp;
        this.childComments = childComments;
        this.baseurl = baseurl;
        this.myUserId = myUserId;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemviewHomePostsCommentsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.itemview_home_posts_comments_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommonMethods.INSTANCE.setUserImage(holder.binding.ivProfilePicComment, baseurl + childComments.get(position).getUser().getProfilePhoto());
        holder.binding.tvUserNameComment.setText(childComments.get(position).getUser().getFirstName()+" "+childComments.get(position).getUser().getLastName());
        holder.binding.tvComment.setText(childComments.get(position).getContent());
        holder.binding.tvTime.setText(childComments.get(position).getCreatedAt());
        if (myUserId.equalsIgnoreCase(childComments.get(position).getUserId().toString())) {
            holder.binding.tvEdit.setVisibility(View.VISIBLE);
            holder.binding.tvDelete.setVisibility(View.VISIBLE);
            holder.binding.tvReply.setVisibility(View.GONE);
        } else {
            holder.binding.tvEdit.setVisibility(View.GONE);
            holder.binding.tvDelete.setVisibility(View.GONE);
            holder.binding.tvReply.setVisibility(View.VISIBLE);
        }
        holder.binding.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onReplyClicked(position);
            }
        });
        holder.binding.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onReplyClicked(pp);
            }
        });
        holder.binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onEditCommentClicked(pp,position);
            }
        });
        holder.binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDeleteClick(pp,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return childComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemviewHomePostsCommentsItemBinding binding;

        public ViewHolder(@NonNull ItemviewHomePostsCommentsItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
