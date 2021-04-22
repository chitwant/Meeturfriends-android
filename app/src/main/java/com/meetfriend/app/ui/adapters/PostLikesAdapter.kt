package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.friends.Data
import com.meetfriend.app.responseclasses.homeposts.ParentComment
import contractorssmart.app.utilsclasses.CommonMethods

class PostLikesAdapter(
    val userList: ArrayList<ParentComment>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String,
    val type: String
) :
    RecyclerView.Adapter<PostLikesAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onPostLikesUserClicked(position: Int)
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
        holder.bindItems(userList[position], callBack, context, baseUrl, type)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            dataX: ParentComment,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String,
            type: String
        ) {
            /*   val tvRemove=itemView.findViewById(R.id.tvRemove) as TextView*/
            val tvUserName = itemView.findViewById(R.id.tvUserName) as TextView
            val tvUpdateInfo = itemView.findViewById(R.id.tvUpdateInfo) as TextView
            val ivUserProfilePic = itemView.findViewById(R.id.ivUserProfilePic) as ImageView
            tvUserName.text = dataX.user.firstName + " " + dataX.user.lastName
            CommonMethods.setUserImage(
                ivUserProfilePic,
                baseUrl + dataX.user.profilePhoto
            )
            tvUpdateInfo.visibility = View.GONE

        }
    }
}