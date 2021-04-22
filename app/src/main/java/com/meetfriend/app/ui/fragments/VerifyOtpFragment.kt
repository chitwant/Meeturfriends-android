package com.meetfriend.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.VerificationViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_verify_otp.*


class VerifyOtpFragment : BaseFragment() {

    private var verificationViewModal: VerificationViewModal? = null

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_verify_otp, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verificationViewModal = ViewModelProvider(this).get(VerificationViewModal::class.java)
        initializeObervers()
        tvSendOtp.setOnClickListener {
            hideKeyboard()
            hideKeyboard(requireActivity())
            sendOtpApi()
        }
        tvVerifyOtp.setOnClickListener {
            hideKeyboard()
            hideKeyboard(requireActivity())
            verifyOtpResponseApi()
            //findNavController().navigate(R.id.action_verifyOtpFragment_to_registerFragment)

        }
        textResendOTP.setOnClickListener {
            hideKeyboard()
            hideKeyboard(requireActivity())
            sendOtpApi()
        }
    }

    private fun initializeObervers() {
        verificationViewModal?.sendOtpResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    textFieldPhone.visibility = View.GONE
                    tvSendOtp.visibility = View.GONE
                    ccp.visibility = View.GONE
                    textFieldOtp.visibility = View.VISIBLE
                    tvVerifyOtp.visibility = View.VISIBLE
                    textResendOTP.visibility = View.VISIBLE
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    //CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                } else {
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                     }*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }

        })
        verificationViewModal?.verifyOtpResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    val bundle = bundleOf(
                        "type" to "phone",
                        "number" to etPhone.text.toString().trim(),
                        "country" to "+"+ccp.selectedCountryCode
                    )
                    findNavController().navigate(
                        R.id.action_verifyOtpFragment_to_registerFragment,
                        bundle
                    )

                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                     }*/
                    //CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        verificationViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                //CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        verificationViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        verificationViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
                /*CommonMethods.showSnackbarMessageWithoutColor(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )*/
            }
        })
    }

    private fun sendOtpApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["country_code"] = "+"+ccp.selectedCountryCode
        mHashMap["phone"] = etPhone.text.toString()
        verificationViewModal?.sendOtp(mHashMap)
    }


    private fun verifyOtpResponseApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["country_code"] = "+"+ccp.selectedCountryCode
        mHashMap["phone"] = etPhone.text.toString()
        mHashMap["verification_code"] = etOtp.text.toString()
        verificationViewModal?.verifyOtp(mHashMap)

    }

}