package com.meetfriend.app.ui.activities

import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseActivity
import com.meetfriend.app.responseclasses.friends.Data
import com.meetfriend.app.responseclasses.searchuser.ResultUser
import com.meetfriend.app.responseclasses.searchuser.SearchUserResponseClass
import com.meetfriend.app.ui.adapters.SearchUsersAdapter
import com.meetfriend.app.ui.adapters.TagPeopleAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.activity_search_user_listing.*

class SearchUserListingActivity : BaseActivity(), SearchUsersAdapter.AdapterCallback,
    TagPeopleAdapter.AdapterCallback {
    private var type: String = ""
    private var msg: String =""
    private var searchAdapter: SearchUsersAdapter? = null
    private var mediaUrl = ""
    private var searchUserListingActivity: SearchUserListingActivity? = null
    private var homeViewModal: HomeViewModal? = null
    private var homePostList: ArrayList<ResultUser>? =
        ArrayList()
    var isFriend: String = "0"
    private var friendsList: ArrayList<Data>? = ArrayList()
    private var baseUrl = ""
    var rvAdapter: TagPeopleAdapter? = null

    override fun initializeLayout(): Int {
        return R.layout.activity_search_user_listing
    }

    override fun inflateLayout() {
        searchUserListingActivity = this@SearchUserListingActivity
        etSearchUser.addTextChangedListener(textWatcherSearchListener)
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        ivBackIcon.setOnClickListener { onBackPressed() }
        isFriend = intent.getStringExtra("from")
        if (isFriend.equals("1")) {
            if( !intent.getStringExtra("msg").equals("")){
                msg= intent.getStringExtra("msg")
                type= intent.getStringExtra("type")
            }
            initialObservers()
            getFriendsListApi()
        } else {

        }
        if (isFriend.equals("1")) {
            tvTitleHeader!!.setText("Create Chat")
//            etSearchUser!!.visibility = View.GONE
        }
        initializeObservers()
    }

    private fun getFriendsListApi() {
        val mHashMap = java.util.HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        homeViewModal?.friendsList(mHashMap)
    }

    private fun initialObservers() {
        homeViewModal?.friendsListResponse!!.observe(searchUserListingActivity!!, Observer {
            it.let {
                if (it.status) {
                    baseUrl = it.media_url
                    friendsList = it.result.data
                    setAdapter()
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(
                        searchUserListingActivity!!,
                        it.message
                    )
                }
            }
        })

        homeViewModal!!.apiError.observe(searchUserListingActivity!!, Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(searchUserListingActivity!!, it)
            }
        })
        homeViewModal?.isLoading?.observe(searchUserListingActivity!!, Observer {
            it?.let {
                showLoading(it)
            }
        })

        homeViewModal?.onFailure?.observe(searchUserListingActivity!!, Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    searchUserListingActivity!!,
                    ApiFailureTypes().getFailureMessage(it, searchUserListingActivity!!)
                )

            }
        })
    }

    private fun setAdapter() {
        recyclerViewSearchListing.visibility=View.VISIBLE
        if (friendsList!!.size > 0) {
            mNoFound.visibility = View.GONE
            recyclerViewSearchListing.apply {
                layoutManager = LinearLayoutManager(searchUserListingActivity)
                adapter = searchAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                //setEmptyView(textNoFriendsSuggestion)
            }
            rvAdapter = TagPeopleAdapter(
                friendsList!!,
                this,
                searchUserListingActivity!!,
                baseUrl,
                "chat"

            )
            recyclerViewSearchListing.setAdapter(rvAdapter)

        } else {
            mNoFound.visibility = View.VISIBLE
        }
    }

    private fun getListingApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["is_friend"] = isFriend
        mHashMap["per_page"] = 1000
        mHashMap["search"] = etSearchUser.text.toString().trim()
        homeViewModal?.searchUsers(mHashMap)
    }

    private fun initializeObservers() {
        homeViewModal?.searchUsersResponse!!.observe(searchUserListingActivity!!, Observer {
            it.let {
                if (it.status) {
                    setAdapter(it)
                } else {
                    CommonMethods.showToastMessageAtTop(searchUserListingActivity!!, it.message)
                }
            }
        })
        homeViewModal?.addFriendResponse!!.observe(searchUserListingActivity!!, Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showSnackbarMessageWithoutColor(
                        searchUserListingActivity!!,
                        it.message
                    )
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(
                        searchUserListingActivity!!,
                        it.message
                    )
                }
            }
        })
        homeViewModal!!.apiError.observe(searchUserListingActivity!!, Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(searchUserListingActivity!!, it)
            }
        })
        homeViewModal?.isLoading?.observe(searchUserListingActivity!!, Observer {
            it?.let {
                showLoading(it)
            }
        })

        homeViewModal?.onFailure?.observe(searchUserListingActivity!!, Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    searchUserListingActivity!!,
                    ApiFailureTypes().getFailureMessage(it, searchUserListingActivity!!)
                )
            }
        })
    }

    private fun setAdapter(it: SearchUserResponseClass) {
        homePostList!!.clear()
        homePostList = it.result
        mediaUrl = it.media_url
        searchAdapter =
            SearchUsersAdapter(
                isFriend,
                homePostList!!,
                this,
                searchUserListingActivity!!,
                mediaUrl!!
            )
        recyclerViewSearchListing.apply {
            layoutManager = LinearLayoutManager(searchUserListingActivity)
            adapter = searchAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            //setEmptyView(textNoFriendsSuggestion)
        }

        searchAdapter!!.notifyDataSetChanged()
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
            if (etSearchUser.text.toString().trim().length > 0) {
                runnable = Runnable {
                    //do some work with s.toString()
                    getListingApi()
                }
                handler.postDelayed(runnable, 1000)
            }else{
                if (isFriend.equals("1")) {
                    recyclerViewSearchListing.setAdapter(rvAdapter)
                }
            }
        }

        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }
    }

    override fun onUserClicked(position: Int) {
        if (isFriend.equals("0")) {
            val intent = Intent(this@SearchUserListingActivity, ProfileActivity::class.java)
            intent.putExtra("type", "other")
            intent.putExtra("from", "activitys")
            intent.putExtra("userId", homePostList!!.get(position).id.toString())
            startActivity(intent)
        } else {
            val intent = Intent(this@SearchUserListingActivity, ChatActivity::class.java)
            intent.putExtra("user_id", homePostList!!.get(position).id.toString());
            intent.putExtra(
                "image",
                "https://meeturfriends.s3.amazonaws.com" + homePostList!!.get(position).profile_photo
            );
            intent.putExtra(
                "name",
                homePostList!!.get(position)?.firstName + " " + homePostList!!.get(position)?.lastName
            )
            intent.putExtra(
                "msg",
                msg
            )
            intent.putExtra(
                "type",
                type
            )
            startActivity(intent)
        }

    }

    override fun addFriendClicked(position: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = homePostList!!.get(position).id
        homeViewModal?.addFriend(mHashMap)
    }

    override fun onTagClicked(position: Int) {


    }

    override fun unBlockClicked(position: Int) {
    }

    override fun unFriendClicked(position: Int) {
    }

    override fun openUserProfileFromTaggedList(position: Int) {
    }

    override fun blockFriend(position: Int) {
    }

    override fun chat(position: Int) {
        val intent = Intent(searchUserListingActivity!!, ChatActivity::class.java)
        intent.putExtra("user_id", friendsList!![position].accepted_user.id.toString() + "")
        intent.putExtra(
            "image",
            baseUrl + friendsList!![position].accepted_user.profile_photo
        )
        intent.putExtra(
            "name",
            friendsList!![position].accepted_user.firstName + " " + friendsList!![position].accepted_user.lastName
        )
        intent.putExtra(
            "msg",
            msg
        )
        intent.putExtra(
            "type",
            type
        )
        startActivity(intent)
    }

    override fun remove(position: Int) {

    }

}