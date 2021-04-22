package com.meetfriend.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_reset_password.*
import java.util.regex.Pattern

class ResetPasswordFragment : BaseFragment() {
    private var homeViewModal: HomeViewModal? = null
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_reset_password, parent, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as HomeActivity).showAndHideBottomNavigation(true)
        (activity as HomeActivity).toolbar!!.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showAndHideBottomNavigation(false)
        (activity as HomeActivity).toolbar!!.visibility = View.GONE
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        initializeObservers()
        tvReset.setOnClickListener {
            if (etOldPassword.text.toString().trim().equals("")) {
                textFieldOldPassword.error = "Old password is empty"
                return@setOnClickListener
            }
            textFieldOldPassword.error = null
            if (etNewPassword.text.toString().trim().equals("")) {
                textFieldNewPassword.error = "New password is empty"
                return@setOnClickListener
            }
            textFieldNewPassword.error = null
            if (!isValidPasswordFormat(etNewPassword.text.toString().trim())) {
                textFieldNewPassword.error =
                    "Password must be atleast 8 characters long, containing lower, upper case, number and special characters"
                return@setOnClickListener
            }
            textFieldNewPassword.error = null

            if (etConfirmPassword.text.toString().trim().equals("")) {
                textFieldCPassword.error = "Confirm password is empty"
                return@setOnClickListener
            }
            textFieldCPassword.error = null
            resetPasswordApi()
        }
        ivBackIcon.setOnClickListener { requireActivity().onBackPressed() }
    }

    fun isValidPasswordFormat(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    /*    "(?=.*[a-z])" + */        //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=?])" +    //at least 1 special character
                    /*"(?=\\S+$)" + */          //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )
        return passwordREGEX.matcher(password).matches()
    }

    private fun resetPasswordApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["old_password"] = etOldPassword.text.toString().trim()
        mHashMap["new_password"] = etNewPassword.text.toString().trim()
        mHashMap["confirm_password"] = etConfirmPassword.text.toString().trim()
        homeViewModal?.resetPassword(mHashMap)
    }

    private fun initializeObservers() {
        homeViewModal?.resetPasswordResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    requireActivity().onBackPressed()
                } else {
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                     }*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
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
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
            }
        })
    }

}