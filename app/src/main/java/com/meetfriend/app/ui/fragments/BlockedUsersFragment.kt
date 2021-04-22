package com.meetfriend.app.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.friends.Data
import com.meetfriend.app.ui.adapters.TagPeopleAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_blocked_users.*
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.MutableIterator
import kotlin.collections.set

class BlockedUsersFragment(var from: String) : BaseFragment(), TagPeopleAdapter.AdapterCallback {
    private var homeViewModal: HomeViewModal? = null
    private var baseUrl = ""
    private var list: ArrayList<Data>? = ArrayList()
    private var selectedPositiom = -1
    var rvAdapter: TagPeopleAdapter? = null
    private var isFragmentVisible = false
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_blocked_users, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
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
        homeViewModal?.blockedList(mHashMap)

    }

    private fun initialObservers() {
        homeViewModal?.blockedListResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        baseUrl = it.media_url
                        list = it.result.data
                        setAdapter()
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
                }
            }

        })
        homeViewModal?.blockUnBlockPeopleResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        list!!.removeAt(selectedPositiom)
                        rvAdapter!!.notifyDataSetChanged()
                        setAdapter()
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
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
        blockedPeopleRV.setLayoutManager(LinearLayoutManager(context))
        blockedPeopleRV.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
//        blockedPeopleRV.setEmptyView(tvNoFriends)
        //tagPeopleRV.addItemDecoration(SimpleDividerItemDecoration(context, R.drawable.divider))
//        var lists: ArrayList<HashMap<String,String>>? = ArrayList()
//        for (i in 0 until list!!.size){
//            val hashmap: HashMap<String,String>?
//            if (list!![i].is_blocked_by_me==1){
//               hashmap?.put("id","")
//
//               lists!!.add(list!![i])
//           }
//        }

        if (list!!.size>0){
            tvNoFriends.visibility=View.GONE
        }
        rvAdapter = TagPeopleAdapter(list!!, this, requireActivity(), baseUrl, "blocked")
        blockedPeopleRV.setAdapter(rvAdapter)
    }

    override fun onTagClicked(position: Int) {

    }

    override fun unBlockClicked(position: Int) {
        selectedPositiom = position
        unblockUserApi()
    }

    override fun unFriendClicked(position: Int) {
    }

    override fun openUserProfileFromTaggedList(position: Int) {
    }

    override fun blockFriend(position: Int) {

    }

    override fun chat(position: Int) {


    }

    override fun remove(position: Int) {
        list!!.removeAt(position)
       }

    private fun unblockUserApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = list!!.get(selectedPositiom).friend_id
        mHashMap["block_status"] = "unblock"
        homeViewModal?.blockUnBlockPeople(mHashMap)
    }
}