package com.meetfriend.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.CallProgressWheel
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_policy.*

class PrivacyPolicyFragment : BaseFragment() {
    private var homeViewModal: HomeViewModal? = null

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_policy, parent, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
       // (activity as HomeActivity).showAndHideBottomNavigation(true)
       // (activity as HomeActivity).activityHeader!!.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as HomeActivity).showAndHideBottomNavigation(false)
        //(activity as HomeActivity).activityHeader!!.visibility = View.GONE
//        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
//        initializeObservers()
//        homeViewModal?.privacyPolicy()
        webview.settings.setJavaScriptEnabled(true)
//        CallProgressWheel.showLoadingDialog(requireActivity())

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
//                CallProgressWheel.dismissLoadingDialog()

                return true
            }
        }
        webview.loadUrl("https://www.meeturfriends.com/privacy-policy")
    }

    private fun initializeObservers() {
        homeViewModal?.privacyPolicyResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
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