package com.meetfriend.app.ui.home

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.modals.friendsuggestion.Data
import com.meetfriend.app.modals.friendsuggestion.SuggestionResponseClass
import com.meetfriend.app.network.ApiHelper
import com.meetfriend.app.responseclasses.InboxPOJO
import com.meetfriend.app.responseclasses.addpost.AddPostResponse
import com.meetfriend.app.responseclasses.homeposts.Datum
import com.meetfriend.app.responseclasses.homeposts.HomePostsResponseClass
import com.meetfriend.app.responseclasses.homeposts.ParentComment
import com.meetfriend.app.ui.activities.*
import com.meetfriend.app.ui.adapters.FriendAdapter
import com.meetfriend.app.ui.adapters.FriendSuggestionAdapter
import com.meetfriend.app.ui.adapters.HomePostsAdadpter
import com.meetfriend.app.ui.adapters.PostLikesAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.ErrorHandler
import com.meetfriend.app.utilclasses.GridItemDecoration
import com.meetfriend.app.utilclasses.UtilsClass
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import contractorssmart.app.utilsclasses.PreferenceHandler
import contractorssmart.app.utilsclasses.PreferenceHandler.writeString
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : BaseFragment(), FriendSuggestionAdapter.AdapterCallback,
    HomePostsAdadpter.HomePostsAdadpterAdapterCallback, PostLikesAdapter.AdapterCallback {
    private var rvAdapter: FriendAdapter? = null
    private var friendsList: ArrayList<com.meetfriend.app.responseclasses.friends.Data>? =
        ArrayList()

    private var baseUrl: String = ""
    private var homeViewModal: HomeViewModal? = null
    private var friendList: ArrayList<Data>? = ArrayList()
    private var friendSuggestionAdapter: FriendSuggestionAdapter? = null
    private var homePostsAdadpter: HomePostsAdadpter? = null
    private var friendSuggestionPosition = -1
    private var homePostList: ArrayList<Datum>? =
        ArrayList()
    private var mediaUrl = ""
    private var postCommentedPosition = -1
    private var postLikedDislikedPosition = -1
    private var postLikedDisliked = ""
    private var postHideOrReported = ""
    private var postReportedHiddenPosition = -1
    private var REQUEST_ID_MULTIPLE_PERMISSIONS = 1000
    var reportDialog: Dialog? = null
    private var isClicked = false
    private var pictureClicked = false
    private var postDeletedPosition = -1
    private var isFragmentVisible = false
    var likesPostDialog: Dialog? = null
    private var recyclerViewState: Parcelable? = null
    var editSharedPost: Dialog? = null
    private fun checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions()
        } else {
            // code for lollipop and pre-lollipop devices
        }
    }

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        isClicked = false
        writeString(requireActivity(), "user_id","")

        //(activity as HomeActivity).activityHeader!!.visibility = View.VISIBLE
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        removeObservers()
        initializeObservers()
        val itemDecorator =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.divider_line
            )!!
        )
        recyclerViewHomePost.addItemDecoration(itemDecorator)
        recyclerViewFriendSuggestion.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            val itemDecoration = GridItemDecoration(
                requireActivity(),
                R.dimen.grid_item_spacing
            ) // R.dimen.grid_item_spacing is 2dp

            addItemDecoration(itemDecoration)
            setEmptyView(textNoFriendsSuggestion)
        }
        if (PreferenceHandler.readString(
                requireActivity(),
                PreferenceHandler.SHOW_SUGGESTION,
                ""
            ).equals("yes")
        ) {
            ivAddStory.visibility = View.GONE
            tvSkipText.visibility = View.VISIBLE
            recyclerViewFriendSuggestion.visibility = View.VISIBLE
            recyclerViewHomePost.visibility = View.GONE
            textNoFriendsSuggestion.visibility = View.GONE
            text_home.visibility = View.GONE
            // getHomeListing()
            getFriendSuggestionApi()
            PreferenceHandler.writeString(
                requireActivity(),
                PreferenceHandler.SHOW_SUGGESTION,
                "no"
            )
        } else {
            /*ivAddStory.visibility = View.GONE
            tvSkipText.visibility = View.VISIBLE
            recyclerViewFriendSuggestion.visibility = View.VISIBLE
            recyclerViewHomePost.visibility = View.GONE
            getFriendSuggestionApi()*/
            (activity as HomeActivity).ivSearchIcon!!.visibility = View.VISIBLE
            ivAddStory.visibility = View.VISIBLE
            tvSkipText.visibility = View.GONE
            recyclerViewFriendSuggestion.visibility = View.GONE
            recyclerViewHomePost.visibility = View.GONE
            textNoFriendsSuggestion.visibility = View.GONE
            text_home.visibility = View.GONE
            getHomeListing()
            initialObservers()
            homeViewModal!!.removeToken()
        }
        tvSkipText.setOnClickListener {
            PreferenceHandler.writeString(
                requireActivity(),
                PreferenceHandler.SHOW_SUGGESTION,
                "no"
            )
            ivAddStory.visibility = View.VISIBLE
            tvSkipText.visibility = View.GONE
            recyclerViewFriendSuggestion.visibility = View.GONE
            recyclerViewHomePost.visibility = View.GONE
            textNoFriendsSuggestion.visibility = View.GONE
            (activity as HomeActivity).ivSearchIcon!!.visibility = View.VISIBLE
            getHomeListing()

        }
        ivAddStory.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addStoryPostFragment)
        }
        checkAndroidVersion()
        (activity as HomeActivity).ivSearchIcon!!.setOnClickListener {
            val intent = Intent(requireActivity(), SearchUserListingActivity::class.java)
            intent.putExtra("from", "0")
            startActivity(intent)
            // etSearch.visibility = View.VISIBLE
        }
        etSearch.addTextChangedListener(textWatcherSearchListener)


    }

    private fun getFriendsListApi() {
        isClicked = true
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        homeViewModal?.friendsList(mHashMap)

    }

    private fun initialObservers() {
        homeViewModal?.friendsListResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        baseUrl = it.media_url
                        friendsList = it.result.data
                        setAdapter()
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
                }

            }

        })
    }

    private fun setAdapter() {
        if (friendsList!!.size > 0) {
            var chatlist: List<InboxPOJO>? = ArrayList()
            rvAdapter = FriendAdapter(chatlist, requireActivity(), baseUrl)
            chatlist = UtilsClass.checkFriends(
                friendsList!!,
                requireActivity(),
                rvAdapter,
                recyclerfriends
            )

            recyclerfriends.visibility = View.VISIBLE
            mView.visibility = View.VISIBLE
        } else {
            recyclerfriends.visibility = View.GONE
            mView.visibility = View.GONE
        }
    }

    private val textWatcherSearchListener: TextWatcher = object : TextWatcher {
        val handler = Handler()
        var runnable: Runnable? = null
        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            handler.removeCallbacks(runnable)
        }

        override fun afterTextChanged(s: Editable) {
            //show some progress, because you can access UI here
            runnable = Runnable {
                //do some work with s.toString()
                getHomeListing()
            }
            handler.postDelayed(runnable, 1000)
        }

        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            requireActivity()!!,
            Manifest.permission.CAMERA
        )
        val wtite = ContextCompat.checkSelfPermission(
            requireActivity()!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val read = ContextCompat.checkSelfPermission(
            requireActivity()!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val record = ContextCompat.checkSelfPermission(
            requireActivity()!!,
            Manifest.permission.RECORD_AUDIO
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (record != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity()!!,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onDestroyView() {
        isFragmentVisible = false
        super.onDestroyView()
        removeObservers()
        homeViewModal = null
//        (activity as HomeActivity).ivSearchIcon!!.visibility = View.GONE
        //(activity as HomeActivity).activityHeader!!.visibility = View.GONE
    }

    private fun getHomeListing() {
        if (isFragmentVisible) {
            isClicked = true
            val mHashMap = HashMap<String, Any>()
            mHashMap["page"] = 1
            mHashMap["per_page"] = 1000
            mHashMap["search"] = etSearch.text.toString().trim()
            homeViewModal?.homePosts(mHashMap)
        }
    }

    private fun getFriendSuggestionApi() {
        if (isFragmentVisible) {
            isClicked = true
            val mHashMap = HashMap<String, Any>()
            mHashMap["page"] = 1
            mHashMap["per_page"] = 100
            homeViewModal?.friendSuggestions(mHashMap)
        }
    }

    private fun addFriend(position: Int) {
        if (isFragmentVisible) {
            isClicked = true
            val mHashMap = HashMap<String, Any>()
            mHashMap["friend_id"] = friendList!!.get(position).id
            homeViewModal?.addFriend(mHashMap)
        }

    }

    private fun initializeObservers() {
        homeViewModal?.cancelFriendRequestResponse!!.removeObservers(this);
        homeViewModal?.cancelFriendRequestResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
                    if (it.status) {
                        getFriendSuggestionApi()
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
            }
        })
        homeViewModal?.friendSuggestionsResponse!!.removeObservers(this);
        homeViewModal?.friendSuggestionsResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
                    if (it.status) {
                        if (isFragmentVisible) {
                            println("Data recieved")
                            setAdapter(it)
                            /* PreferenceHandler.writeString(
                             requireActivity(),
                             PreferenceHandler.SHOW_SUGGESTION,
                             "no"
                         )*/
                        }

                    } else {
                        CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    }
            }
        })
        homeViewModal?.homePostsResponse!!.removeObservers(this);
        homeViewModal?.homePostsResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    if (isFragmentVisible) {
                        println("Data recieved")
                        setHomePostAdapter(it)
                        getFriendsListApi()

                    }
                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        homeViewModal?.reportOrHidePostResponse!!.removeObservers(this);
        homeViewModal?.reportOrHidePostResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
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
        homeViewModal?.addFriendResponse!!.removeObservers(this);
        homeViewModal?.addFriendResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
                    if (it.status) {
                        getFriendSuggestionApi()
                        if (friendSuggestionPosition != -1) {
                        }
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
            }
        })
        homeViewModal?.commentPostResponse!!.removeObservers(this);
        homeViewModal?.commentPostResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
                    if (it.status) {
                        if (postCommentedPosition != -1) {
                            CommonMethods.showSnackbarMessageWithoutColor(
                                requireActivity(),
                                it.message
                            )
                            homePostList!!.get(postCommentedPosition).postComments.add(it.result)
                            homePostsAdadpter!!.notifyDataSetChanged()
                            postCommentedPosition = -1
                        }
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
            }
        })
        homeViewModal?.postLikeUnlikeResponse!!.removeObservers(this);
        homeViewModal?.postLikeUnlikeResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
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
        homeViewModal?.postShareResponse!!.removeObservers(this);
        homeViewModal?.postShareResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
                    if (it.status) {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                        getHomeListing()
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
            }
        })

        homeViewModal?.savePostRequestResponse!!.removeObservers(this);
        homeViewModal?.savePostRequestResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
                    if (it.status) {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                        //getHomeListing()
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
            }
        })
        homeViewModal?.deletePostResponse!!.removeObservers(this);
        homeViewModal!!.deletePostResponse.observe(requireActivity(), Observer {
            it.let {
                if (isClicked)
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                if (it.status) {
                    homePostList!!.removeAt(postDeletedPosition)
                    homePostsAdadpter!!.notifyDataSetChanged()
                }
            }
        })
        homeViewModal?.apiError!!.removeObservers(this);
        homeViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        homeViewModal?.isLoading!!.removeObservers(this);
        homeViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
