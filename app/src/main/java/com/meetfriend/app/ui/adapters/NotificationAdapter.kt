package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.notification.data
import com.meetfriend.app.utilclasses.UtilsClass
import contractorssmart.app.utilsclasses.CommonMethods
import contractorssmart.app.utilsclasses.PreferenceHandler.readString

class NotificationAdapter(
    val notificationList: ArrayList<data>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun deleteNotification(id: String)
        fun openDetail(id: Int, from: String, title: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_notification_item, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }


    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.bindItems(notificationList[position], callBack, context, baseUrl)
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindItems(
            dataX: data,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String
        ) {
            val mAdView = itemView.findViewById(R.id.mAdView) as AdView
            val mAd = itemView.findViewById(R.id.mAd) as LinearLayout
            val lytCotainer = itemView.findViewById(R.id.lytCotainer) as ConstraintLayout
            val userImage = itemView.findViewById(R.id.ivUserImage) as ImageView
            val userName = itemView.findViewById(R.id.tv_name) as TextView
            val createTime = itemView.findViewById(R.id.tvDeviceLastTime) as TextView
            val ivTrashIcon = itemView.findViewById(R.id.ivTrashIcon) as ImageView
            if (adapterPosition % 5 == 0) {
                mAd.visibility = View.VISIBLE
//                UtilsClass.loadAd(mAdView)
                mAd.visibility = View.GONE
            } else {
                mAd.visibility = View.GONE
            }
            userName.text = dataX.message
            createTime.text = dataX.created_at
            if (dataX.from_user != null) {
                CommonMethods.setUserImage(
                    userImage,
                    baseUrl + dataX.from_user.profile_photo
                )
            } else {
                CommonMethods.setUserImage(
                    userImage,
                    readString(context, "PROFILE_PHOTO", "")
                )
            }
            ivTrashIcon.setOnClickListener {
                callBack.deleteNotification(dataX.id)
            }
            lytCotainer.setOnClickListener {
                callBack.openDetail(dataX.type_id, dataX.type, dataX.message)
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