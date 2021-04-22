package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.settings.data

class SecurityManagmentAdapter(
    val securityList: ArrayList<data>,
    var callBack: AdapterCallback,
    val context: Context
) : RecyclerView.Adapter<SecurityManagmentAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun itemClick(result: data)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_securitymanagment_item, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return securityList.size
    }

    override fun onBindViewHolder(holder: SecurityManagmentAdapter.ViewHolder, position: Int) {
        holder.bindItems(securityList[position], callBack, context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            result: data,
            callBack: AdapterCallback,
            context: Context
        ) {
            val deviceName = itemView.findViewById(R.id.tv_name) as TextView
            val deviceLastTime = itemView.findViewById(R.id.tvDeviceLastTime) as TextView
            val ivMoreIcon = itemView.findViewById(R.id.ivMoreIcon) as ImageView
            deviceName.text = result.device_model + "-" + result.device_location
            deviceLastTime.text = result.created_at
            ivMoreIcon.setOnClickListener {
                callBack.itemClick(result)
            }
        }
    }
}