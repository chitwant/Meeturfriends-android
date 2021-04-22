package com.meetfriend.app.ui.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.settings.data
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.CallProgressWheel
import com.meetfriend.app.viewmodal.SettingViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.bottomsheet_security_managment.*


class SecurityManagmentBottomSheet(var result: data, var callBack: AdapterCallback) :
    BottomSheetDialogFragment() {
    private var settingViewModal: SettingViewModal? = null

    interface AdapterCallback {
        fun refreshSecurity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_security_managment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingViewModal = ViewModelProvider(this).get(SettingViewModal::class.java)
        initializeObservers()
        tv_name.text = result.device_model + " - " + result.device_location
        tvDeviceLastTime.text = result.created_at
        tvDeleteDevice.setOnClickListener {
            //callBack.deleteDevice(result.id)
            deleteDevice(result.id)
        }
    }

    fun deleteDevice(id: String) {
        Log.e("securit", "delete device")
        val mHashMap = HashMap<String, Any>()
        mHashMap["sm_id"] = id
        settingViewModal?.deleteSecurity(mHashMap)
    }

    private fun initializeObservers() {
        settingViewModal?.securityMngmntDeleteResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    dismiss()
                    callBack.refreshSecurity()
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

    fun showLoading(show: Boolean?) {
        if (show!!) showLoading() else hideLoading()
    }

    fun showLoading() {
        CallProgressWheel.showLoadingDialog(requireActivity())
    }

    fun hideLoading() {
        CallProgressWheel.dismissLoadingDialog()
    }
}