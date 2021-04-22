package com.meetfriend.app.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.homeposts.ChildComments
import com.meetfriend.app.responseclasses.homeposts.Datum
import com.meetfriend.app.ui.activities.FullScreenActivity
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.activities.VideoPlayActivity
import com.meetfriend.app.ui.adapters.HomePostsCommentsNewAdapter
import com.meetfriend.app.ui.adapters.HorizontalPostsImagesRecyclerAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.RecyclerItemClickListener
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_post_details.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PostDetailsFragment : BaseFragment(),
    HomePostsCommentsNewAdapter.HomePostCommentsAdapterAdapterCallback {
    private var post_id = -1
    private var clickedPosition = -1
    private var homePostList: ArrayList<Datum>? =
        ArrayList()
    private var homeViewModal: HomeViewModal? = null
    private var baseUrl = ""
    private var commentsAdapter: HomePostsCommentsNewAdapter? = null
    private var postLikedDisliked = ""
    private var showPicsOrNot = "false"
    private var ff = -1
    private var replyCommentClickedPosition = -1
    private var deleteCommentPositionSelected = -1
    private var editCommentPositionSelected = -1
    private var replyPosition = -1

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_post_details, parent, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (ff == -1) {
            (activity as HomeActivity).showAndHideBottomNavigation(true)
            (activity as HomeActivity).toolbar!!.visibility = View.VISIBLE
        }
//        (activity as HomeActivity).ivBackIcon!!.visibility = View.GONE
    }
