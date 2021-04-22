package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.searchuser.ResultUser
import contractorssmart.app.utilsclasses.CommonMethods


class SearchUsersAdapter(
   val isFriend: String,
    val userList: ArrayList<ResultUser>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String
) :
    RecyclerView.Adapter<SearchUsersAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onUserClicked(position: Int)
        fun addFriendClicked(position: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_search_item, parent, false)
        return ViewHolder(
            v
        )
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position], callBack, context, baseUrl)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            dataX: ResultUser,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String
        ) {
            /*   val tvRemove=itemView.findViewById(R.id.tvRemove) as TextView*/
            val tvUserName = itemView.findViewById(R.id.tvUserName) as TextView
            val tvStatus = itemView.findViewById(R.id.tvStatus) as TextView
            val tvAddFriend = itemView.findViewById(R.id.tvAddFriend) as TextView
            val ivUserProfilePic = itemView.findViewById(R.id.ivUserProfilePic) as ImageView
            tvUserName.text = dataX.firstName + " " + dataX.lastName
            CommonMethods.setUserImage(
                ivUserProfilePic,
                baseUrl + dataX.profile_photo
            )
            if (dataX.ismyfriend_count == 1) {
                tvStatus.setText("Friend")
                tvAddFriend.visibility=View.GONE
            } else {
                tvStatus.setText("")
                tvAddFriend.visibility=View.VISIBLE
            }

            tvUserName.setOnClickListener {
                callBack.onUserClicked(adapterPosition)
            }
            ivUserProfilePic.setOnClickListener {
                tvUserName.performClick()
            }
            tvAddFriend.setOnClickListener { callBack.addFriendClicked(adapterPosition) }

        }
    }
}