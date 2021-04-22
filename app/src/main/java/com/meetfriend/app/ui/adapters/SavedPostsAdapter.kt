package com.meetfriend.app.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.savedposts.Datum
import com.meetfriend.app.utilclasses.RecyclerItemClickListener
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import contractorssmart.app.utilsclasses.CommonMethods
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator


class SavedPostsAdapter(
    val userList: ArrayList<Datum>,
    var callBack: HomePostsAdadpterAdapterCallback,
    val context: Context,
    val baseUrl: String,
    val myUserId: String
) :
    RecyclerView.Adapter<SavedPostsAdapter.ViewHolder>() {
    interface HomePostsAdadpterAdapterCallback {
        fun onItemClicked(position: Int)
        fun onItemLiked(position: Int)
        fun onPostReported(position: Int)
        fun onPostSaved(positiom: Int)
        fun onPostHidden(position: Int)
        fun postShared(position: Int)
        fun onCommentAdded(position: Int, comment: String)
        fun likePost(position: Int)
        fun onHomePostItemClicked(position: Int, childPosition: Int)
        fun onHomePostEditClicked(position: Int)
        fun onPostDeleted(position: Int)
        fun openProfile(position: Int)
        fun openCommentsScreen(position: Int)
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
        holder.bindItems(userList[position], callBack, context, baseUrl, myUserId)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindItems(
            dataX: Datum,
            callBack: HomePostsAdadpterAdapterCallback,
            context: Context,
            baseUrl: String,
            myUserId: String
        ) {
            val parentLayoutHomePostItem = itemView.findViewById(R.id.parentLayoutHomePostItem) as ConstraintLayout
            val tvUsername = itemView.findViewById(R.id.tvUsername) as TextView
            val tvPostTime = itemView.findViewById(R.id.tvPostTime) as TextView
            val ivUserPicture = itemView.findViewById(R.id.ivUserPicture) as ImageView
            //  val ivPostPicture = itemView.findViewById(R.id.ivPostPicture) as ImageView
            val tvPostTitle = itemView.findViewById(R.id.tvPostTitle) as TextView
            val ivOptionPic = itemView.findViewById(R.id.ivOptionPic) as ImageView
            val ivLikePost = itemView.findViewById(R.id.ivLikePost) as ImageView
            val ivSharePost = itemView.findViewById(R.id.ivSharePost) as ImageView
            val ivComment = itemView.findViewById(R.id.ivComment) as ImageView
            val viewPager = itemView.findViewById(R.id.viewPagerHomeImages) as ViewPager
            //val dots_indicator = itemView.findViewById(R.id.dots_indicator) as SpringDotsIndicator
            val addCommentLayout = itemView.findViewById(R.id.addCommentLayout) as RelativeLayout
            val ivSendMessage = itemView.findViewById(R.id.ivSendMessage) as ImageView
            val etComment = itemView.findViewById(R.id.etComment) as EditText
            val tvTotalLikes = itemView.findViewById(R.id.tvTotalLikes) as TextView
            val tvTotalComments = itemView.findViewById(R.id.tvTotalComments) as TextView
            val tvTotalShares = itemView.findViewById(R.id.tvTotalShares) as TextView
            val ivOtherUserPicture = itemView.findViewById(R.id.ivOtherUserPicture) as ImageView
            val tvOtherUsername = itemView.findViewById(R.id.tvOtherUsername) as TextView
            val tvOtherPostTime = itemView.findViewById(R.id.tvOtherPostTime) as TextView
            val tvOtherPostTitle = itemView.findViewById(R.id.tvOtherPostTitle) as TextView
            //val viewotherComment = itemView.findViewById(R.id.viewotherComment) as View
            val likeShareLayout=itemView.findViewById(R.id.likeShareLayout)as ConstraintLayout
            val totalLayout=itemView.findViewById(R.id.totalLayout) as LinearLayout
            totalLayout.visibility=View.GONE
            likeShareLayout.visibility=View.GONE
            val recyclerViewHorizontalHomeImages =
                itemView.findViewById(R.id.recyclerViewHorizontalHomeImages) as RecyclerView
            val indicator =
                itemView.findViewById(R.id.indicator) as ScrollingPagerIndicator
            val viewBelow = itemView.findViewById(R.id.viewBelow) as View
            val viewSharedTop = itemView.findViewById(R.id.viewSharedTop) as View
            val viewSharedLeft = itemView.findViewById(R.id.viewSharedLeft) as View
            val viewSharedRight = itemView.findViewById(R.id.viewSharedRight) as View
            val viewSharedBottom = itemView.findViewById(R.id.viewSharedBottom) as View
            val viewLikeShare = itemView.findViewById(R.id.viewLikeShare) as View
            val viewcomments = itemView.findViewById(R.id.viewcomments) as View
            viewBelow.visibility=View.GONE
            viewLikeShare.visibility=View.GONE
            viewcomments.visibility=View.GONE
            if (dataX.post==null){
                parentLayoutHomePostItem.visibility=View.GONE
            }else{
                if (dataX.userId.toString().equals(myUserId)) {
                    ivOptionPic.visibility = View.VISIBLE
                } else {
                    ivOptionPic.visibility = View.VISIBLE
                }
                /*itemView.setOnClickListener {
                    callBack.onHomePostItemClicked(adapterPosition)
                }*/
                val commentsRecyclerView =
                    itemView.findViewById(R.id.commentsRecyclerView) as RecyclerView
                if (dataX.post!=null)
                    if (dataX.post.postLikesCount > 1) {
                        tvTotalLikes.setText(dataX.post.postLikesCount.toString() + " Likes")
                    } else {
                        tvTotalLikes.setText(dataX.post.postLikesCount.toString() + " Like")
                    }
                if (dataX.post.postComments.size > 1) {
                    tvTotalComments.setText(dataX.post.postComments.size.toString() + " Comments")
                } else {
                    tvTotalComments.setText(dataX.post.postComments.size.toString() + " Comment")
                }
                tvTotalShares.setText(dataX.post.noOfSharedCount.toString() + " Share")
                tvPostTime.text = dataX.createdAt
                if (dataX.post.type.equals("profile_cover_photo", true)) {
                    tvUsername.text =
                        dataX.post.user.firstName + " " + dataX.post.user.lastName + " updated cover photo"
                } else if (dataX.post.type.equals("profile_photo", true)) {
                    tvUsername.text =
                        dataX.post.user.firstName + " " + dataX.post.user.lastName + " updated profile photo"
                } else {
                    if (dataX.post.taggedUsers != null && dataX.post.taggedUsers.size > 0) {
                        var text = ""
                        if (dataX.post.taggedUsers!!.size > 0 && dataX.post.taggedUsers!!.get(0).user != null) {
                            if (dataX.post.taggedUsers!!.size == 1) {
                                text =
                                    text + "<b>"+dataX.post.user.firstName + " " + dataX.post.user.lastName +"</b>"+ " is with " + "<b>"+dataX.post.taggedUsers!!.get(
                                        0
                                    ).user.firstName + " " + dataX.post.taggedUsers!!.get(0).user.lastName+"</b>"
                            } else {
                                text =
                                    text + "<b>"+dataX.post.user.firstName + " " + dataX.post.user.lastName+"</b>" + " is with " +"<b>"+ dataX.post.taggedUsers!!.get(
                                        0
                                    ).user.firstName!! + " " + dataX.post.taggedUsers!!.get(0).user.lastName + " and " + dataX.post.taggedUsers!!.size.minus(
                                        1
                                    ) + " other"+"</b>"

                            }
                            // tvTaggedLocation.setText("- with")
                        }
                        if (dataX.post.location != null) {
                            text =
                                text + " in " +"<b>"+ dataX.post.location+"</b>"
                        }
                        tvUsername.setText(CommonMethods.fromHtml(text))
                        //tvUsername.text = text
                    } else {
                        var text = ""
                        if (dataX.post.location != null) {
                            text =
                                text + "<b>"+dataX.post.user.firstName + " " + dataX.post.user.lastName +"</b>"+ " in " + "<b>"+dataX.post.location+"</b>"
                        } else {
                            text =
                                dataX.post.user.firstName + " " + dataX.post.user.lastName
                        }
                        tvUsername.setText(CommonMethods.fromHtml(text))
                        /*tvUsername.text =
                            text*/
                    }

                }
                // tvUsername.text = dataX.user.firstName + " " + dataX.user.lastName
                if (dataX.post.content==null){
                    tvPostTitle.visibility=View.GONE
                }else
                    tvPostTitle.text = dataX.post.content
                if (dataX.post.user != null) {
                    CommonMethods.setUserImage(ivUserPicture, baseUrl + dataX.post.user.profilePhoto)
                }
                if (dataX.post.isShared == 1) {
                    tvOtherPostTime.visibility = View.VISIBLE
                    tvOtherUsername.visibility = View.VISIBLE
                    tvOtherPostTitle.visibility = View.VISIBLE
                    ivOtherUserPicture.visibility = View.VISIBLE
                    viewSharedTop.visibility = View.VISIBLE
                    viewSharedLeft.visibility = View.VISIBLE
                    viewSharedRight.visibility = View.VISIBLE
                    viewSharedBottom.visibility = View.VISIBLE
                    //viewotherComment.visibility = View.GONE
                    if (dataX.post.sharedPost != null) {
                        tvOtherPostTime.text = dataX.post.sharedPost.created_at
                        if (dataX.post.sharedPost.type.equals("profile_cover_photo", true)) {
                            tvOtherUsername.text =
                                dataX.post.sharedPost.user.firstName + " " + dataX.post.sharedPost.user.lastName + " updated cover photo"
                        } else if (dataX.post.sharedPost.type.equals("profile_photo", true)) {
                            tvOtherUsername.text =
                                dataX.post.sharedPost.user.firstName + " " + dataX.post.sharedPost.user.lastName + " updated profile photo"
                        } else {
                            tvOtherUsername.text =
                                dataX.post.sharedPost.user.firstName + " " + dataX.post.sharedPost.user.lastName
                        }


                        tvOtherPostTitle.text = dataX.post.sharedPost.content
                        if (dataX.post.user != null) {
                            CommonMethods.setUserImage(
                                ivOtherUserPicture,
                                baseUrl + dataX.post.sharedPost.user.profilePhoto
                            )
                        }
                        if (dataX.post.sharedPost.postMediumArrayList.size > 0) {
                            viewPager.visibility = View.GONE
                            recyclerViewHorizontalHomeImages.visibility = View.VISIBLE
                            // val adapter = HomePostImagesViewPager(context, dataX.postMedia, baseUrl)
                            //viewPager.adapter = adapter
                            // dots_indicator.setViewPager(viewPager)
                            var adapter =
                                HorizontalPostsImagesRecyclerAdapter(
                                    context,
                                    dataX.post.sharedPost.postMediumArrayList,
                                    baseUrl
                                )
                            recyclerViewHorizontalHomeImages.setLayoutManager(
                                LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            )
                            recyclerViewHorizontalHomeImages.adapter = adapter
                            recyclerViewHorizontalHomeImages.addOnItemTouchListener(
                                RecyclerItemClickListener(
                                    context,
                                    RecyclerItemClickListener.OnItemClickListener { view, childPosition ->
                                        callBack.onHomePostItemClicked(adapterPosition, childPosition)
                                        /*callBack.onHomePostItemClickedChild(
                                            adapterPosition,
                                            childPosition
                                        )*/
                                        //itemView.performClick()
                                        // Log.e("@@@@@", "" + position)
                                    })
                            )
                            indicator.attachToRecyclerView(recyclerViewHorizontalHomeImages);
                            if (dataX.post.sharedPost.postMediumArrayList.size == 1) {
                                // dots_indicator.visibility = View.GONE
                                indicator.visibility = View.GONE
                            } else {
                                //dots_indicator.visibility = View.GONE
                                indicator.visibility = View.VISIBLE
                            }
                            // CommonMethods.setImage(ivPostPicture, baseUrl + dataX.post_media.get(0).file_path)
                        } else {
                            recyclerViewHorizontalHomeImages.visibility = View.GONE
                            viewPager.visibility = View.GONE
                            //dots_indicator.visibility = View.GONE
                            //ivPostPicture.setImageResource(R.drawable.place_holder_image)
                        }
                    }

                } else {
                    tvOtherPostTime.visibility = View.GONE
                    tvOtherUsername.visibility = View.GONE
                    tvOtherPostTitle.visibility = View.GONE
                    ivOtherUserPicture.visibility = View.GONE
                    // viewotherComment.visibility = View.GONE
                    viewSharedTop.visibility = View.GONE
                    viewSharedLeft.visibility = View.GONE
                    viewSharedRight.visibility = View.GONE
                    viewSharedBottom.visibility = View.GONE
                    if (dataX.post.postMedia.size > 0) {
                        viewPager.visibility = View.GONE
                        recyclerViewHorizontalHomeImages.visibility = View.VISIBLE
                        // val adapter = HomePostImagesViewPager(context, dataX.postMedia, baseUrl)
                        //viewPager.adapter = adapter
                        // dots_indicator.setViewPager(viewPager)
                        var adapter =
                            HorizontalPostsImagesRecyclerAdapter(context, dataX.post.postMedia, baseUrl)
                        recyclerViewHorizontalHomeImages.setLayoutManager(
                            LinearLayoutManager(
                                context,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        )
                        recyclerViewHorizontalHomeImages.adapter = adapter
                        recyclerViewHorizontalHomeImages.addOnItemTouchListener(
                            RecyclerItemClickListener(
                                context,
                                RecyclerItemClickListener.OnItemClickListener { view, childPosition ->
                                    callBack.onHomePostItemClicked(adapterPosition, childPosition)
                                    //callBack.onHomePostItemClickedChild(adapterPosition, childPosition)
                                    // Log.e("@@@@@", "" + position)
                                })
                        )
                        indicator.attachToRecyclerView(recyclerViewHorizontalHomeImages);
                        if (dataX.post.postMedia.size == 1) {
                            // dots_indicator.visibility = View.GONE
                            indicator.visibility = View.GONE
                        } else {
                            //dots_indicator.visibility = View.GONE
                            indicator.visibility = View.VISIBLE
                        }
                        // CommonMethods.setImage(ivPostPicture, baseUrl + dataX.post_media.get(0).file_path)
                    } else {
                        recyclerViewHorizontalHomeImages.visibility = View.GONE
                        viewPager.visibility = View.GONE
                        //dots_indicator.visibility = View.GONE
                        //ivPostPicture.setImageResource(R.drawable.place_holder_image)
                    }
                }

                commentsRecyclerView.layoutManager = LinearLayoutManager(context)
                commentsRecyclerView.adapter =
                    HomePostCommentsAdapter(dataX.post.postComments, context, baseUrl)

                if (dataX.post.isLikedCount != null) {
                    if (dataX.post.isLikedCount == 1) {
                        ivLikePost.setImageResource(R.drawable.ic_post_liked_red_heart)
                    } else {
                        ivLikePost.setImageResource(R.drawable.heart_icon_outline)
                    }
                }

                ivLikePost.setOnClickListener {
                    callBack.likePost(adapterPosition)
                }
                ivSharePost.setOnClickListener {
                    callBack.postShared(adapterPosition)
                }
                ivComment.setOnClickListener {
                    callBack.onItemClicked(adapterPosition)
                    // addCommentLayout.visibility = View.VISIBLE
                    // commentsRecyclerView.visibility = View.VISIBLE
                }
                ivSendMessage.setOnClickListener {
                    if (!etComment.text.toString().trim().equals("")) {
                        callBack.onCommentAdded(adapterPosition, etComment.text.toString().trim())
                        etComment.setText("")
                    }
                }
                viewPager.setOnClickListener {
                    itemView.performClick()
                }

                ivUserPicture.setOnClickListener {
                    callBack.openProfile(adapterPosition)
                }
                tvUsername.setOnClickListener {
                    ivUserPicture.performClick()
                }
                tvTotalComments.setOnClickListener {
                    callBack.openCommentsScreen(adapterPosition)
                }
                ivOptionPic.setOnClickListener {
                    if (dataX.userId.toString().equals(myUserId)) {
                        val popup = PopupMenu(context, ivOptionPic)
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.user_post_menu, popup.getMenu())
                        if (dataX.post.isShared == 1) {
                            popup.getMenu().findItem(R.id.editPost).setVisible(false);
                        } else {
                            if (dataX.post.type.equals("profile_cover_photo", true)) {
                                popup.getMenu().findItem(R.id.editPost).setVisible(false);
                            } else if (dataX.post.type.equals("profile_photo", true)) {
                                popup.getMenu().findItem(R.id.editPost).setVisible(false);
                            } else {
                                popup.getMenu().findItem(R.id.editPost).setVisible(false);
                            }


                        }
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                            override fun onMenuItemClick(item: MenuItem): Boolean {
                                if (item.itemId == R.id.deletePost) {
                                    callBack.onPostDeleted(adapterPosition)
                                } else if (item.itemId == R.id.editPost) {
                                    callBack.onHomePostEditClicked(adapterPosition)
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
                    } else {
                        //Creating the instance of PopupMenu
                        val popup = PopupMenu(context, ivOptionPic)
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.home_post_menu, popup.getMenu())
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                            override fun onMenuItemClick(item: MenuItem): Boolean {
                                if (item.itemId == R.id.reportPost) {
                                    callBack.onPostReported(adapterPosition)
                                } else if (item.itemId == R.id.savePost) {
                                    callBack.onPostSaved(adapterPosition)
                                } else {
                                    callBack.onPostHidden(adapterPosition)
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
    }
}