package com.meetfriend.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.homeposts.Datum
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import contractorssmart.app.utilsclasses.CommonMethods


class UserPostsAdapter(
    val userList: ArrayList<Datum>,
    var callBack: HomePostsAdadpterAdapterCallback,
    val context: Context,
    val baseUrl: String,
    val type: String
) :
    RecyclerView.Adapter<UserPostsAdapter.ViewHolder>() {
    interface HomePostsAdadpterAdapterCallback {
        fun onItemClicked(position: Int)
        fun onPostDeleted(position: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_homeposts_item, parent, false)
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
            dataX: Datum,
            callBack: HomePostsAdadpterAdapterCallback,
            context: Context,
            baseUrl: String,
            type: String
        ) {
            val tvUsername = itemView.findViewById(R.id.tvUsername) as TextView
            val tvPostTime = itemView.findViewById(R.id.tvPostTime) as TextView
            val ivUserPicture = itemView.findViewById(R.id.ivUserPicture) as ImageView
            //val ivPostPicture = itemView.findViewById(R.id.ivPostPicture) as ImageView
            val tvPostTitle = itemView.findViewById(R.id.tvPostTitle) as TextView
            val ivOptionPic = itemView.findViewById(R.id.ivOptionPic) as ImageView
            val likeShareLayout = itemView.findViewById(R.id.likeShareLayout) as ConstraintLayout
            val viewPager = itemView.findViewById(R.id.viewPagerHomeImages) as ViewPager
            //val dots_indicator = itemView.findViewById(R.id.dots_indicator) as SpringDotsIndicator
            tvUsername.visibility = View.INVISIBLE
            likeShareLayout.visibility = View.GONE
            if(type.equals("own")){
                ivOptionPic.visibility=View.VISIBLE
            }
            ivUserPicture.visibility = View.GONE
            tvPostTime.text = dataX.createdAt
            tvUsername.text = dataX.user.firstName + " " + dataX.user.lastName
            tvPostTitle.text = dataX.content
            // tvTitle.text = dataX.lastName
            if (dataX.postMedia.size > 0) {
                val adapter = HomePostImagesViewPager(context, dataX.postMedia, baseUrl)
                viewPager.adapter = adapter
               // dots_indicator.setViewPager(viewPager)
                if(dataX.postMedia.size==1)
                {
                    //dots_indicator.visibility=View.GONE
                }
                else
                {
                    //dots_indicator.visibility=View.VISIBLE
                }
            } else {
                //ivPostPicture.setImageResource(R.drawable.place_holder_image)
            }
            if (dataX.user != null) {

                // CommonMethods.setUserImage(ivUserPicture, baseUrl + dataX.user.profile_photo)
            }
            itemView.setOnClickListener {
                callBack.onItemClicked(adapterPosition)
            }
            //Long Press
            if (type.equals("own", true)) {
                itemView.setOnLongClickListener(OnLongClickListener { v ->
                   // ivOptionPic.performClick()
                    false
                })
            }
            ivOptionPic.setOnClickListener {
                //Creating the instance of PopupMenu
                val popup = PopupMenu(context, ivOptionPic)
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.user_post_menu, popup.getMenu())
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        if (item.itemId == R.id.deletePost) {
                            callBack.onPostDeleted(adapterPosition)
                        } else if (item.itemId == R.id.editPost) {
                            callBack.onItemClicked(adapterPosition)
                        }
                        /*   Toast.makeText(
                               context,
                               "You Clicked : " + item.getTitle(),
                               Toast.LENGTH_SHORT
                           ).show()*/
                        return true
                    }
                })

                popup.show()
            }

        }
    }
}