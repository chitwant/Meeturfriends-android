package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.homeposts.Comment
import contractorssmart.app.utilsclasses.CommonMethods


class HomePostsCommentsNewAdapter(
    val userList: ArrayList<Comment>,
    val context: Context,
    val baseurl: String,
    val callBack: HomePostCommentsAdapterAdapterCallback,
    val myUserId: String
) :
    RecyclerView.Adapter<HomePostsCommentsNewAdapter.ViewHolder>() {
    interface HomePostCommentsAdapterAdapterCallback {
        fun onDeleteClick(position: Int,rposition: Int)
        fun onReplyClicked(position: Int)
        fun onEditCommentClicked(position: Int,rposition: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_home_posts_comments_item, parent, false)
        return ViewHolder(
            v
        )
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position], context, baseurl, callBack, myUserId)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            dataX: Comment,
            context: Context,
            baseurl: String,
            callBack: HomePostCommentsAdapterAdapterCallback,
            myUserId: String
        ) {
            val commentReply = itemView.findViewById(R.id.mCommentReply) as RecyclerView
            val tvComment = itemView.findViewById(R.id.tvComment) as TextView
            val tvTime = itemView.findViewById(R.id.tvTime) as TextView
            val tvUserNameComment = itemView.findViewById(R.id.tvUserNameComment) as TextView
            val ivProfilePicComment = itemView.findViewById(R.id.ivProfilePicComment) as ImageView
            val tvReply = itemView.findViewById(R.id.tvReply) as TextView
            val tvDelete = itemView.findViewById(R.id.tvDelete) as TextView
            val tvEdit = itemView.findViewById(R.id.tvEdit) as TextView
            if (dataX.user_id == myUserId.toInt()) {
                tvDelete.visibility = View.VISIBLE
                tvEdit.visibility = View.VISIBLE
                tvReply.visibility = View.GONE
            } else {
                tvDelete.visibility = View.GONE
                tvReply.visibility = View.VISIBLE
                tvEdit.visibility = View.GONE
            }
            tvTime.text = dataX.created_at
            tvComment.text = dataX.content
            tvUserNameComment.visibility = View.VISIBLE
            tvUserNameComment.text = dataX.user.firstName + " " + dataX.user.lastName
            if (dataX.user.profilePhoto != null)
                CommonMethods.setUserImage(ivProfilePicComment, baseurl + dataX.user.profilePhoto)
            if (dataX.child_comments != null) {
                if (dataX.child_comments.size > 0) {
                    commentReply.visibility = View.VISIBLE
                    commentReply.adapter = CommentReplyAdapter(
                        context,
                        dataX.child_comments,
                        baseurl,
                        myUserId,
                        callBack,
                        adapterPosition
                    )
                } else {
                    commentReply.visibility = View.GONE
                }
            } else {
                commentReply.visibility = View.GONE
            }
            tvReply.setOnClickListener {
                callBack.onReplyClicked(adapterPosition)
            }

            tvDelete.setOnClickListener {
                callBack.onDeleteClick(adapterPosition,-1)
            }
            tvEdit.setOnClickListener {
                callBack.onEditCommentClicked(adapterPosition,-1)
            }
        }
    }
}