//    fun newInstance(address: String?): PostDetailsFragment? {
//        val myFragment = PostDetailsFragment()
//        val args = Bundle()
//        args.putString("from", address)
//        myFragment.setArguments(args)
//        return myFragment
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        (activity as HomeActivity).ivBackIcon!!.visibility = View.VISIBLE
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)

        initializeObservers()

        clickedPosition = this.requireArguments().getInt("position", 0)
        if (clickedPosition != -1) {
            Handler().postDelayed({
                (activity as HomeActivity).showAndHideBottomNavigation(false)
                (activity as HomeActivity).toolbar!!.visibility = View.GONE
            }, 300)
            post_id = this.requireArguments().getInt("post_id", 0)
            homePostList = this.requireArguments().getParcelableArrayList("list")
            baseUrl = this.requireArguments().getString("baseUrl", "")
            showPicsOrNot = this.requireArguments().getString("showPicsOrNot", "")
            setData()
        } else {
            ff = this.requireArguments().getInt("ff", 0)
            showPicsOrNot = "true"
            post_id = this.requireArguments().getInt("post_id", 0)
            initialObservers()
            getHomeListing()
        }

        ivSharePost.setOnClickListener { showDialog(clickedPosition) }
    }

    private fun initialObservers() {
        homeViewModal?.homePostsResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    println("Data recieved")
                    homePostList = it.result.data
                    for (i in 0 until homePostList!!.size) {
                        if (post_id == homePostList!![i].id) {
                            clickedPosition = i
                            setData()
                        }
                    }
                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
    }

    private fun getHomeListing() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        mHashMap["search"] = ""
        homeViewModal?.homePosts(mHashMap)

    }

    fun showDialog(position: Int) {
        var postPrivacySpinnerValue = ""
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.share_post_dialog)
        // dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var ivUserPicture: ImageView = dialog.findViewById(R.id.ivUserPicture)
        CommonMethods.setUserImage(ivUserPicture, getProfilePic())
        var tvUsername: TextView = dialog.findViewById(R.id.tvUsername)
        tvUsername.setText(getUserName())
        var postPrivacySpinner: Spinner = dialog.findViewById(R.id.postPrivacySpinner)
        postPrivacySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                position: Int,
                id: Long
            ) {
                postPrivacySpinnerValue = postPrivacySpinner.getSelectedItem().toString()
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {

            }
        })
        var etStoryText: EditText = dialog.findViewById(R.id.etStoryText)
        etStoryText.isActivated = true
        etStoryText.isPressed = true
        var tvPostStory: TextView = dialog.findViewById(R.id.tvPostStory)
        tvPostStory.setOnClickListener {
            sharePostApi(position, postPrivacySpinnerValue, etStoryText.text.toString().trim())
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sharePostApi(
        position: Int,
        postPrivacySpinnerValue: String,
        content: String
    ) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        if (postPrivacySpinnerValue.equals("Public", true)) {
            mHashMap["privacy"] = "1"
        } else if (postPrivacySpinnerValue.equals("Friends", true)) {
            mHashMap["privacy"] = "2"
        } else if (postPrivacySpinnerValue.equals("FOF", true)) {
            mHashMap["privacy"] = "3"
        }
        mHashMap["content"] = content
        homeViewModal?.postShare(mHashMap)
    }

    private fun setData() {
        tvPostTime.text = homePostList!!.get(clickedPosition).createdAt
        if (homePostList!!.get(clickedPosition).type.equals("profile_cover_photo", true)) {
            tvUsername.text =
                homePostList!!.get(clickedPosition).user.firstName + " " + homePostList!!.get(
                    clickedPosition
                ).user.lastName + " updated cover photo"
        } else if (homePostList!!.get(clickedPosition).type.equals("profile_cover_photo", true)) {
            tvUsername.text =
                homePostList!!.get(clickedPosition).user.firstName + " " + homePostList!!.get(
                    clickedPosition
                ).user.lastName + " updated profile photo"
        } else {
            if (homePostList!!.get(clickedPosition).tagged_users_list != null && homePostList!!.get(
                    clickedPosition
                ).tagged_users_list.size > 0
            ) {
                var text = ""
                if (homePostList!!.get(clickedPosition).tagged_users_list!!.size > 0 && homePostList!!.get(
                        clickedPosition
                    ).tagged_users_list!!.get(0).user != null
                ) {
                    if (homePostList!!.get(clickedPosition).tagged_users_list!!.size == 1) {
                        text =
                            text + homePostList!!.get(clickedPosition).user.firstName + " " + homePostList!!.get(
                                clickedPosition
                            ).user.lastName + " is with " + homePostList!!.get(clickedPosition).tagged_users_list!!.get(
                                0
                            ).user.firstName + " " + homePostList!!.get(clickedPosition).tagged_users_list!!.get(
                                0
                            ).user.lastName
                    } else {
                        text =
                            text + homePostList!!.get(clickedPosition).user.firstName + " " + homePostList!!.get(
                                clickedPosition
                            ).user.lastName + " is with " + homePostList!!.get(clickedPosition).tagged_users_list!!.get(
                                0
                            ).user.firstName!! + " " + homePostList!!.get(clickedPosition).tagged_users_list!!.get(
                                0
                            ).user.lastName + " and " + homePostList!!.get(clickedPosition).tagged_users_list!!.size.minus(
                                1
                            ) + " other"

                    }
                    // tvTaggedLocation.setText("- with")
                }
                if (homePostList!!.get(clickedPosition).location != null) {
                    text =
                        text + " in " + homePostList!!.get(clickedPosition).location
                }
                tvUsername.text = text
            } else {
                var text = ""
                if (homePostList!!.get(clickedPosition).location != null) {
                    text =
                        text + homePostList!!.get(clickedPosition).user.firstName + " " + homePostList!!.get(
                            clickedPosition
                        ).user.lastName + " in " + homePostList!!.get(clickedPosition).location
                } else {
                    text =
                        homePostList!!.get(clickedPosition).user.firstName + " " + homePostList!!.get(
                            clickedPosition
                        ).user.lastName
                }
                tvUsername.text =
                    text
            }

        }
        // tvUsername.text = dataX.user.firstName + " " + dataX.user.lastName
        tvPostTitle.text = homePostList!!.get(clickedPosition).content
        if (homePostList!!.get(clickedPosition).user != null) {
            CommonMethods.setUserImage(
                ivUserPicture,
                baseUrl + homePostList!!.get(clickedPosition).user.profilePhoto
            )
        }
        if (homePostList!!.get(clickedPosition).isShared == 1) {
            tvOtherPostTime.visibility = View.VISIBLE
            tvOtherUsername.visibility = View.VISIBLE
            tvOtherPostTitle.visibility = View.VISIBLE
            ivOtherUserPicture.visibility = View.VISIBLE
            //viewotherComment.visibility = View.GONE
            viewSharedTop.visibility = View.VISIBLE
            viewSharedLeft.visibility = View.VISIBLE
            viewSharedRight.visibility = View.VISIBLE
            viewSharedBottom.visibility = View.VISIBLE
            if (homePostList!!.get(clickedPosition).sharedUser != null) {
                tvOtherPostTime.text = homePostList!!.get(clickedPosition).sharedUser.created_at
                if (homePostList!!.get(clickedPosition).sharedUser.type.equals(
                        "profile_cover_photo",
                        true
                    )
                ) {
                    tvOtherUsername.text =
                        homePostList!!.get(clickedPosition).sharedUser.user.firstName + " " + homePostList!!.get(
                            clickedPosition
                        ).sharedUser.user.lastName + " updated cover photo"
                } else if (homePostList!!.get(clickedPosition).sharedUser.type.equals(
                        "profile_cover_photo",
                        true
                    )
                ) {
                    tvOtherUsername.text =
                        homePostList!!.get(clickedPosition).sharedUser.user.firstName + " " + homePostList!!.get(
                            clickedPosition
                        ).sharedUser.user.lastName + " updated profile photo"
                } else {
                    tvOtherUsername.text =
                        homePostList!!.get(clickedPosition).sharedUser.user.firstName + " " + homePostList!!.get(
                            clickedPosition
                        ).sharedUser.user.lastName
                }


                tvOtherPostTitle.text = homePostList!!.get(clickedPosition).sharedUser.content
                if (homePostList!!.get(clickedPosition).user != null) {
                    CommonMethods.setUserImage(
                        ivOtherUserPicture,
                        baseUrl + homePostList!!.get(clickedPosition).sharedUser.user.profilePhoto
                    )
                }
                if (homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList.size > 0 && showPicsOrNot.equals(
                        "true",
                        true
                    )
                ) {
                    //  viewPager.visibility = View.GONE
                    recyclerViewHorizontalHomeImages.visibility = View.VISIBLE
                    // val adapter = HomePostImagesViewPager(context, dataX.postMedia, baseUrl)
                    //viewPager.adapter = adapter
                    // dots_indicator.setViewPager(viewPager)
                    var adapter =
                        HorizontalPostsImagesRecyclerAdapter(
                            requireContext(),
                            homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList,
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
                                if (homePostList!!.get(clickedPosition).isShared == 1) {
                                    if (homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList.get(
                                            childPosition
                                        ).extension.equals(
                                            "mov",
                                            true
                                        ) || homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList.get(
                                            childPosition
                                        ).extension.equals(
                                            "mp4",
                                            true
                                        ) || homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList.get(
                                            childPosition
                                        ).extension.equals(
                                            "3gp",
                                            true
                                        ) || homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList.get(
                                            childPosition
                                        ).extension.equals(
                                            "mpeg",
                                            true
                                        )
                                    ) {
                                        val intent =
                                            Intent(requireActivity(), VideoPlayActivity::class.java)
                                        intent.putExtra(
                                            "path",
                                            baseUrl + homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList.get(
                                                childPosition
                                            ).filePath
                                        )
                                        requireActivity().startActivity(intent)
                                    } else {
                                        val intent = Intent(
                                            requireActivity(),
                                            FullScreenActivity::class.java
                                        )
                                        intent.putExtra(
                                            "url",
                                            baseUrl + homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList.get(
                                                childPosition
                                            ).filePath
                                        )
                                        requireActivity().startActivity(intent)

                                    }
                                } else {
                                    if (homePostList!!.get(clickedPosition).postMedia.get(
                                            childPosition
                                        ).extension.equals(
                                            "mov",
                                            true
                                        ) || homePostList!!.get(clickedPosition).postMedia.get(
                                            childPosition
                                        ).extension.equals(
                                            "mp4",
                                            true
                                        ) || homePostList!!.get(clickedPosition).postMedia.get(
                                            childPosition
                                        ).extension.equals(
                                            "3gp",
                                            true
                                        ) || homePostList!!.get(clickedPosition).postMedia.get(
                                            childPosition
                                        ).extension.equals(
                                            "mpeg",
                                            true
                                        )
                                    ) {
                                        val intent =
                                            Intent(requireActivity(), VideoPlayActivity::class.java)
                                        intent.putExtra(
                                            "path",
                                            baseUrl + homePostList!!.get(clickedPosition).postMedia.get(
                                                childPosition
                                            ).filePath
                                        )
                                        requireActivity().startActivity(intent)
                                    } else {
                                        val intent = Intent(
                                            requireActivity(),
                                            FullScreenActivity::class.java
                                        )
                                        intent.putExtra(
                                            "url",
                                            baseUrl + homePostList!!.get(clickedPosition).postMedia.get(
                                                childPosition
                                            ).filePath
                                        )
                                        requireActivity().startActivity(intent)

                                    }
                                }
                                /*  if (homePostList!!.get(clickedPosition).postMedia.get(childPosition).extension.equals(
                                          "mp4",
                                          true
                                      ) || homePostList!!.get(clickedPosition).postMedia.get(
                                          childPosition
                                      ).extension.equals(
                                          "3gp",
                                          true
                                      ) || homePostList!!.get(clickedPosition).postMedia.get(
                                          childPosition
                                      ).extension.equals(
                                          "mpeg",
                                          true
                                      )
                                  ) {
                                      val intent =
                                          Intent(requireActivity(), VideoPlayActivity::class.java)
                                      intent.putExtra(
                                          "path",
                                          baseUrl + homePostList!!.get(clickedPosition).postMedia.get(
                                              childPosition
                                          ).filePath
                                      )
                                      requireActivity().startActivity(intent)
                                  } else {
                                      val intent =
                                          Intent(requireActivity(), FullScreenActivity::class.java)
                                      intent.putExtra(
                                          "url",
                                          baseUrl + homePostList!!.get(clickedPosition).postMedia.get(
                                              childPosition
                                          ).filePath
                                      )
                                      requireActivity().startActivity(intent)
                                  }*/
                            })
                    )
                    indicator.attachToRecyclerView(recyclerViewHorizontalHomeImages)
                    if (homePostList!!.get(clickedPosition).sharedUser.postMediumArrayList.size == 1) {
                        // dots_indicator.visibility = View.GONE
                        indicator.visibility = View.GONE
                    } else {
                        //dots_indicator.visibility = View.GONE
                        indicator.visibility = View.VISIBLE
                    }
                    // CommonMethods.setImage(ivPostPicture, baseUrl + dataX.post_media.get(0).file_path)
                } else {
                    recyclerViewHorizontalHomeImages.visibility = View.GONE
                    //viewPager.visibility = View.GONE
                    //dots_indicator.visibility = View.GONE
                    //ivPostPicture.setImageResource(R.drawable.place_holder_image)
                }
            }

        } else {
            //viewotherComment.visibility = View.GONE
            viewSharedTop.visibility = View.GONE
            viewSharedLeft.visibility = View.GONE
            viewSharedRight.visibility = View.GONE
            viewSharedBottom.visibility = View.GONE
            if (homePostList!!.get(clickedPosition).postMedia.size > 0 && showPicsOrNot.equals(
                    "true",
                    true
                )
            ) {
                // viewPager.visibility = View.GONE
                recyclerViewHorizontalHomeImages.visibility = View.VISIBLE
                // val adapter = HomePostImagesViewPager(context, dataX.postMedia, baseUrl)
                //viewPager.adapter = adapter
                // dots_indicator.setViewPager(viewPager)
                var adapter =
                    HorizontalPostsImagesRecyclerAdapter(
                        requireActivity(),
                        homePostList!!.get(clickedPosition).postMedia,
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
                            //callBack.onHomePostItemClicked(adapterPosition, childPosition)
                            //callBack.onHomePostItemClickedChild(adapterPosition, childPosition)
                            // Log.e("@@@@@", "" + position)
                        })
                )
                indicator.attachToRecyclerView(recyclerViewHorizontalHomeImages);
                if (homePostList!!.get(clickedPosition).postMedia.size == 1) {
                    //  dots_indicator.visibility = View.GONE
                    indicator.visibility = View.GONE
                } else {
                    // dots_indicator.visibility = View.GONE
                    indicator.visibility = View.VISIBLE
                }
                // CommonMethods.setImage(ivPostPicture, baseUrl + dataX.post_media.get(0).file_path)
            } else {
                recyclerViewHorizontalHomeImages.visibility = View.GONE
                // viewPager.visibility = View.GONE
                indicator.visibility = View.GONE
                // dots_indicator.visibility = View.GONE
                //ivPostPicture.setImageResource(R.drawable.place_holder_image)
            }
        }

        commentsRecyclerView.layoutManager = LinearLayoutManager(context)
        commentsRecyclerView.setEmptyView(tvNoComments)
        commentsRecyclerView.isNestedScrollingEnabled = false
        Collections.reverse(homePostList!!.get(clickedPosition).postComments)
        commentsAdapter = HomePostsCommentsNewAdapter(
            homePostList!!.get(clickedPosition).postComments,
            requireActivity(),
            baseUrl, this, getUserId()
        )
        commentsRecyclerView.adapter = commentsAdapter
        commentsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        updateLikeAndDislikeButton()
        ivBackIcon!!.setOnClickListener { requireActivity().onBackPressed() }
        ivSendMessage.setOnClickListener {
            if (!etComment.text.toString().trim().equals("")) {
                if (editCommentPositionSelected != -1) {
                    updateCommentApi()
                } else {
                    addCommentApi()
                }

                //etComment.setText("")
            }
        }
        ivLikePost.setOnClickListener {
            likePost()
        }
        if (showPicsOrNot.equals("true", true)) {
            ivUserPicture.visibility = View.VISIBLE
            tvUsername.visibility = View.VISIBLE
            tvPostTime.visibility = View.VISIBLE
            tvPostTitle.visibility = View.VISIBLE
            //ivOtherUserPicture.visibility=View.VISIBLE
            //tvOtherUsername.visibility=View.VISIBLE
            //tvOtherPostTime.visibility=View.VISIBLE
            //tvOtherPostTitle.visibility=View.VISIBLE
            totalLayout.visibility = View.VISIBLE
            likeShareLayout.visibility = View.VISIBLE
            //recyclerViewHorizontalHomeImages.visibility=View.VISIBLE
        } else {
            viewcomments.visibility = View.GONE
            viewLikeShare.visibility = View.GONE
            viewBelow.visibility = View.GONE
            ivUserPicture.visibility = View.GONE
            tvUsername.visibility = View.GONE
            tvPostTime.visibility = View.GONE
            tvPostTitle.visibility = View.GONE
            ivOtherUserPicture.visibility = View.GONE
            tvOtherUsername.visibility = View.GONE
            tvOtherPostTime.visibility = View.GONE
            tvOtherPostTitle.visibility = View.GONE
            totalLayout.visibility = View.GONE
            likeShareLayout.visibility = View.GONE
            //recyclerViewHorizontalHomeImages.visibility=View.GONE
        }
    }

    fun updateLikeAndDislikeButton() {
        if (homePostList!!.get(clickedPosition).postLikesCount > 1) {
            tvTotalLikes.setText(homePostList!!.get(clickedPosition).postLikesCount.toString() + " Likes")
        } else {
            tvTotalLikes.setText(homePostList!!.get(clickedPosition).postLikesCount.toString() + " Like")
        }
        if (homePostList!!.get(clickedPosition).postComments.size > 1) {
            tvTotalComments.setText(homePostList!!.get(clickedPosition).postComments.size.toString() + " Comments")
        } else {
            tvTotalComments.setText(homePostList!!.get(clickedPosition).postComments.size.toString() + " Comment")
        }

        tvTotalShares.setText(homePostList!!.get(clickedPosition).no_of_shared_count.toString() + " Share")
        if (homePostList!!.get(clickedPosition).isLikedCount != null) {
            if (homePostList!!.get(clickedPosition).isLikedCount == 1) {
                ivLikePost.setImageResource(R.drawable.ic_post_liked_red_heart)
            } else {
                ivLikePost.setImageResource(R.drawable.heart_icon_outline)
            }
        }
    }

    fun likePost() {
        if (homePostList!!.get(clickedPosition).postLikesCount == 1) {
            postLikedDisliked = "unlike"
        } else {
            postLikedDisliked = "like"
        }
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(clickedPosition).id
        mHashMap["post_status"] = postLikedDisliked
        homeViewModal?.postLikeUnlike(mHashMap)
    }

    private fun addCommentApi() {
        if (replyCommentClickedPosition != -1) {
            if (replyPosition == -1) {
                val mHashMap = HashMap<String, Any>()
                mHashMap["post_id"] = homePostList!!.get(clickedPosition).id
                mHashMap["content"] = etComment.text.toString()
                mHashMap["parent_id"] =
                    homePostList!!.get(clickedPosition).postComments.get(replyCommentClickedPosition).id
                homeViewModal?.replyComment(mHashMap)
            } else {
                val mHashMap = HashMap<String, Any>()
                mHashMap["post_id"] = homePostList!!.get(clickedPosition).id
                mHashMap["content"] = etComment.text.toString()
                mHashMap["parent_id"] =
                    homePostList!!.get(clickedPosition).postComments.get(replyCommentClickedPosition).child_comments.get(
                        replyPosition
                    ).id
                homeViewModal?.replyComment(mHashMap)
            }

        } else {
            val mHashMap = HashMap<String, Any>()
            mHashMap["post_id"] = homePostList!!.get(clickedPosition).id
            mHashMap["content"] = etComment.text.toString()
            homeViewModal?.commentPost(mHashMap)
        }
    }

    private fun updateCommentApi() {
        if (replyPosition == -1) {
            val mHashMap = HashMap<String, Any>()
            mHashMap["comment_id"] =
                homePostList!!.get(clickedPosition).postComments.get(editCommentPositionSelected).id
            mHashMap["content"] = etComment.text.toString()
            homeViewModal?.updateComment(mHashMap)
        } else {
            val mHashMap = HashMap<String, Any>()
            mHashMap["comment_id"] =
                homePostList!!.get(clickedPosition).postComments.get(editCommentPositionSelected).child_comments.get(
                    replyPosition
                ).id
            mHashMap["content"] = etComment.text.toString()
            homeViewModal?.updateComment(mHashMap)
        }

    }

    private fun initializeObservers() {
        homeViewModal?.commentPostResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    homePostList!!.get(clickedPosition).postComments.add(it.result)
                    commentsAdapter!!.notifyDataSetChanged()
                    etComment.setText("")
                    updateLikeAndDislikeButton()
                    commentsRecyclerView.scrollToPosition(homePostList!!.get(clickedPosition).postComments.size - 1)
//                    postCommentedPosition = -1
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.postLikeUnlikeResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    if (postLikedDisliked.equals("like")) {
                        homePostList!!.get(clickedPosition).isLikedCount = 1
                        homePostList!!.get(clickedPosition).postLikesCount =
                            homePostList!!.get(clickedPosition).postLikesCount + 1
                    } else {
                        homePostList!!.get(clickedPosition).isLikedCount = 0
                        homePostList!!.get(clickedPosition).postLikesCount =
                            homePostList!!.get(clickedPosition).postLikesCount - 1
                    }
                    updateLikeAndDislikeButton()

                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.replyCommentResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    if (replyCommentClickedPosition != -1) {
                        var child: ChildComments = ChildComments()
                        child.content = it.result.content
                        child.id = it.result.id
                        child.postId = it.result.post_id
                        child.createdAt = it.result.created_at
                        child.userId = it.result.user_id
                        child.user = it.result.user
                        homePostList!!.get(clickedPosition).postComments.get(
                            replyCommentClickedPosition
                        ).child_comments
                            .add(child)
                        commentsAdapter!!.notifyDataSetChanged()
                        replyCommentClickedPosition = -1
                        etComment.setText("")
                    }
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.postShareResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.deleteCommentResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    if (deleteCommentPositionSelected != -1) {
                        if (replyPosition != -1) {
                            homePostList!!.get(clickedPosition).postComments.get(
                                deleteCommentPositionSelected
                            ).child_comments.removeAt(
                                replyPosition
                            )
                        } else {
                            homePostList!!.get(clickedPosition).postComments.removeAt(
                                deleteCommentPositionSelected
                            )
                        }

                        commentsAdapter!!.notifyDataSetChanged()
                        deleteCommentPositionSelected = -1
                        hideKeyboard()
                        hideKeyboard(requireActivity())
                        val inputManager =
                            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
                        inputManager!!.hideSoftInputFromWindow(etComment.windowToken, 0)
                    }


                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.updateCommentResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    if (editCommentPositionSelected != -1) {
                        if (replyPosition != -1) {
                            var child: ChildComments = ChildComments()
                            child.content = it.result.content
                            child.id = it.result.id
                            child.postId = it.result.post_id
                            child.createdAt = it.result.created_at
                            child.userId = it.result.user_id
                            child.user = it.result.user
                            homePostList!!.get(clickedPosition).postComments.get(
                                editCommentPositionSelected
                            ).child_comments.set(
                                replyPosition,
                                child
                            )
                        } else {
                            homePostList!!.get(clickedPosition).postComments.set(
                                editCommentPositionSelected,
                                it.result
                            )
                        }

                        commentsAdapter!!.notifyDataSetChanged()
                        editCommentPositionSelected = -1
                        etComment.setText("")
                    }


                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        homeViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        homeViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
            }
        })

    }

    override fun onDeleteClick(position: Int, rposition: Int) {
        if (rposition == -1) {
            replyPosition = rposition
            deleteCommentPositionSelected = position
            val builder =
                AlertDialog.Builder(requireActivity())
            builder.setCancelable(false)
            builder.setMessage("Are you sure, you want to delete this comment?")
            builder.setPositiveButton(
                "Yes"
            ) { dialog, _ ->
                dialog.dismiss()
                val mHashMap = HashMap<String, Any>()
                mHashMap["comment_id"] =
                    homePostList!!.get(clickedPosition).postComments.get(position).id
                homeViewModal?.deleteComment(mHashMap)
            }
            builder.setNegativeButton(
                "No"
            ) { dialog, _ ->
                dialog.dismiss()
                deleteCommentPositionSelected = -1

            }
            val alert = builder.create()
            alert.show()
        } else {
            deleteCommentPositionSelected = position
            replyPosition = rposition
            val builder =
                AlertDialog.Builder(requireActivity())
            builder.setCancelable(false)
            builder.setMessage("Are you sure, you want to delete this comment?")
            builder.setPositiveButton(
                "Yes"
            ) { dialog, _ ->
                dialog.dismiss()
                val mHashMap = HashMap<String, Any>()
                mHashMap["comment_id"] =
                    homePostList!!.get(clickedPosition).postComments.get(position).child_comments.get(
                        rposition
                    ).id
                homeViewModal?.deleteComment(mHashMap)
            }
            builder.setNegativeButton(
                "No"
            ) { dialog, _ ->
                dialog.dismiss()
                deleteCommentPositionSelected = -1

            }
            val alert = builder.create()
            alert.show()
        }

    }

    override fun onReplyClicked(position: Int) {
        replyCommentClickedPosition = position
        editCommentPositionSelected = -1
        etComment.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT)
        /*val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(clickedPosition).id
        mHashMap["content"] = etComment.text.toString()
        mHashMap["parent_id"] = homePostList!!.get(clickedPosition).postComments.get(position).id
        homeViewModal?.replyComment(mHashMap)*/
    }

    override fun onEditCommentClicked(position: Int, rposition: Int) {
        if (rposition == -1) {
            replyPosition = rposition
            editCommentPositionSelected = position
            replyCommentClickedPosition = -1
            etComment.setText(homePostList!!.get(clickedPosition).postComments.get(position).content)
            etComment.setSelection(etComment.length())
            etComment.requestFocus()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm!!.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT)
        } else {
            editCommentPositionSelected = position
            replyPosition = rposition
            replyCommentClickedPosition = -1
            etComment.setText(
                homePostList!!.get(clickedPosition).postComments.get(position).child_comments.get(
                    rposition
                ).content
            )
            etComment.setSelection(etComment.length())
            etComment.requestFocus()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm!!.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}