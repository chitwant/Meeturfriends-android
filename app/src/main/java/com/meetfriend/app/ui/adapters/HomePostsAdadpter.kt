package com.meetfriend.app.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.homeposts.Datum
import com.meetfriend.app.utilclasses.RecyclerItemClickListener
import com.meetfriend.app.utilclasses.UtilsClass
import contractorssmart.app.utilsclasses.CommonMethods
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import kotlin.math.roundToInt


class HomePostsAdadpter(
    val userList: ArrayList<Datum>,
    var callBack: HomePostsAdadpterAdapterCallback,
    val context: Context,
    val baseUrl: String,
    val myUserId: String
) :
    RecyclerView.Adapter<HomePostsAdadpter.ViewHolder>() {
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
        fun openLikes(position: Int)
        fun editSharedPost(position: Int, content: String)
        fun sendGift(position: Int)
        fun openFriendProfile(position: Int)
        fun openTaggedList(position: Int)

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
        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun bindItems(
            dataX: Datum,
            callBack: HomePostsAdadpterAdapterCallback,
            context: Context,
            baseUrl: String,
            myUserId: String
        ) {
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
            val ivSendGift = itemView.findViewById(R.id.ivSendGift) as ImageView
            val tvOtherPostTime = itemView.findViewById(R.id.tvOtherPostTime) as TextView
            val mAdView = itemView.findViewById(R.id.mAdView) as AdView
            val mAd = itemView.findViewById(R.id.mAd) as LinearLayout
            val tvOtherPostTitle = itemView.findViewById(R.id.tvOtherPostTitle) as TextView
            //val viewotherComment = itemView.findViewById(R.id.viewotherComment) as View
            var snapHelper: SnapHelper = LinearSnapHelper()
            val recyclerViewHorizontalHomeImages =
                itemView.findViewById(R.id.recyclerViewHorizontalHomeImages) as RecyclerView
            recyclerViewHorizontalHomeImages.setOnFlingListener(null)
            snapHelper.attachToRecyclerView(recyclerViewHorizontalHomeImages)
            val indicator =
                itemView.findViewById(R.id.indicator) as ScrollingPagerIndicator
            val viewSharedTop = itemView.findViewById(R.id.viewSharedTop) as View
            val viewSharedLeft = itemView.findViewById(R.id.viewSharedLeft) as View
            val viewSharedRight = itemView.findViewById(R.id.viewSharedRight) as View
            val viewSharedBottom = itemView.findViewById(R.id.viewSharedBottom) as View

            if (adapterPosition % 5 == 0) {
                mAd.visibility = View.VISIBLE
                UtilsClass.loadAd(mAdView)
            } else {
                mAd.visibility = View.GONE
            }
            if (dataX.userId.toString().equals(myUserId)) {
                ivOptionPic.visibility = View.VISIBLE
                ivSendGift.visibility = View.GONE
            } else {
                ivOptionPic.visibility = View.VISIBLE
                ivSendGift.visibility = View.VISIBLE
            }
            ivSendGift.setOnClickListener {
                callBack.sendGift(adapterPosition)
            }
            /*itemView.setOnClickListener {
                callBack.onHomePostItemClicked(adapterPosition)
            }*/
            val commentsRecyclerView =
                itemView.findViewById(R.id.commentsRecyclerView) as RecyclerView

            if (dataX.postLikesCount > 1) {
                tvTotalLikes.setText(dataX.postLikesCount.toString() + " Likes")
            } else {
                tvTotalLikes.setText(dataX.postLikesCount.toString() + " Like")
            }
            if (dataX.postComments.size > 1) {
                tvTotalComments.setText(dataX.postComments.size.toString() + " Comments")
            } else {
                tvTotalComments.setText(dataX.postComments.size.toString() + " Comment")
            }
            tvTotalShares.setText(dataX.no_of_shared_count.toString() + " Share")
            tvPostTime.text = dataX.createdAt
            if (dataX.type.equals("profile_cover_photo", true)) {
                if (dataX.user.gender.equals("Male", true)) {
                    tvUsername.text =
                        dataX.user.firstName + " " + dataX.user.lastName + " updated his cover photo"
                } else if (dataX.user.gender.equals("Female", true)) {
                    tvUsername.text =
                        dataX.user.firstName + " " + dataX.user.lastName + " updated her cover photo"

                } else {
                    tvUsername.text =
                        dataX.user.firstName + " " + dataX.user.lastName + " updated cover photo"
                }


            } else if (dataX.type.equals("profile_photo", true)) {
                if (dataX.user.gender.equals("Male", true)) {
                    tvUsername.text =
                        dataX.user.firstName + " " + dataX.user.lastName + " updated his profile photo"
                } else if (dataX.user.gender.equals("Female", true)) {
                    tvUsername.text =
                        dataX.user.firstName + " " + dataX.user.lastName + " updated her profile photo"

                } else {
                    tvUsername.text =
                        dataX.user.firstName + " " + dataX.user.lastName + " updated profile photo"
                }

            } else {
                if (dataX.tagged_users_list != null && dataX.tagged_users_list.size > 0) {
                    var text = ""
                    if (dataX.tagged_users_list!!.size > 0 && dataX.tagged_users_list!!.get(0).user != null) {
                        if (dataX.tagged_users_list!!.size == 1) {
                            text =
                                text + "<b>" + dataX.user.firstName + " " + dataX.user.lastName + "</b>" + " is with " + "<b>" + dataX.tagged_users_list!!.get(
                                    0
                                ).user.firstName + " " + dataX.tagged_users_list!!.get(0).user.lastName + "</b>"
                            if (dataX.location != null) {
                                text =
                                    text + " in " + "<b>" + dataX.location + "</b> "
                                var uname = CommonMethods.fromHtml(text)?.split("is with")
                                var oname = CommonMethods.fromHtml(text)?.split(" in")
//
                                tvUsername.setOnTouchListener { v, event ->
                                    if (event.action === MotionEvent.ACTION_DOWN) {
                                        val layout: Layout? = (v as TextView).layout
                                        val x = event.x as Float
                                        val y = event.y as Float
                                        if (layout != null) {
                                            val line: Int =
                                                layout.getLineForVertical(y.roundToInt())
                                            val offset: Int = layout.getOffsetForHorizontal(line, x)
                                            Log.v("index", "" + offset)
                                            if (offset < uname?.get(0)!!.length) {
                                                ivUserPicture.performClick()

                                            } else if (offset > uname?.get(0)!!.length && offset < oname?.get(
                                                    0
                                                )!!.length
                                            ) {
                                                callBack.openFriendProfile(adapterPosition)

                                            }
                                        }
                                    }
                                    true
                                }
                            } else {
                                var uname = CommonMethods.fromHtml(text)?.split("is with")
//
                                tvUsername.setOnTouchListener { v, event ->
                                    if (event.action === MotionEvent.ACTION_DOWN) {
                                        val layout: Layout? = (v as TextView).layout
                                        val x = event.x as Float
                                        val y = event.y as Float
                                        if (layout != null) {
                                            val line: Int =
                                                layout.getLineForVertical(y.roundToInt())
                                            val offset: Int = layout.getOffsetForHorizontal(line, x)
                                            Log.v("index", "" + offset)
                                            if (offset < uname?.get(0)!!.length) {
                                                ivUserPicture.performClick()

                                            } else if (offset > uname?.get(0)!!.length
                                            ) {
                                                callBack.openFriendProfile(adapterPosition)

                                            }
                                        }
                                    }
                                    true
                                }
                            }
                        } else {
                            text =
                                text + "<b>" + dataX.user.firstName + " " + dataX.user.lastName + "</b>" + " is with " + "<b>" + dataX.tagged_users_list!!.get(
                                    0
                                ).user.firstName!! + " " + dataX.tagged_users_list!!.get(0).user.lastName + "</b>" + " and " + "<b>" + dataX.tagged_users_list!!.size.minus(
                                    1
                                ) + " other" + "</b>"
                            if (dataX.location != null) {
                                text =
                                    text + " in " + "<b>" + dataX.location + "</b> "
                                var uname = CommonMethods.fromHtml(text)?.split("is with")
                                var pname = CommonMethods.fromHtml(text)?.split("and ")
                                var oname = CommonMethods.fromHtml(text)?.split(" in")
//
                                tvUsername.setOnTouchListener { v, event ->
                                    if (event.action === MotionEvent.ACTION_DOWN) {
                                        val layout: Layout? = (v as TextView).layout
                                        val x = event.x as Float
                                        val y = event.y as Float
                                        if (layout != null) {
                                            val line: Int = layout.getLineForVertical(y.roundToInt())
                                            val offset: Int = layout.getOffsetForHorizontal(line, x)
                                            Log.v("index", "" + offset)
                                            if (offset < uname?.get(0)!!.length) {
                                                ivUserPicture.performClick()
                                            } else if (offset > uname?.get(0)!!.length && offset < pname?.get(
                                                    0
                                                )!!.length
                                            ) {
                                                callBack.openFriendProfile(adapterPosition)
                                                callBack.openTaggedList(adapterPosition)
                                            } else if (offset > pname?.get(0)!!.length && offset < oname?.get(
                                                    0
                                                )!!.length
                                            ) {
                                                callBack.openTaggedList(adapterPosition)
                                            }
                                        }
                                    }
                                    true
                                }  }else{
                                var uname = CommonMethods.fromHtml(text)?.split("is with")
                                var pname = CommonMethods.fromHtml(text)?.split("and ")
//
                                tvUsername.setOnTouchListener { v, event ->
                                    if (event.action === MotionEvent.ACTION_DOWN) {
                                        val layout: Layout? = (v as TextView).layout
                                        val x = event.x as Float
                                        val y = event.y as Float
                                        if (layout != null) {
                                            val line: Int = layout.getLineForVertical(y.roundToInt())
                                            val offset: Int = layout.getOffsetForHorizontal(line, x)
                                            Log.v("index", "" + offset)
                                            if (offset < uname?.get(0)!!.length) {
                                                ivUserPicture.performClick()
                                            } else if (offset > uname?.get(0)!!.length && offset < pname?.get(
                                                    0
                                                )!!.length
                                            ) {
                                                callBack.openFriendProfile(adapterPosition)
                                            } else if (offset > pname?.get(0)!!.length
                                            ) {
                                                callBack.openTaggedList(adapterPosition)
                                            }
                                        }
                                    }
                                    true
                                }
                            }

                        }
                        // tvTaggedLocation.setText("- with")
                    }
                    // text=  "<b>" + text + "</b> "
                    tvUsername.setText(CommonMethods.fromHtml(text))
                } else {
                    var text = ""
                    if (dataX.location != null) {
                        text =
                            "<b>" + text + dataX.user.firstName + " " + dataX.user.lastName + "</b>" + " in " + "<b>" + dataX.location + "</b>"
                    } else {
                        text =
                            "<b>" + dataX.user.firstName + " " + dataX.user.lastName + "</b>"
                    }
                    tvUsername.setText(CommonMethods.fromHtml(text))
                    /*tvUsername.text =
                        text*/
                    tvUsername.setOnClickListener {
                        ivUserPicture.performClick()
                    }
                }
            }
            // tvUsername.text = dataX.user.firstName + " " + dataX.user.lastName
            if (dataX.content != null && !dataX.content.equals("")) {
                tvPostTitle.text = dataX.content
                tvPostTitle.visibility = View.VISIBLE
            } else {
                tvPostTitle.visibility = View.GONE
            }

            if (dataX.user != null) {
                CommonMethods.setUserImage(ivUserPicture, baseUrl + dataX.user.profilePhoto)
            }
            if (dataX.isShared == 1) {
                tvOtherPostTime.visibility = View.VISIBLE
                tvOtherUsername.visibility = View.VISIBLE
                tvOtherPostTitle.visibility = View.VISIBLE
                ivOtherUserPicture.visibility = View.VISIBLE
                viewSharedTop.visibility = View.VISIBLE
                viewSharedLeft.visibility = View.VISIBLE
                viewSharedRight.visibility = View.VISIBLE
                viewSharedBottom.visibility = View.VISIBLE
                if (dataX.sharedUser != null) {
                    tvOtherPostTime.text = dataX.sharedUser.created_at
                    if (dataX.sharedUser.type.equals("profile_cover_photo", true)) {
                        if (dataX.sharedUser.user.gender.equals("Male", true)) {
                            tvOtherUsername.text =
                                dataX.sharedUser.user.firstName + " " + dataX.sharedUser.user.lastName + " updated his cover photo"
                        } else if (dataX.sharedUser.user.gender.equals("Female", true)) {
                            tvOtherUsername.text =
                                dataX.sharedUser.user.firstName + " " + dataX.sharedUser.user.lastName + " updated her cover photo"

                        } else {
                            tvOtherUsername.text =
                                dataX.sharedUser.user.firstName + " " + dataX.sharedUser.user.lastName + " updated cover photo"
                        }
                    } else if (dataX.sharedUser.type.equals("profile_photo", true)) {
                        if (dataX.sharedUser.user.gender.equals("Male", true)) {
                            tvOtherUsername.text =
                                dataX.sharedUser.user.firstName + " " + dataX.sharedUser.user.lastName + " updated his profile photo"
                        } else if (dataX.sharedUser.user.gender.equals("Female", true)) {
                            tvOtherUsername.text =
                                dataX.sharedUser.user.firstName + " " + dataX.sharedUser.user.lastName + " updated her profile photo"

                        } else {
                            tvOtherUsername.text =
                                dataX.sharedUser.user.firstName + " " + dataX.sharedUser.user.lastName + " updated profile photo"
                        }
                    } else {
                        tvOtherUsername.text =
                            dataX.sharedUser.user.firstName + " " + dataX.sharedUser.user.lastName
                    }


                    tvOtherPostTitle.text = dataX.sharedUser.content
                    if (dataX.user != null) {
                        CommonMethods.setUserImage(
                            ivOtherUserPicture,
                            baseUrl + dataX.sharedUser.user.profilePhoto
                        )
                    }
                    if (dataX.sharedUser.postMediumArrayList.size > 0) {
                        viewPager.visibility = View.GONE
                        recyclerViewHorizontalHomeImages.visibility = View.VISIBLE
                        // val adapter = HomePostImagesViewPager(context, dataX.postMedia, baseUrl)
                        //viewPager.adapter = adapter
                        // dots_indicator.setViewPager(viewPager)
                        var adapter =
                            HorizontalPostsImagesRecyclerAdapter(
                                context,
                                dataX.sharedUser.postMediumArrayList,
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
                        if (dataX.sharedUser.postMediumArrayList.size == 1) {
                            //dots_indicator.visibility = View.GONE
                            indicator.visibility = View.GONE
                        } else {
                            // dots_indicator.visibility = View.GONE
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
                viewSharedTop.visibility = View.GONE
                viewSharedLeft.visibility = View.GONE
                viewSharedRight.visibility = View.GONE
                viewSharedBottom.visibility = View.GONE
                if (dataX.postMedia.size > 0) {
                    viewPager.visibility = View.GONE
                    recyclerViewHorizontalHomeImages.visibility = View.VISIBLE
                    // val adapter = HomePostImagesViewPager(context, dataX.postMedia, baseUrl)
                    //viewPager.adapter = adapter
                    // dots_indicator.setViewPager(viewPager)
                    var adapter =
                        HorizontalPostsImagesRecyclerAdapter(context, dataX.postMedia, baseUrl)
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
                    if (dataX.postMedia.size == 1) {
                        //dots_indicator.visibility = View.GONE
                        indicator.visibility = View.GONE
                    } else {
                        //dots_indicator.visibility = View.GONE
                        indicator.visibility = View.VISIBLE
                    }
                    // CommonMethods.setImage(ivPostPicture, baseUrl + dataX.post_media.get(0).file_path)
                } else {
                    recyclerViewHorizontalHomeImages.visibility = View.GONE
                    viewPager.visibility = View.GONE
                    indicator.visibility = View.GONE
                    //dots_indicator.visibility = View.GONE
                    //ivPostPicture.setImageResource(R.drawable.place_holder_image)
                }
            }

            commentsRecyclerView.layoutManager = LinearLayoutManager(context)
            commentsRecyclerView.adapter =
                HomePostCommentsAdapter(dataX.postComments, context, baseUrl)

            if (dataX.isLikedCount != null) {
                if (dataX.isLikedCount == 1) {
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
//
            tvTotalComments.setOnClickListener {
                callBack.openCommentsScreen(adapterPosition)
            }
            tvTotalLikes.setOnClickListener {
                callBack.openLikes(adapterPosition)
            }
            ivOptionPic.setOnClickListener {
                if (dataX.userId.toString().equals(myUserId)) {
                    val popup = PopupMenu(context, ivOptionPic)
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.user_post_menu, popup.getMenu())
                    if (dataX.isShared == 1) {
                        popup.getMenu().findItem(R.id.editPost).setVisible(true);
                    } else {
                        if (dataX.type.equals("profile_cover_photo", true)) {
                            popup.getMenu().findItem(R.id.editPost).setVisible(false);
                        } else if (dataX.type.equals("profile_photo", true)) {
                            popup.getMenu().findItem(R.id.editPost).setVisible(false);
                        } else {
                            popup.getMenu().findItem(R.id.editPost).setVisible(true);
                        }


                    }
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(item: MenuItem): Boolean {
                            if (item.itemId == R.id.deletePost) {
                                callBack.onPostDeleted(adapterPosition)
                            } else if (item.itemId == R.id.editPost) {
                                if (dataX.isShared == 1) {
                                    if (dataX.content == null) {
                                        callBack.editSharedPost(adapterPosition, "")
                                    } else {
                                        callBack.editSharedPost(adapterPosition, dataX.content)
                                    }
                                } else {
                                    callBack.onHomePostEditClicked(adapterPosition)
                                }

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
                            } else if (item.itemId == R.id.hidePost) {
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}