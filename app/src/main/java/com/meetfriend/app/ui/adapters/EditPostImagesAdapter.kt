package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.homeposts.PostMedium
import contractorssmart.app.utilsclasses.CommonMethods


class EditPostImagesAdapter(
    val userList: ArrayList<PostMedium>,
    var callBack: AdapterCallback,
    val context: Context,
    val type: String,
    val mediaUrl: String
) :
    RecyclerView.Adapter<EditPostImagesAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onDeleteEditPics(position: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_post_gallery_item, parent, false)
        return ViewHolder(
            v
        )
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position], callBack, context,mediaUrl)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            dataX: PostMedium,
            callBack: AdapterCallback,
            context: Context,
            mediaUrl: String
        ) {
            val ivGalleryImage = itemView.findViewById(R.id.ivGalleryImage) as ImageView
            val ivDeleteImage = itemView.findViewById(R.id.ivDeleteImage) as ImageView
            CommonMethods.setImage(ivGalleryImage,mediaUrl+dataX.filePath)
            ivDeleteImage.visibility=View.VISIBLE
            //  CommonMethods.setImage(ivUserImage, baseUrl  + dataX.profile_photo)
            ivDeleteImage.setOnClickListener {
                callBack.onDeleteEditPics(adapterPosition)
            }

        }
    }
}