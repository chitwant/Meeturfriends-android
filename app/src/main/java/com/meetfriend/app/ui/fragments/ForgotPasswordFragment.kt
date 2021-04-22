package com.meetfriend.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.RegisterViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.header_before_login_screens.*


class ForgotPasswordFragment : BaseFragment() {
    private var registerViewModal: RegisterViewModal? = null

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_forgot_password, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModal = ViewModelProvider(this).get(RegisterViewModal::class.java)
        initializeObservers()
        tvSendPassword.setOnClickListener {
            if(etEmailPhone.text.toString().trim().equals("")){
                CommonMethods.showToastMessageAtTop(requireActivity(),"Email is empty")
                return@setOnClickListener
            }
            forgotPasswordAPI()
        }
        headerLoginBackButton.setOnClickListener {
            hideKeyboard()
            hideKeyboard(requireActivity())
            requireActivity().onBackPressed()
        }
    }

    private fun forgotPasswordAPI() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["email_or_phone"] = etEmailPhone.text.toString().trim()
        registerViewModal?.forgotPassword(mHashMap)
    }

    private fun initializeObservers() {
        registerViewModal?.forgotPasswordResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    requireActivity().onBackPressed()
//                    9915137376
                } else {
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {
                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {
                   }*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        registerViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it)

            }
        })
        registerViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        registerViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), ApiFailureTypes().getFailureMessage(it, requireActivity()))
               /* CommonMethods.showSnackbarMessageWithoutColor(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )*/
            }
        })
    }
}