package com.meetfriend.app.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.friends.Data
import com.meetfriend.app.ui.activities.ProfileActivity
import com.meetfriend.app.ui.adapters.TagPeopleAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_myfriends.*
import java.util.HashMap

class MyfriendsFragment(var from: String, var container: Int) : BaseFragment(), TagPeopleAdapter.AdapterCallback {
    private var homeViewModal: HomeViewModal? = null
    private var friendsList: ArrayList<Data>? = ArrayList()
    private var baseUrl = ""
    private var clickedPositiom = -1
    var rvAdapter: TagPeopleAdapter? = null
    private var isFragmentVisible = false
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_myfriends, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        tagPeopleRV.setLayoutManager(LinearLayoutManager(requireActivity()))
        tagPeopleRV.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        initialObservers()
        getFriendsListApi()
    }

    override fun onDestroyView() {
        isFragmentVisible = false
        super.onDestroyView()
    }

    private fun getFriendsListApi() {
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

        homeViewModal?.acceptOrRejectRequestResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    friendsList!!.removeAt(clickedPositiom)
                    rvAdapter!!.notifyDataSetChanged()

                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.unfriendResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    friendsList!!.removeAt(clickedPositiom)
                    rvAdapter!!.notifyDataSetChanged()
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)

                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        homeViewModal?.blockUnBlockPeopleResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    getFriendsListApi()
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
                if (isFragmentVisible) {
                    CommonMethods.showToastMessageAtTop(
                        requireActivity(),
                        ApiFailureTypes().getFailureMessage(it, requireActivity())
                    )
                }

            }
        })
    }

    private fun setAdapter() {
        tagPeopleRV.setEmptyView(tvNoFriends)
        rvAdapter = TagPeopleAdapter(friendsList!!, this, requireActivity(), baseUrl, "friend")
        tagPeopleRV.setAdapter(rvAdapter)

    }

    override fun onTagClicked(position: Int) {

    }

    override fun unBlockClicked(position: Int) {
    }

    override fun unFriendClicked(position: Int) {
        clickedPositiom = position
        unfriendApi(position)
    }

    override fun openUserProfileFromTaggedList(position: Int) {
        if (from.equals("notification")) {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra("from", "activitys")
            intent.putExtra("type", "other")
            intent.putExtra("userId",friendsList!!.get(position).friend_id.toString())
            startActivity(intent)
        } else {
        val bundle = bundleOf(
            "type" to "other",
            "userId" to friendsList!!.get(position).friend_id.toString()
        )
        findNavController().navigate(R.id.action_navigation_friend_to_profileFragment, bundle)
    }}

    override fun blockFriend(position: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = friendsList!!.get(position).friend_id
        mHashMap["block_status"] = "block"
        homeViewModal?.blockUnBlockPeople(mHashMap)
    }

    override fun chat(position: Int) {


    }

    override fun remove(position: Int) {

    }

    private fun unfriendApi(position: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = friendsList!!.get(position).friend_id
        homeViewModal?.unfriend(mHashMap)
    }
}