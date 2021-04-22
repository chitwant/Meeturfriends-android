package com.meetfriend.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.modals.friendsuggestion.Data
import com.meetfriend.app.modals.friendsuggestion.SuggestionResponseClass
import com.meetfriend.app.ui.activities.ProfileActivity
import com.meetfriend.app.ui.adapters.FriendSuggestionAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.GridItemDecoration
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_friend_suggestions.*


class FriendSuggestionsFragment(var from: String, var container: Int) : BaseFragment(),
    FriendSuggestionAdapter.AdapterCallback {
    private var homeViewModal: HomeViewModal? = null
    private var friendList: ArrayList<Data>? = ArrayList()
    private var friendSuggestionAdapter: FriendSuggestionAdapter? = null
    private var mediaUrl = ""
    private var friendSuggestionPosition = -1
    private var isFragmentVisible = false
    private var recyclerViewState: Parcelable? = null
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_friend_suggestions, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        // (activity as HomeActivity).showAndHideBottomNavigation(false)
        //(activity as HomeActivity).ivSearchIcon!!.visibility = View.GONE
        //(activity as HomeActivity).ivBackIcon!!.visibility = View.VISIBLE
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        friendSuggestionAdapter =
            FriendSuggestionAdapter(friendList!!, this, requireContext(), mediaUrl)
        recyclerViewFriendSuggestion.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            // layoutManager = LinearLayoutManager(requireActivity())
            adapter = friendSuggestionAdapter
            val itemDecoration = GridItemDecoration(
                requireActivity(),
                R.dimen.grid_item_spacing
            ) // R.dimen.grid_item_spacing is 2dp

            addItemDecoration(itemDecoration)
            setEmptyView(textNoFriendsSuggestion)
        }
        initializeObservers()
        getFriendSuggestionApi()
//        (activity as HomeActivity).ivBackIcon!!.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun onDestroyView() {
        isFragmentVisible = false
        super.onDestroyView()
        //(activity as HomeActivity).showAndHideBottomNavigation(true)
        //(activity as HomeActivity).ivSearchIcon!!.visibility = View.VISIBLE
        //(activity as HomeActivity).ivBackIcon!!.visibility = View.GONE
    }

    private fun initializeObservers() {
        homeViewModal?.friendSuggestionsResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        println("Data recieved")
                        setAdapter(it)
                    } else {
                        CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    }
                }
            }

        })
        homeViewModal?.addFriendResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        getFriendSuggestionApi()
                        if (friendSuggestionPosition != -1) {
                            // friendList!!.get(friendSuggestionPosition).sentOrNot=true
                            // friendList!!.removeAt(friendSuggestionPosition)
                            // friendSuggestionAdapter!!.notifyDataSetChanged()
                            // friendSuggestionPosition = -1
                        }


                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
                }
            }

        })
        homeViewModal?.cancelFriendRequestResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    getFriendSuggestionApi()
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
                if (isFragmentVisible) {
                    CommonMethods.showToastMessageAtTop(
                        requireActivity(),
                        ApiFailureTypes().getFailureMessage(it, requireActivity())
                    )
                }
            }
        })
    }

    override fun onLikeItemClicked(position: Int) {
        recyclerViewState =
            recyclerViewFriendSuggestion.getLayoutManager()!!.onSaveInstanceState();//save
        friendSuggestionPosition = position
        addFriend(position)
    }

    override fun onCancelItemClicked(position: Int) {
        recyclerViewState =
            recyclerViewFriendSuggestion.getLayoutManager()!!.onSaveInstanceState();//save
        friendSuggestionPosition = position
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = friendList!!.get(position).id
        homeViewModal?.cancelFriendRequest(mHashMap)
        /*friendSuggestionPosition = position
        friendList!!.removeAt(position)
        friendSuggestionAdapter!!.notifyDataSetChanged()*/
    }

    override fun friendItemClicked(position: Int) {
        if (from.equals("notification")) {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra("call", "profile")
            intent.putExtra("type", "other")
            intent.putExtra("from", "activitys")
            intent.putExtra("userId", friendList!!.get(position).id.toString())
            startActivity(intent)
        } else {
            val bundle = bundleOf(
                "type" to "other",
                "userId" to friendList!!.get(position).id.toString()
            )
            findNavController().navigate(R.id.action_navigation_friend_to_profileFragment, bundle)
        }
    }

    private fun getFriendSuggestionApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        homeViewModal?.friendSuggestions(mHashMap)
    }

    private fun addFriend(position: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = friendList!!.get(position).id
        homeViewModal?.addFriend(mHashMap)
    }

    private fun setAdapter(it: SuggestionResponseClass) {
        friendList = it.result.data
        mediaUrl = it.media_url
        /*  friendSuggestionAdapter =
              FriendSuggestionAdapter(friendList!!, this, requireContext(), it.media_url)
          recyclerViewFriendSuggestion.apply {
              layoutManager = GridLayoutManager(requireActivity(), 2)
              // layoutManager = LinearLayoutManager(requireActivity())
              adapter = friendSuggestionAdapter
              val itemDecoration = GridItemDecoration(
                  requireActivity(),
                  R.dimen.grid_item_spacing
              ) // R.dimen.grid_item_spacing is 2dp

              addItemDecoration(itemDecoration)
              setEmptyView(textNoFriendsSuggestion)
          }*/
        if (friendSuggestionPosition != -1) {
            recyclerViewFriendSuggestion.getLayoutManager()!!
                .onRestoreInstanceState(recyclerViewState)//restore
            // recyclerViewFriendSuggestion.smoothScrollToPosition(friendSuggestionPosition.plus(1))
            friendSuggestionPosition = -1
        }
        friendSuggestionAdapter =
            FriendSuggestionAdapter(friendList!!, this, requireContext(), mediaUrl)
        recyclerViewFriendSuggestion.adapter = friendSuggestionAdapter
        // friendSuggestionAdapter!!.notifyDataSetChanged()

        // friendSuggestionAdapter!!.notifyDataSetChanged()

    }
}