//                if (isClicked)
                showLoading(it)
            }
        })
        homeViewModal?.onFailure!!.removeObservers(this);

        homeViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                if (isFragmentVisible) {
                    if (isClicked)
                        CommonMethods.showToastMessageAtTop(
                            requireActivity(),
                            ApiFailureTypes().getFailureMessage(it, requireActivity())
                        )
                }
            }
        })
    }

    private fun removeObservers() {
        isClicked = false;
        homeViewModal?.cancelFriendRequestResponse!!.removeObservers(this)

        homeViewModal?.friendSuggestionsResponse!!.removeObservers(this);

        homeViewModal?.homePostsResponse!!.removeObservers(this);

        homeViewModal?.reportOrHidePostResponse!!.removeObservers(this);

        homeViewModal?.addFriendResponse!!.removeObservers(this);

        homeViewModal?.commentPostResponse!!.removeObservers(this);

        homeViewModal?.postLikeUnlikeResponse!!.removeObservers(this);

        homeViewModal?.postShareResponse!!.removeObservers(this);

        homeViewModal?.savePostRequestResponse!!.removeObservers(this);

        homeViewModal?.deletePostResponse!!.removeObservers(this);

        homeViewModal?.apiError!!.removeObservers(this);

        homeViewModal?.isLoading!!.removeObservers(this);

        homeViewModal?.onFailure!!.removeObservers(this);

    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onStop() {
        super.onStop()
        removeObservers()
    }

    override fun likePost(position: Int) {
        if (isFragmentVisible) {
            isClicked = true
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
    }

/*    override fun onHomePostItemClicked(position: Int) {
        val bundle = bundleOf(
            "position" to position,
            "list" to homePostList,
            "post_id" to homePostList!!.get(position).id,
            "baseUrl" to mediaUrl
        )
        findNavController().navigate(
            R.id.action_navigation_home_fragment_to_postDetailsFragment,
            bundle
        )
    }*/

    override fun onHomePostItemClicked(position: Int, childPosition: Int) {
        if (isFragmentVisible) {
            if (homePostList!!.get(position).isShared == 1) {
                if (homePostList!!.get(position).sharedUser.postMediumArrayList.get(childPosition).extension.equals(
                        "mov",
                        true
                    ) ||homePostList!!.get(position).sharedUser.postMediumArrayList.get(childPosition).extension.equals(
                        "mp4",
                        true
                    ) || homePostList!!.get(position).sharedUser.postMediumArrayList.get(
                        childPosition
                    ).extension.equals(
                        "3gp",
                        true
                    ) || homePostList!!.get(position).sharedUser.postMediumArrayList.get(
                        childPosition
                    ).extension.equals(
                        "mpeg",
                        true
                    )
                ) {
                    val intent = Intent(requireActivity(), VideoPlayActivity::class.java)
                    intent.putExtra(
                        "path",
                        mediaUrl + homePostList!!.get(position).sharedUser.postMediumArrayList.get(
                            childPosition
                        ).filePath
                    )
                    requireActivity().startActivity(intent)
                } else {
                    if (!pictureClicked) {
                        val intent = Intent(requireActivity(), FullScreenActivity::class.java)
                        intent.putExtra(
                            "url",
                            mediaUrl + homePostList!!.get(position).sharedUser.postMediumArrayList.get(
                                childPosition
                            ).filePath
                        )
                        requireActivity().startActivity(intent)
                        /*    val bundle = bundleOf(
                            "position" to position,
                            "list" to homePostList,
                            "post_id" to homePostList!!.get(position).id,
                            "baseUrl" to mediaUrl
                        )
                        findNavController().navigate(
                            R.id.action_navigation_home_fragment_to_postDetailsFragment,
                            bundle
                        )*/
                        pictureClicked = true
                    }

                }
            } else {
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
                        mediaUrl + homePostList!!.get(position).postMedia.get(childPosition).filePath
                    )
                    requireActivity().startActivity(intent)
                } else {
                    if (!pictureClicked) {
                        val intent = Intent(requireActivity(), FullScreenActivity::class.java)
                        intent.putExtra(
                            "url",
                            mediaUrl + homePostList!!.get(position).postMedia.get(childPosition).filePath
                        )
                        requireActivity().startActivity(intent)
                        /*    val bundle = bundleOf(
                            "position" to position,
                            "list" to homePostList,
                            "post_id" to homePostList!!.get(position).id,
                            "baseUrl" to mediaUrl
                        )
                        findNavController().navigate(
                            R.id.action_navigation_home_fragment_to_postDetailsFragment,
                            bundle
                        )*/
                        pictureClicked = true
                    }

                }
            }
        }

    }

    override fun onHomePostEditClicked(position: Int) {
        /*  var arrayList = ArrayList<String>()
          for (i in 0 until homePostList!!.get(position).postMedia.size) {
              arrayList.add(mediaUrl + homePostList!!.get(position).postMedia.get(i).filePath)
          }*/
        val bundle = bundleOf(
            "post_id" to homePostList!!.get(position)!!.id,
            "content" to homePostList!!.get(position)!!.content,
            "privacy" to homePostList!!.get(position)!!.privacy,
            /*"list" to arrayList,*/
            "main_list" to homePostList!!,
            "position_clicked" to position!!,
            "mediaUrl" to mediaUrl!!

        )
        findNavController().navigate(R.id.action_navigation_home_to_editPostFragment, bundle)
    }

    override fun onPostDeleted(position: Int) {
        postDeletedPosition = position
        postDeleteApi()
    }

    override fun openProfile(position: Int) {
        if (isFragmentVisible) {
            if (homePostList!!.get(position).user.id.equals(getUserId())) {
                val bundle = bundleOf(
                    "type" to "own",
                    "userId" to homePostList!!.get(position).user.id.toString()
                )
                findNavController().navigate(R.id.action_navigation_home_to_profileFragment, bundle)
            } else {
                val bundle = bundleOf(
                    "type" to "other",
                    "userId" to homePostList!!.get(position).user.id.toString()
                )
                findNavController().navigate(R.id.action_navigation_home_to_profileFragment, bundle)
            }
        }
    }


    private fun postDeleteApi() {
        if (isFragmentVisible) {
            isClicked = true
            val mHashMap = HashMap<String, Any>()
            mHashMap["post_id"] = homePostList!!.get(postDeletedPosition).id
            homeViewModal?.deletePost(mHashMap)
        }
    }

    override fun onResume() {
        super.onResume()
        pictureClicked = false

    }

    private fun setHomePostAdapter(it: HomePostsResponseClass) {
        mediaUrl = it.mediaUrl
        homePostList = it.result.data
        homePostsAdadpter =
            HomePostsAdadpter(homePostList!!, this, requireActivity(), it.mediaUrl, getUserId())
        recyclerViewHomePost.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = homePostsAdadpter

            /*  addItemDecoration(
                  DividerItemDecoration(
                      context,
                      DividerItemDecoration.VERTICAL
                  )
              )*/
            setEmptyView(text_home)
        }
        textNoFriendsSuggestion.visibility = View.GONE
        homePostsAdadpter!!.notifyDataSetChanged()
    }

    private fun setAdapter(it: SuggestionResponseClass) {
        friendList = it.result.data
        mediaUrl = it.media_url
        friendSuggestionAdapter =
            FriendSuggestionAdapter(friendList!!, this, requireContext(), it.media_url)
        recyclerViewFriendSuggestion.adapter = friendSuggestionAdapter
        text_home.visibility = View.GONE
        if (friendSuggestionPosition != -1) {
            recyclerViewFriendSuggestion.getLayoutManager()!!
                .onRestoreInstanceState(recyclerViewState)
            friendSuggestionPosition = -1
        }
        //friendSuggestionAdapter!!.notifyDataSetChanged()

    }

    override fun onLikeItemClicked(position: Int) {
        recyclerViewState =
            recyclerViewFriendSuggestion.getLayoutManager()!!.onSaveInstanceState();//save
        friendSuggestionPosition = position
        addFriend(position)
    }

    override fun onCancelItemClicked(position: Int) {
        isClicked = true
        recyclerViewState =
            recyclerViewFriendSuggestion.getLayoutManager()!!.onSaveInstanceState();//save
        friendSuggestionPosition = position
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = friendList!!.get(position).id
        homeViewModal?.cancelFriendRequest(mHashMap)
    }

    override fun friendItemClicked(position: Int) {
        val bundle = bundleOf(
            "type" to "other",
            "userId" to friendList!!.get(position).id.toString()
        )
        findNavController().navigate(R.id.action_navigation_home_to_profileFragment, bundle)
    }

    override fun onItemClicked(position: Int) {
        openDetailsScreen(position, "false")
    }

    override fun openCommentsScreen(position: Int) {

        openDetailsScreen(position, "true")

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
        val intent = Intent(requireContext(), TaggedListActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("baseUrl", mediaUrl)
        intent.putParcelableArrayListExtra("list", homePostList?.get(position)?.tagged_users_list)
        startActivity(intent)
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

        val rvAdapter = PostLikesAdapter(postLikesList!!, this, requireActivity(), mediaUrl, "tag")
        tagPeopleRV.setAdapter(rvAdapter)
    }

    private fun openDetailsScreen(position: Int, type: String) {
        val bundle = bundleOf(
            "position" to position,
            "list" to homePostList,
            "post_id" to homePostList!!.get(position).id,
            "baseUrl" to mediaUrl,
            "showPicsOrNot" to type
        )
        findNavController().navigate(
            R.id.action_navigation_home_fragment_to_postDetailsFragment,
            bundle
        )
    }

    override fun onItemLiked(position: Int) {
        isClicked = true
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["type"] = "like"
        homeViewModal?.friendSuggestions(mHashMap)
    }

    override fun onPostReported(position: Int) {
        postReportedHiddenPosition = position
        postHideOrReported = "report"
        showReportDialog(position)
//        reportPostApi("report", position)

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
        isClicked = true
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["type"] = "report"
        mHashMap["reason"] = reason
        homeViewModal?.reportOrHidePost(mHashMap)
        reportDialog!!.dismiss()
        homePostList!!.removeAt(position)
        homePostsAdadpter!!.notifyDataSetChanged()

    }

    override fun onPostSaved(positiom: Int) {
        isClicked = true
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(positiom).id
        homeViewModal?.savePost(mHashMap)
    }

    override fun onPostHidden(position: Int) {
        isClicked = true
        postReportedHiddenPosition = position
        postHideOrReported = "hide"
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["type"] = "hide"
        homeViewModal?.reportOrHidePost(mHashMap)
    }

    override fun postShared(position: Int) {
        showDialog(position)
        /*val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["privacy"] = "1"
        homeViewModal?.postShare(mHashMap)*/
        /*    val intent = Intent(Intent.ACTION_SEND)
            val shareBody =
                "Meet Friend Post " + homePostList!!.get(position).content + "\n" + mediaUrl + homePostList!!.get(
                    position
                ).post_media.get(0).file_path
            *//*The type of the content is text, obviously.*//*intent.type = "text/plain"
        *//*Applying information Subject and Body.*//*intent.putExtra(
            Intent.EXTRA_SUBJECT,
            mediaUrl + homePostList!!.get(position).post_media.get(0).file_path
        )
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        *//*Fire!*//*startActivity(Intent.createChooser(intent, "Share"))*/
    }

    private fun sharePostApi(
        position: Int,
        postPrivacySpinnerValue: String,
        content: String
    ) {
        isClicked = true
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

    override fun onCommentAdded(position: Int, comment: String) {
        addCommentApi(position, comment)
    }


    private fun addCommentApi(position: Int, comment: String) {
        isClicked = true
        postCommentedPosition = position
        val mHashMap = HashMap<String, Any>()
        mHashMap["post_id"] = homePostList!!.get(position).id
        mHashMap["content"] = comment
        homeViewModal?.commentPost(mHashMap)
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

    override fun onPostLikesUserClicked(position: Int) {

    }
}