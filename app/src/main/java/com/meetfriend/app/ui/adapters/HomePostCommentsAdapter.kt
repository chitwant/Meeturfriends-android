package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.modals.friendsuggestion.homepost.Comments
import com.meetfriend.app.responseclasses.homeposts.Comment
import contractorssmart.app.utilsclasses.CommonMethods


class HomePostCommentsAdapter(
    val userList: ArrayList<Comment>,
    val context: Context,
    val baseurl: String
) :
    RecyclerView.Adapter<HomePostCommentsAdapter.ViewHolder>() {
    interface HomePostCommentsAdapterAdapterCallback {
        fun onDeleteClick(position: Int)
        fun onReplyClicked(position: Int)

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
        holder.bindItems(userList[position], context, baseurl)
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
            baseurl: String
        ) {
            val tvComment = itemView.findViewById(R.id.tvComment) as TextView
            val tvTime = itemView.findViewById(R.id.tvTime) as TextView
            val tvUserNameComment = itemView.findViewById(R.id.tvUserNameComment) as TextView
            val ivProfilePicComment = itemView.findViewById(R.id.ivProfilePicComment) as ImageView
            val tvReply = itemView.findViewById(R.id.tvReply) as TextView
            val tvDelete = itemView.findViewById(R.id.tvDelete) as TextView
            tvTime.text = dataX.created_at
            tvComment.text = dataX.content
            tvUserNameComment.text = dataX.user.firstName + " " + dataX.user.lastName
            CommonMethods.setImage(ivProfilePicComment, baseurl + dataX.user.profilePhoto)

        }
    }
}