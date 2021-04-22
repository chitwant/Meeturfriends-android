package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.friends.Data

class BlockListAdapter(
    val userList: ArrayList<Data>,
    var callBack: BlockListAdapter.AdapterCallback,
    val context: Context,
    val baseUrl: String,
    val type: String
) : RecyclerView.Adapter<BlockListAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onTagClicked(position: Int)
        fun unBlockClicked(position: Int)
        fun unFriendClicked(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BlockListAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_blocklist_item, parent, false)
        return BlockListAdapter.ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: BlockListAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position], callBack, context, baseUrl, type)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            dataX: Data,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String,
            type: String
        ) {
            val mAd = itemView.findViewById(R.id.mAd) as LinearLayout
            val tvUserName = itemView.findViewById(R.id.tv_name) as TextView
            val tvUpdateInfo = itemView.findViewById(R.id.tvTime) as TextView
            val tvUnblock = itemView.findViewById(R.id.tvUnblock) as TextView
            val ivUserProfilePic = itemView.findViewById(R.id.ivUserProfilePic) as ImageView
        }
    }
}