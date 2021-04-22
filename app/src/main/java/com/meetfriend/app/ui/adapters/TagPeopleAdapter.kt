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
import contractorssmart.app.utilsclasses.CommonMethods


class TagPeopleAdapter(
    val userList: ArrayList<Data>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String,
    val type: String
) :
    RecyclerView.Adapter<TagPeopleAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onTagClicked(position: Int)
        fun unBlockClicked(position: Int)
        fun unFriendClicked(position: Int)
        fun openUserProfileFromTaggedList(position: Int)
        fun blockFriend(position: Int)
        fun chat(position: Int)
        fun remove(position: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_tag_people_item, parent, false)
        return ViewHolder(
            v
        )
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (type.equals("blocked", true)) {
            if (userList[position].is_blocked_by_me == 1)
                holder.bindItems(userList[position], callBack, context, baseUrl, type)
            else {
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }
        } else {
            holder.bindItems(userList[position], callBack, context, baseUrl, type)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            dataX: Data,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String,
            type: String
        ) {
            /*   val tvRemove=itemView.findViewById(R.id.tvRemove) as TextView*/
            val mRow = itemView.findViewById(R.id.mRow) as LinearLayout
            val tvUserName = itemView.findViewById(R.id.tvUserName) as TextView
            val tvUpdateInfo = itemView.findViewById(R.id.tvUpdateInfo) as TextView
            val tvBlockFriend = itemView.findViewById(R.id.tvBlockFriend) as TextView
            val ivUserProfilePic = itemView.findViewById(R.id.ivUserProfilePic) as ImageView
            tvUserName.text = dataX.accepted_user.firstName + " " + dataX.accepted_user.lastName
            CommonMethods.setUserImage(
                ivUserProfilePic,
                baseUrl + dataX.accepted_user.profile_photo
            )

            if (type.equals("friend", true)) {
                ivUserProfilePic.setOnClickListener {
                    callBack.openUserProfileFromTaggedList(adapterPosition)
                }
                tvUserName.setOnClickListener {
                    ivUserProfilePic.performClick()
                }
                tvUpdateInfo.visibility = View.VISIBLE
                tvBlockFriend.visibility = View.VISIBLE
                tvUpdateInfo.setText("Unfriend")
                tvUpdateInfo.setOnClickListener {
                    callBack.unFriendClicked(adapterPosition)
                }
                tvBlockFriend.setOnClickListener {
                    callBack.blockFriend(adapterPosition)
                }
            } else if (type.equals("blocked", true)) {
                if (dataX.is_blocked_by_me == 0) {
                    tvUpdateInfo.visibility = View.GONE
                    mRow.visibility = View.GONE
                    itemView.visibility = View.GONE
                } else {
                    tvUpdateInfo.visibility = View.VISIBLE
                    tvBlockFriend.visibility = View.GONE
                    tvUpdateInfo.setText("Unblock")
                    tvUpdateInfo.visibility = View.VISIBLE
                    mRow.visibility = View.VISIBLE
                    itemView.visibility = View.VISIBLE
                    itemView.setOnClickListener {
                        callBack.unBlockClicked(adapterPosition)
                    }
                }
            } else if (type.equals("chat", true)) {
                tvUpdateInfo.visibility = View.GONE
                tvBlockFriend.visibility = View.GONE
                itemView.setOnClickListener {
                    callBack.chat(adapterPosition)
                }
            } else {
                tvUpdateInfo.visibility = View.GONE
                tvBlockFriend.visibility = View.GONE
                itemView.setOnClickListener {
                    callBack.onTagClicked(adapterPosition)
                }
            }
        }
    }
}