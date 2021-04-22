package com.meetfriend.app.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.homeposts.PostMedium
import contractorssmart.app.utilsclasses.CommonMethods


class HorizontalPostsImagesRecyclerAdapter(
    val context: Context,
    val postMedia: ArrayList<PostMedium>,
    val baseUrl: String
) :
    RecyclerView.Adapter<HorizontalPostsImagesRecyclerAdapter.ViewHolder>() {
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
                .inflate(R.layout.home_view_pager_item, parent, false)
        return ViewHolder(
            v
        )
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(postMedia[position], context, baseUrl)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return postMedia.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            dataX: PostMedium,
            context: Context,
            baseUrl: String
        ) {
            /*   val tvRemove=itemView.findViewById(R.id.tvRemove) as TextView*/
            val ivUserImage = itemView.findViewById(R.id.ivPostPicture) as ImageView
            val ivVideoIcon = itemView.findViewById(R.id.ivVideoIcon) as ImageView
            if (dataX.extension.equals("mov", true) ||dataX.extension.equals("mp4", true) || dataX.extension.equals(
                    "3gp",
                    true
                ) || dataX.extension.equals("mpeg", true)
            ) {

                CommonMethods.setImageWithGlideForVideo(ivUserImage, baseUrl + dataX.filePath,context)
                ivVideoIcon.visibility = View.VISIBLE
               /* ivUserImage.setBackgroundColor(Color.BLACK)
                ivVideoIcon.visibility = View.VISIBLE
                ivUserImage.getLayoutParams().height = 500
                ivUserImage.requestLayout();*/
                ivUserImage.setOnClickListener {

                }
            } else {
                CommonMethods.setImage(ivUserImage, baseUrl + dataX.filePath)
                ivVideoIcon.visibility = View.GONE

            }

            /* ivUserImage.setOnClickListener {
                 //callBack.ItemClicked(adapterPosition)
             }*/

        }
    }
}