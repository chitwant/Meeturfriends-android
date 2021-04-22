package com.meetfriend.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragmentNew
import com.meetfriend.app.responseclasses.savedposts.Datum
import com.meetfriend.app.responseclasses.savedposts.SavedPostsResponseClass
import com.meetfriend.app.ui.activities.FullScreenActivity
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.activities.VideoPlayActivity
import com.meetfriend.app.ui.adapters.SavedPostsAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_saved_posts.*

class SavedPostsFragment : BaseFragmentNew(), SavedPostsAdapter.HomePostsAdadpterAdapterCallback {
    private var homeViewModal: HomeViewModal? = null
    private var mediaUrl = ""
    private var homePostList: ArrayList<Datum>? =
        ArrayList()
    private var homePostsAdadpter: SavedPostsAdapter? = null
    private var pictureClicked = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(
            inflater,
            container,
            savedInstanceState,
            R.layout.fragment_saved_posts
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
            initializeObservers()
            getHomeListing()
            Handler().postDelayed(Runnable {
                (activity as HomeActivity).showAndHideBottomNavigation(false)
                (activity as HomeActivity).toolbar!!.visibility = View.GONE
            }, 100)
            ivBackIcon.setOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as HomeActivity).showAndHideBottomNavigation(true)
        (activity as HomeActivity).toolbar!!.visibility = View.VISIBLE
    }

    private fun initializeObservers() {
        homeViewModal?.savedPostListingResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    println("Data recieved")
                    setHomePostAdapter(it)

                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.deleteSavedPostResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    println("Data recieved")
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    getHomeListing()
                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
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

    private fun setHomePostAdapter(it: SavedPostsResponseClass?) {
        mediaUrl = it!!.mediaUrl
        homePostList!!.clear()
        homePostList = it!!.result.data
        homePostsAdadpter =
            SavedPostsAdapter(homePostList!!, this, requireActivity(), it.mediaUrl, getUserId())
        recyclerViewHomePost.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = homePostsAdadpter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            setEmptyView(tvNoSaved)
        }
        homePostsAdadpter!!.notifyDataSetChanged()

    }

    private fun getHomeListing() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        homeViewModal?.savedPostListing(mHashMap)
    }


    override fun onItemLiked(position: Int) {
    }

    override fun onPostReported(position: Int) {
    }

    override fun onPostSaved(positiom: Int) {
    }

    override fun onPostHidden(position: Int) {
    }

    override fun postShared(position: Int) {
    }

    override fun onCommentAdded(position: Int, comment: String) {
    }

    override fun likePost(position: Int) {
    }

    override fun onHomePostItemClicked(position: Int, childPosition: Int) {
        if (homePostList!!.get(position).post.isShared == 1) {
            if (homePostList!!.get(position).post.sharedPost.postMediumArrayList.get(childPosition).extension.equals(
                    "mov",
                    true
                ) ||homePostList!!.get(position).post.sharedPost.postMediumArrayList.get(childPosition).extension.equals(
                    "mp4",
                    true
                ) || homePostList!!.get(position).post.sharedPost.postMediumArrayList.get(
                    childPosition
                ).extension.equals(
                    "3gp",
                    true
                ) || homePostList!!.get(position).post.sharedPost.postMediumArrayList.get(
                    childPosition
                ).extension.equals(
                    "mpeg",
                    true
                )
            ) {
                val intent = Intent(requireActivity(), VideoPlayActivity::class.java)
                intent.putExtra(
                    "path",
                    mediaUrl + homePostList!!.get(position).post.sharedPost.postMediumArrayList.get(
                        childPosition
                    ).filePath
                )
                requireActivity().startActivity(intent)
            } else {
                if (!pictureClicked) {
                    val intent = Intent(requireActivity(), FullScreenActivity::class.java)
                    intent.putExtra(
                        "url",
                        mediaUrl + homePostList!!.get(position).post.sharedPost.postMediumArrayList.get(
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
            if (homePostList!!.get(position).post.postMedia.get(childPosition).extension.equals(
                    "mov",
                    true
                ) ||homePostList!!.get(position).post.postMedia.get(childPosition).extension.equals(
                    "mp4",
                    true
                ) || homePostList!!.get(position).post.postMedia.get(childPosition).extension.equals(
                    "3gp",
                    true
                ) || homePostList!!.get(position).post.postMedia.get(childPosition).extension.equals(
                    "mpeg",
                    true
                )
            ) {
                val intent = Intent(requireActivity(), VideoPlayActivity::class.java)
                intent.putExtra(
                    "path",
                    mediaUrl + homePostList!!.get(position).post.postMedia.get(childPosition).filePath
                )
                requireActivity().startActivity(intent)
            } else {
                if (!pictureClicked) {
                    val intent = Intent(requireActivity(), FullScreenActivity::class.java)
                    intent.putExtra(
                        "url",
                        mediaUrl + homePostList!!.get(position).post.postMedia.get(childPosition).filePath
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

    override fun onHomePostEditClicked(position: Int) {
    }

    override fun onPostDeleted(position: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["saved_post_id"] = homePostList!!.get(position).id
        homeViewModal?.deleteSavedPost(mHashMap)
    }

    override fun openProfile(position: Int) {
        if (homePostList!!.get(position).post.user.id.equals(getUserId())) {
            val bundle = bundleOf(
                "type" to "own",
                "userId" to homePostList!!.get(position).post.user.id.toString(),
                "show_header" to "no"
            )
            findNavController().navigate(R.id.action_savedPostsFragment_to_profileFragment, bundle)
        } else {
            val bundle = bundleOf(
                "type" to "other",
                "userId" to homePostList!!.get(position).post.user.id.toString(),
                "show_header" to "no"
            )
            findNavController().navigate(R.id.action_savedPostsFragment_to_profileFragment, bundle)
        }

    }

    override fun onItemClicked(position: Int) {
        openDetailsScreen(position, "true")
    }

    override fun openCommentsScreen(position: Int) {
        openDetailsScreen(position, "false")
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
            R.id.action_savedPostsFragment_to_postDetailsFragment,
            bundle
        )
    }

    override fun onResume() {
        super.onResume()
        pictureClicked = false
    }
}