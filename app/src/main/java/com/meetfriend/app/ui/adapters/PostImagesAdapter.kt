package com.meetfriend.app.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.meetfriend.app.R
import contractorssmart.app.utilsclasses.CommonMethods
import droidninja.filepicker.utils.ContentUriUtils


class PostImagesAdapter(
    val userList: ArrayList<Uri>,
    var callBack: AdapterCallback,
    val context: Context,
    val type:String
) :
    RecyclerView.Adapter<PostImagesAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun onItemClicked(position: Int)
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
        holder.bindItems(userList[position], callBack, context)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            dataX: Uri,
            callBack: AdapterCallback,
            context: Context
        ) {
            val ivGalleryImage = itemView.findViewById(R.id.ivGalleryImage) as ImageView
            val ivDeleteImage = itemView.findViewById(R.id.ivDeleteImage) as ImageView
            val path = ContentUriUtils.getFilePath(context, dataX)
            Glide.with(context).load(path).into(ivGalleryImage)
           // CommonMethods.setLocalImage(ivGalleryImage,path!!)
          //  CommonMethods.setImage(ivUserImage, baseUrl  + dataX.profile_photo)
            ivDeleteImage.setOnClickListener {
                callBack.onItemClicked(adapterPosition)
            }

        }
    }
}