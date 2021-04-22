package com.meetfriend.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.friendrequest.Datum
import com.meetfriend.app.responseclasses.friendrequest.FriendRequestResponseClass
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.adapters.FriendRequestAdapter
import com.meetfriend.app.ui.adapters.FriendSuggestionAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_friend_request.*

class FriendRequestFragment(var from: String) : BaseFragment(), FriendRequestAdapter.AdapterCallback {
    private var homeViewModal: HomeViewModal? = null
    private var baseUrl = ""
    private var friendSuggestionAdapter: FriendRequestAdapter? = null
    private var requestList: ArrayList<Datum>? = ArrayList()
    private var positionClicked = -1
    private var isFragmentVisible = false
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_friend_request, parent, false)
    }

    override fun onDestroyView() {
        isFragmentVisible = false
        super.onDestroyView()

        //(activity as HomeActivity).showAndHideBottomNavigation(true)
        //(activity as HomeActivity).ivSearchIcon!!.visibility = View.VISIBLE
        //(activity as HomeActivity).ivBackIcon!!.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
       // (activity as HomeActivity).showAndHideBottomNavigation(false)
        //(activity as HomeActivity).ivSearchIcon!!.visibility = View.GONE
        //(activity as HomeActivity).ivBackIcon!!.visibility = View.VISIBLE
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        initializeObservers()
        recyclerViewRequests.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewRequests.setEmptyView(tvNoRequest)
        getHomeListing()

//        (activity as HomeActivity).ivBackIcon!!.setOnClickListener { requireActivity().onBackPressed() }
    }


    private fun initializeObservers() {
        homeViewModal?.friendRequestsResponse!!.observe(requireActivity(), Observer {
            it.let {
                if(isFragmentVisible){
                    if (it.status) {
                        setAdapter(it)
                        //CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
                }

            }

        })
        homeViewModal?.acceptOrRejectRequestResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {


                if (it.status) {
                    if (positionClicked != -1) {
                        requestList!!.removeAt(positionClicked)
                        friendSuggestionAdapter!!.notifyDataSetChanged()
                    }
                    getHomeListing()
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }
            }

        })
        homeViewModal?.acceptFriendRequestResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    if (positionClicked != -1) {
                        requestList!!.removeAt(positionClicked)
                        friendSuggestionAdapter!!.notifyDataSetChanged()
                    }
                    getHomeListing()
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.rejectFriendRequestResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    if (positionClicked != -1) {
                        requestList!!.removeAt(positionClicked)
                        friendSuggestionAdapter!!.notifyDataSetChanged()
                    }
                    getHomeListing()
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
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
                if(isFragmentVisible){


                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )}
            }
        })
    }

    private fun setAdapter(it: FriendRequestResponseClass?) {
        requestList!!.clear()
        baseUrl = it!!.mediaUrl
        requestList = it.result.data
        friendSuggestionAdapter =
            FriendRequestAdapter(requestList!!, this, requireActivity(), baseUrl)
        recyclerViewRequests.adapter = friendSuggestionAdapter
    }

    private fun getHomeListing() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        mHashMap["request_status"] = "pending"
        homeViewModal?.friendRequests(mHashMap)
    }

    override fun onRejectReuest(position: Int) {
        positionClicked = position
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_request_id"] = requestList!!.get(positionClicked).id
        homeViewModal?.rejectFriendRequest(mHashMap)
        //acceptOrRejectFriend("declined")
    }

    override fun onAcceptRequest(position: Int) {
        positionClicked = position
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_request_id"] = requestList!!.get(positionClicked).id
        homeViewModal?.acceptFriendRequest(mHashMap)
       // acceptOrRejectFriend("accepted")
    }

    private fun acceptOrRejectFriend(status: String) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = requestList!!.get(positionClicked).userId
        mHashMap["request_status"] = status
        homeViewModal?.acceptOrRejectRequest(mHashMap)
    }
}