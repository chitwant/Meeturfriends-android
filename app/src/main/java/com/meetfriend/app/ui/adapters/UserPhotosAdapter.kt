package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.photos.Data
import contractorssmart.app.utilsclasses.CommonMethods


class UserPhotosAdapter(
    val userList: ArrayList<Data>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String
) :
    RecyclerView.Adapter<UserPhotosAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onLikeItemClicked(position: Int)
        fun onCancelItemClicked(position: Int)
        fun ItemClicked(position: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_user_photos_item, parent, false)
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
            dataX: Data,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String
        ) {
         /*   val tvRemove=itemView.findViewById(R.id.tvRemove) as TextView*/
            val ivUserImage = itemView.findViewById(R.id.ivUserImage) as ImageView
            val ivVideoIcon = itemView.findViewById(R.id.ivVideoIcon) as ImageView
            if (dataX.extension.equals("mov", true) ||dataX.extension.equals("mp4", true) || dataX.extension.equals(
                    "3gp",
                    true
                ) || dataX.extension.equals("mpeg", true)
            ) {
                CommonMethods.setImageWithGlideForVideo(ivUserImage, baseUrl + dataX.file_path,context)
                ivVideoIcon.visibility=View.VISIBLE
            }
            else
            {
                CommonMethods.setImagePicassoResize(ivUserImage, baseUrl + dataX.file_path)
                ivVideoIcon.visibility=View.GONE
            }

            ivUserImage.setOnClickListener {
                callBack.ItemClicked(adapterPosition)
            }

        }
    }
}