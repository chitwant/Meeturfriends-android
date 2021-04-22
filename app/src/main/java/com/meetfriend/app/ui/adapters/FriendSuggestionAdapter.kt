package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.modals.friendsuggestion.Data
import contractorssmart.app.utilsclasses.CommonMethods
import org.w3c.dom.Text


class FriendSuggestionAdapter(
    val userList: ArrayList<Data>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String
) :
    RecyclerView.Adapter<FriendSuggestionAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onLikeItemClicked(position: Int)
        fun onCancelItemClicked(position: Int)
        fun friendItemClicked(position: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_friend_suggestion_item, parent, false)
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            dataX: Data,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String
        ) {
            val tvTitle = itemView.findViewById(R.id.tvTitle) as TextView
            val tvDesc = itemView.findViewById(R.id.tvDesc) as TextView
            val tvAddFriend = itemView.findViewById(R.id.tvAddFriend) as TextView
            val tvRemove = itemView.findViewById(R.id.tvRemove) as TextView
            val ivUserImage = itemView.findViewById(R.id.ivUserImage) as ImageView
            val tvRequestSent = itemView.findViewById(R.id.tvRequestSent) as TextView
            tvDesc.text = dataX.firstName + " " + dataX.lastName
            if (dataX.ismyfriend_count == 1) {
                tvRequestSent.visibility = View.VISIBLE
                tvAddFriend.visibility = View.GONE
                tvRemove.setText("Cancel")
            } else {
                tvAddFriend.visibility = View.VISIBLE
                tvRequestSent.visibility = View.GONE
                tvRemove.setText("Remove")
            }
            // tvTitle.text = dataX.lastName
            CommonMethods.setUserImageManPlaceHolder(ivUserImage, baseUrl + dataX.profile_photo)
            tvAddFriend.setOnClickListener {
                callBack.onLikeItemClicked(adapterPosition)
            }
            tvRemove.setOnClickListener {
                callBack.onCancelItemClicked(adapterPosition)
            }
            ivUserImage.setOnClickListener {
                callBack.friendItemClicked(adapterPosition)
            }

        }
    }
}