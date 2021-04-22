package com.meetfriend.app.ui.fragments.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.settings.Result
import com.meetfriend.app.responseclasses.settings.data
import com.meetfriend.app.ui.adapters.SecurityManagmentAdapter
import com.meetfriend.app.ui.bottomsheet.CityChooseBottomSheet
import com.meetfriend.app.ui.bottomsheet.SecurityManagmentBottomSheet
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.SettingViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_security_mangment.*

class SecurityManagmentFragment : BaseFragment(), SecurityManagmentAdapter.AdapterCallback,
    SecurityManagmentBottomSheet.AdapterCallback {
    private var settingViewModal: SettingViewModal? = null

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_security_mangment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerLoginBackButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        settingViewModal = ViewModelProvider(this).get(SettingViewModal::class.java)
        initializeObservers()
        fetchSecurityManagment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun fetchSecurityManagment() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        settingViewModal?.fetchSecurity(mHashMap)
    }

    private fun initializeObservers() {
        settingViewModal?.securityMngmntResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    //CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    setAdapter(it.result)
                } else {
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                     }*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        settingViewModal?.securityMngmntDeleteResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        settingViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        settingViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        settingViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
            }
        })
    }

    private fun setAdapter(result: Result) {
        if (result.data.isNullOrEmpty()) {
            tvNoSecurity.visibility = View.VISIBLE
        } else {
            tvNoSecurity.visibility = View.GONE
            securityRV.layoutManager = LinearLayoutManager(requireContext())
            securityRV.addItemDecoration(
                DividerItemDecoration(
                    securityRV.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            val adapter = SecurityManagmentAdapter(result.data, this, requireContext())
            securityRV.adapter = adapter
        }
    }

    override fun itemClick(result: data) {
        val bottomSheet = result?.let { SecurityManagmentBottomSheet(it, this) }
        activity?.supportFragmentManager?.let { bottomSheet!!.show(it, "Tag") }
    }

    override fun refreshSecurity() {
        fetchSecurityManagment()
    }
}