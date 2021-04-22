package com.meetfriend.app.ui.fragments.settings

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.notification.Result
import com.meetfriend.app.ui.activities.ChallangeDetailActivity
import com.meetfriend.app.ui.activities.GiftTransactionActivity
import com.meetfriend.app.ui.activities.ProfileActivity
import com.meetfriend.app.ui.adapters.NotificationAdapter
import com.meetfriend.app.ui.fragments.FriendsFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.NotificationsViewModel
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_security_mangment.securityRV
import kotlinx.android.synthetic.main.fragment_security_mangment.tvNoSecurity

class NotificationsFragment : BaseFragment(), NotificationAdapter.AdapterCallback {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_notifications, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        initializeObservers()
        fetchSecurityManagment()
        tvDeleteAll.setOnClickListener {
            deleteAlertDialog("All", true)
        }
        notificationsViewModel?.readAllNotification()
//        (activity as HomeActivity).notificationCount=0
        headerLoginBackButton.setOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })
    }

    private fun fetchSecurityManagment() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        notificationsViewModel?.fetchNotification(mHashMap)
    }

    private fun initializeObservers() {
        notificationsViewModel?.notifcationResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    setAdapter(it.result, it.media_url)
                } else {

                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        notificationsViewModel?.notifcationDeleteResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    fetchSecurityManagment()
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        notificationsViewModel?.notifcationDeleteAllResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    fetchSecurityManagment()
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        notificationsViewModel!!.apiError.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        notificationsViewModel?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        notificationsViewModel?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
            }
        })
    }

    private fun setAdapter(result: Result, baseUrl: String) {
        if (result.data.isNullOrEmpty()) {
            tvNoSecurity.visibility = View.VISIBLE
            securityRV.visibility = View.GONE
        } else {
            securityRV.visibility = View.VISIBLE
            tvNoSecurity.visibility = View.GONE
            securityRV.layoutManager = LinearLayoutManager(requireContext())
            securityRV.addItemDecoration(
                DividerItemDecoration(
                    securityRV.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            val adapter = NotificationAdapter(result.data, this, requireContext(), baseUrl)
            securityRV.adapter = adapter
        }
    }

    override fun deleteNotification(id: String) {
        deleteAlertDialog(id, false)
    }

    override fun openDetail(id: Int, from: String, title: String) {
        if (from.equals("post")) {
//            "position" to position,
//            "list" to homePostList,
//            "post_id" to homePostList!!.get(position).id,
//            "baseUrl" to baseUrl,
//            "showPicsOrNot" to type
//                childFragmentManager?.beginTransaction()
//                    ?.add(R.id.container, it)
//                    ?.addToBackStack("TermsFrag")
//                    ?.commit()
//
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra("call", "post")
            intent.putExtra("position", "")
            intent.putExtra("list", "")
            intent.putExtra("post_id", id)
            intent.putExtra("baseUrl", "")
            intent.putExtra("showPicsOrNot","" )
            startActivity(intent)
        } else if (from.equals("Friends")) {
            FriendsFragment().newInstance("notification")?.let {
                childFragmentManager?.beginTransaction()
                    ?.add(R.id.container, it)
                    ?.addToBackStack("TermsFrag")
                    ?.commit()
            }
        } else if (from.equals("gift")) {
            val intent = Intent(requireContext(), GiftTransactionActivity::class.java)
            startActivity(intent)
        } else if (from.equals("challenge")) {
            var type = ""
            if (title.toString().contains("gift")) {
                val intent = Intent(requireContext(), GiftTransactionActivity::class.java)
                startActivity(intent)
            } else {
                if (title.toString().contains("completed")) {
                    type = "done"
                } else if (title.toString().contains("accepted")) {
                    type = "my"
                } else if (title.toString().contains("reacted")) {
                    type = "my"
                } else if (title.toString().contains("live")) {
                    type = "my"
                }
                val intent = Intent(context, ChallangeDetailActivity::class.java)
                intent.putExtra("id", id);
                intent.putExtra("from", type);
                startActivity(intent)
            }
        }
    }

    private fun deleteAlertDialog(id: String, deleteAll: Boolean) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete")
        if (deleteAll) {
            alertDialog.setMessage("Are you sure want to clear all?")
        } else {
            alertDialog.setMessage("Are you sure want to delete?")
        }
        if (deleteAll) {
            alertDialog.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                notificationsViewModel?.deleteAllNotification()
            }
        } else {
            alertDialog.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                val mHashMap = HashMap<String, Any>()
                mHashMap["id"] = id
                notificationsViewModel?.deleteNotification(mHashMap)
            }
        }
        alertDialog.setNegativeButton("No") { dialog: DialogInterface?, which: Int ->

        }
        val alert: AlertDialog = alertDialog.create()
        alert.show()
    }
}