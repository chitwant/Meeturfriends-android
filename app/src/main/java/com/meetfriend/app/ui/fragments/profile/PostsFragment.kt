package com.meetfriend.app.ui.fragments.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.network.ApiHelper
import com.meetfriend.app.responseclasses.addpost.AddPostResponse
import com.meetfriend.app.responseclasses.homeposts.Datum
import com.meetfriend.app.responseclasses.homeposts.HomePostsResponseClass
import com.meetfriend.app.responseclasses.homeposts.ParentComment
import com.meetfriend.app.ui.activities.GiftGalleryActivity
import com.meetfriend.app.ui.activities.VideoPlayActivity
import com.meetfriend.app.ui.adapters.HomePostsAdadpter
import com.meetfriend.app.ui.adapters.PostLikesAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.ErrorHandler
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_posts.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsFragment(val otherUserId: String, val type: String) : BaseFragment(),
    HomePostsAdadpter.HomePostsAdadpterAdapterCallback, PostLikesAdapter.AdapterCallback {
    //private var profileViewModal: ProfileViewModal? = null
    private var homeViewModal: HomeViewModal? = null
    private var baseUrl = ""
    private var postDeletedPosition = -1
    private var homePostsAdadpter: HomePostsAdadpter? = null
    private var homePostList: ArrayList<Datum>? =
        ArrayList()
    private var postCommentedPosition = -1
    private var postLikedDislikedPosition = -1
    private var postLikedDisliked = ""
    private var postHideOrReported = ""
    private var postReportedHiddenPosition = -1
    private var pictureClicked = false
    var likesPostDialog: Dialog? = null
    var reportDialog: Dialog? = null
    private var isFragmentVisible = false
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_posts, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        // profileViewModal = ViewModelProvider(this).get(ProfileViewModal::class.java)
        iniViews()
        initializeObservers()
        getHomeListing()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFragmentVisible = false
    }

    private fun getHomeListing() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        mHashMap["user_id"] = otherUserId
        homeViewModal?.userPosts(mHashMap)
    }

    private fun postDeleteApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(postDeletedPosition).id
        homeViewModal?.deletePost(mHashMap)
    }


    private fun initializeObservers() {
        homeViewModal?.userPostsResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        println("Data recieved")
                        setHomePostAdapter(it)
                    } else {
                        CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    }
                }

            }

        })
        homeViewModal!!.deletePostResponse.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    if (it.status) {
                        homePostList!!.removeAt(postDeletedPosition)
                        homePostsAdadpter!!.notifyDataSetChanged()

                    }
                }
            }
        })
        homeViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                //CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        homeViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                if (isFragmentVisible) {
                    showLoading(it)
                }

            }
        })
        homeViewModal?.postShareResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                        //getHomeListing()
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
                }

            }

        })
        homeViewModal?.reportOrHidePostResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    if (postHideOrReported.equals("hide", true)) {
                        if (postReportedHiddenPosition != -1) {
                            homePostList!!.removeAt(postReportedHiddenPosition)
                            homePostsAdadpter!!.notifyDataSetChanged()
                            postReportedHiddenPosition = -1
                        }
                    }
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.savePostRequestResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    //getHomeListing()
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.postLikeUnlikeResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    if (postLikedDislikedPosition != -1) {
                        if (postLikedDisliked.equals("like")) {
                            homePostList!!.get(postLikedDislikedPosition).isLikedCount = 1
                            homePostList!!.get(postLikedDislikedPosition).postLikesCount =
                                homePostList!!.get(postLikedDislikedPosition).postLikesCount + 1
                        } else {
                            homePostList!!.get(postLikedDislikedPosition).isLikedCount = 0
                            homePostList!!.get(postLikedDislikedPosition).postLikesCount =
                                homePostList!!.get(postLikedDislikedPosition).postLikesCount - 1
                        }
                        homePostsAdadpter!!.notifyDataSetChanged()
                        postLikedDislikedPosition = -1
                    }

                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                if (isFragmentVisible) {
                    CommonMethods.showToastMessageAtTop(
                        requireActivity(),
                        ApiFailureTypes().getFailureMessage(it, requireActivity())
                    )
                }

                /* CommonMethods.showSnackbarMessageWithoutColor(
                     requireActivity(),
                     ApiFailureTypes().getFailureMessage(it, requireActivity())
                 )*/
            }
        })
    }

    private fun setHomePostAdapter(it: HomePostsResponseClass) {
        if (it.result != null) {
            baseUrl = it.mediaUrl
            homePostList = it.result.data
            homePostsAdadpter =
                HomePostsAdadpter(homePostList!!, this, requireActivity(), it.mediaUrl, getUserId())
            recyclerViewHomePost.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = homePostsAdadpter
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                setEmptyView(tvNoPosts)
            }
            homePostsAdadpter!!.notifyDataSetChanged()
        }

    }

    private fun iniViews() {

    }


    override fun onItemLiked(position: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["type"] = "like"
        homeViewModal?.friendSuggestions(mHashMap)
    }

    override fun onPostReported(position: Int) {
        postReportedHiddenPosition = position
        postHideOrReported = "report"
        showReportDialog(position)
    }

    private fun showReportDialog(position: Int) {
        if (reportDialog == null) {
            reportDialog = Dialog(requireContext(), R.style.full_screen_dialog)
            //  dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            /* dialog!!.getWindow()!!
             .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)*/
            reportDialog!!.setContentView(R.layout.report_post_dialog)
            reportDialog!!.setCanceledOnTouchOutside(true)
            reportDialog!!.setCancelable(true)
        }

        reportDialog!!.show()
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        val height: Int = metrics.heightPixels
        reportDialog!!.getWindow()!!
            .setLayout((6 * width) / 7, RelativeLayout.LayoutParams.WRAP_CONTENT)
        val ivClose: ImageView = reportDialog!!.findViewById(R.id.ivClose) as ImageView
        val etReason: EditText = reportDialog!!.findViewById(R.id.etReason) as EditText
        val tvSendReport: TextView = reportDialog!!.findViewById(R.id.tvSendReport) as TextView
        etReason.visibility = View.GONE
        tvSendReport.visibility = View.GONE
        ivClose.setOnClickListener { reportDialog!!.dismiss() }
        val tvNudity: TextView = reportDialog!!.findViewById(R.id.tvNudity) as TextView
        tvNudity.setOnClickListener {
            reportPostApi(tvNudity.text.toString().trim(), position)
        }
        val tvViolence: TextView = reportDialog!!.findViewById(R.id.tvViolence) as TextView
        tvViolence.setOnClickListener {
            reportPostApi(tvViolence.text.toString().trim(), position)
        }
        val tvSpam: TextView = reportDialog!!.findViewById(R.id.tvSpam) as TextView
        tvSpam.setOnClickListener {
            reportPostApi(tvSpam.text.toString().trim(), position)
        }
        val tvFake: TextView = reportDialog!!.findViewById(R.id.tvFake) as TextView
        tvFake.setOnClickListener {
            reportPostApi(tvFake.text.toString().trim(), position)
        }
        val tvHarrasment: TextView = reportDialog!!.findViewById(R.id.tvHarrasment) as TextView
        tvHarrasment.setOnClickListener {
            reportPostApi(tvHarrasment.text.toString().trim(), position)
        }
        val tvhate: TextView = reportDialog!!.findViewById(R.id.tvhate) as TextView
        tvhate.setOnClickListener {
            reportPostApi(tvhate.text.toString().trim(), position)
        }
        val tvSuicide: TextView = reportDialog!!.findViewById(R.id.tvSuicide) as TextView
        tvSuicide.setOnClickListener {
            reportPostApi(tvSuicide.text.toString().trim(), position)
        }
        val tvTerrorism: TextView = reportDialog!!.findViewById(R.id.tvTerrorism) as TextView
        tvTerrorism.setOnClickListener {
            reportPostApi(tvTerrorism.text.toString().trim(), position)
        }
        val tvOther: TextView = reportDialog!!.findViewById(R.id.tvOther) as TextView
        tvOther.setOnClickListener {
            // reportPostApi(tvOther.text.toString().trim(),position)
            etReason.visibility = View.VISIBLE
            tvSendReport.visibility = View.VISIBLE
        }
        tvSendReport.setOnClickListener {
            if (etReason.text.toString().trim().equals("")) {
                etReason.error = "Type a reason to continue"
                return@setOnClickListener
            }
            etReason.error = null
            reportPostApi(etReason.text.toString().trim(), position)
        }
    }

    private fun reportPostApi(reason: String, position: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["type"] = "report"
        mHashMap["reason"] = reason
        homeViewModal?.reportOrHidePost(mHashMap)
        reportDialog!!.dismiss()
    }

    override fun onPostSaved(positiom: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(positiom).id
        homeViewModal?.savePost(mHashMap)
    }

    override fun onPostHidden(position: Int) {
        postReportedHiddenPosition = position
        postHideOrReported = "hide"
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["type"] = "hide"
        homeViewModal?.reportOrHidePost(mHashMap)
    }

    override fun postShared(position: Int) {
        showDialog(position)
    }

    override fun onCommentAdded(position: Int, comment: String) {
        addCommentApi(position, comment)
    }

    private fun addCommentApi(position: Int, comment: String) {
        postCommentedPosition = position
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["content"] = comment
        homeViewModal?.commentPost(mHashMap)
    }

    override fun likePost(position: Int) {
        postLikedDislikedPosition = position
        if (homePostList!!.get(position).postLikesCount == 1) {
            postLikedDisliked = "unlike"
        } else {
            postLikedDisliked = "like"
        }
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["post_status"] = postLikedDisliked
        homeViewModal?.postLikeUnlike(mHashMap)
    }

    /*  override fun onHomePostItemClicked(position: Int) {
          val bundle = bundleOf(
              "position" to position,
              "list" to homePostList,
              "post_id" to homePostList!!.get(position).id,
              "baseUrl" to baseUrl
          )
          findNavController().navigate(R.id.action_navigation_home_to_postDetailsFragment, bundle)
      }*/

    override fun onHomePostItemClicked(position: Int, childPosition: Int) {
        if (homePostList!!.get(position).postMedia.get(childPosition).extension.equals(
                "mov",
                true
            ) || homePostList!!.get(position).postMedia.get(childPosition).extension.equals(
                "mp4",
                true
            ) || homePostList!!.get(position).postMedia.get(childPosition).extension.equals(
                "3gp",
                true
            ) || homePostList!!.get(position).postMedia.get(childPosition).extension.equals(
                "mpeg",
                true
            )
        ) {
            val intent = Intent(requireActivity(), VideoPlayActivity::class.java)
            intent.putExtra(
                "path",
                baseUrl + homePostList!!.get(position).postMedia.get(childPosition).filePath
            )
            requireActivity().startActivity(intent)
        } else {
            if (!pictureClicked) {
                val bundle = bundleOf(
                    "position" to position,
                    "list" to homePostList,
                    "post_id" to homePostList!!.get(position).id,
                    "baseUrl" to baseUrl
                )
                findNavController().navigate(
                    R.id.action_profileFragment_to_postDetailsFragment,
                    bundle
                )
                pictureClicked = true
            }

        }
    }

    override fun onResume() {
        super.onResume()
        pictureClicked = false
    }

    override fun onHomePostEditClicked(position: Int) {
        var arrayList = ArrayList<String>()
        for (i in 0 until homePostList!!.get(position).postMedia.size) {
            arrayList.add(baseUrl + homePostList!!.get(position).postMedia.get(i).filePath)
        }
        val bundle = bundleOf(
            "post_id" to homePostList!!.get(position).id,
            "content" to homePostList!!.get(position).content,
            "privacy" to homePostList!!.get(position).privacy,
            "list" to arrayList,
            "main_list" to homePostList,
            "position_clicked" to position,
            "mediaUrl" to baseUrl

        )
        findNavController().navigate(R.id.action_profileFragment_to_editPostFragment2, bundle)

    }

    override fun onPostDeleted(position: Int) {
        postDeletedPosition = position
        postDeleteApi()
    }

    override fun openProfile(position: Int) {

    }

    override fun openCommentsScreen(position: Int) {
        openDetailsScreen(position, "true")
    }

    override fun onItemClicked(position: Int) {
        openDetailsScreen(position, "false")
    }

    private fun openDetailsScreen(position: Int, type: String) {
        val bundle = bundleOf(
            "position" to position,
            "list" to homePostList,
            "post_id" to homePostList!!.get(position).id,
            "baseUrl" to baseUrl,
            "showPicsOrNot" to type
        )
        findNavController().navigate(
            R.id.action_profileFragment_to_postDetailsFragment,
            bundle
        )
    }

    override fun openLikes(position: Int) {
        showLikesPostDialog(homePostList!!.get(position).post_likes)
    }

    override fun editSharedPost(position: Int, content: String) {
        showEditPostSharedDialog(position, content)
    }

    override fun sendGift(position: Int) {
        val intent = Intent(requireContext(), GiftGalleryActivity::class.java)
        intent.putExtra("from", "gift")
        intent.putExtra("post_id", homePostList?.get(position)?.id.toString())
        intent.putExtra("to_id", homePostList?.get(position)?.user?.id.toString())
        startActivity(intent)
    }

    override fun openFriendProfile(position: Int) {

        if (isFragmentVisible) {
            if (homePostList!!.get(position).tagged_users_list.get(0).user.id.equals(getUserId())) {
                val bundle = bundleOf(
                    "type" to "own",
                    "userId" to homePostList!!.get(position).tagged_users_list.get(0).user.id.toString()
                )
                findNavController().navigate(R.id.action_navigation_home_to_profileFragment, bundle)
            } else {
                val bundle = bundleOf(
                    "type" to "other",
                    "userId" to homePostList!!.get(position).tagged_users_list.get(0).user.id.toString()
                )
                findNavController().navigate(R.id.action_navigation_home_to_profileFragment, bundle)
            }
        }
    }

    override fun openTaggedList(position: Int) {

    }

    fun showEditPostSharedDialog(position: Int, content: String) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.share_post_dialog)
        // dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var ivUserPicture: ImageView = dialog.findViewById(R.id.ivUserPicture)
        CommonMethods.setUserImage(ivUserPicture, getProfilePic())
        var tvUsername: TextView = dialog.findViewById(R.id.tvUsername)
        tvUsername.setText(getUserName())
        var postPrivacySpinner: Spinner = dialog.findViewById(R.id.postPrivacySpinner)
        postPrivacySpinner.visibility = View.INVISIBLE
        var etStoryText: EditText = dialog.findViewById(R.id.etStoryText)
        etStoryText.isActivated = true
        etStoryText.isPressed = true
        etStoryText.setText(content)
        etStoryText.setSelection(etStoryText.getText().length);
        var tvPostStory: TextView = dialog.findViewById(R.id.tvPostStory)
        tvPostStory.setText("Save")
        tvPostStory.setOnClickListener {
            dialog.dismiss()
            editSharePostApi(position, etStoryText.text.toString().trim())
        }
        dialog.show()
    }

    private fun editSharePostApi(position: Int, content: String) {
        val post_id =
            RequestBody.create(
                MediaType.parse("text/plain"),
                homePostList!!.get(position).id.toString()
            )
        val contentBody =
            RequestBody.create(MediaType.parse("text/plain"), content)
        ApiHelper.createAppService()
            .editSharedPost(
                post_id,
                contentBody
            )!!
            .enqueue(object : Callback<AddPostResponse?> {
                override fun onResponse(
                    call: Call<AddPostResponse?>,
                    response: Response<AddPostResponse?>
                ) {
                    if (isFragmentVisible) {
                        showLoading(false)
                        CommonMethods.showToastMessageAtTop(
                            requireActivity(),
                            response.body()!!.message
                        )
                        getHomeListing()
                    }

                }

                override fun onFailure(
                    call: Call<AddPostResponse?>,
                    t: Throwable
                ) {
                    if (isFragmentVisible) {
                        showLoading(false)
                        CommonMethods.showToastMessageAtTop(
                            requireActivity(),
                            ErrorHandler.getMessage(t, requireActivity())
                        )
                    }

                }
            })

    }

    private fun showLikesPostDialog(postLikesList: ArrayList<ParentComment>) {
        if (likesPostDialog == null) {
            likesPostDialog = Dialog(requireContext(), R.style.full_screen_dialog)
            //  dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            /* dialog!!.getWindow()!!
                 .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)*/
            likesPostDialog!!.setContentView(R.layout.post_likes)
            likesPostDialog!!.setCanceledOnTouchOutside(true)
            likesPostDialog!!.setCancelable(true)
        }


        likesPostDialog!!.show()
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        val height: Int = metrics.heightPixels
        likesPostDialog!!.getWindow()!!
            .setLayout((6 * width) / 7, RelativeLayout.LayoutParams.WRAP_CONTENT);
        val ivClose: ImageView = likesPostDialog!!.findViewById(R.id.ivClose) as ImageView
        val tvNoFriends: TextView = likesPostDialog!!.findViewById(R.id.tvNoFriends) as TextView
        ivClose.setOnClickListener {
            likesPostDialog!!.dismiss()
        }
        val tagPeopleRV: com.meetfriend.app.utilclasses.RecyclerViewEmptySupport =
            likesPostDialog!!.findViewById(R.id.tagPeopleRV) as com.meetfriend.app.utilclasses.RecyclerViewEmptySupport
        tagPeopleRV.setHasFixedSize(true)
        tagPeopleRV.setLayoutManager(LinearLayoutManager(context))
        tagPeopleRV.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        tagPeopleRV.setEmptyView(tvNoFriends)
        //tagPeopleRV.addItemDecoration(SimpleDividerItemDecoration(context, R.drawable.divider))

        val rvAdapter = PostLikesAdapter(postLikesList!!, this, requireActivity(), baseUrl, "tag")
        tagPeopleRV.setAdapter(rvAdapter)
    }

    /*  override fun onPostDeleted(position: Int) {
          postDeletedPosition = position
          postDeleteApi()
      }*/
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
        if (homePostList!!.get(position).isShared == 1)
            mHashMap["post_id"] = homePostList!!.get(position).sharedUser.id
        else
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

    override fun onPostLikesUserClicked(position: Int) {

    }
}