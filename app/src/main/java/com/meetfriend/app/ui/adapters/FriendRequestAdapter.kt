package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.friendrequest.Datum
import com.meetfriend.app.utilclasses.UtilsClass
import contractorssmart.app.utilsclasses.CommonMethods

class FriendRequestAdapter(
    val userList: ArrayList<Datum>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String
) :
    RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onRejectReuest(position: Int)
        fun onAcceptRequest(position: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_friend_request_item, parent, false)
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
            dataX: Datum,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String
        ) {
            /*   val tvRemove=itemView.findViewById(R.id.tvRemove) as TextView*/
            val mAd = itemView.findViewById(R.id.mAd) as LinearLayout
            val mAdView = itemView.findViewById(R.id.mAdView) as AdView
            val ivAcceptRequest = itemView.findViewById(R.id.ivAcceptRequest) as ImageView
            val ivCancelRequest = itemView.findViewById(R.id.ivCancelRequest) as ImageView
            val tvUserName = itemView.findViewById(R.id.tvUserName) as TextView
            val ivUserProfilePic = itemView.findViewById(R.id.ivUserProfilePic) as ImageView
            if (adapterPosition%5==0){
                mAd.visibility=View.VISIBLE
                UtilsClass.loadAd(mAdView)
            }else{
                mAd.visibility=View.GONE
            }
            tvUserName.text=dataX.pendingUser.firstName+" "+dataX.pendingUser.lastName
            CommonMethods.setUserImage(ivUserProfilePic,baseUrl+dataX.pendingUser.profilePhoto)
            ivAcceptRequest.setOnClickListener {
                callBack.onAcceptRequest(adapterPosition)
            }
            ivCancelRequest.setOnClickListener {
                callBack.onRejectReuest(adapterPosition)
            }

        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}