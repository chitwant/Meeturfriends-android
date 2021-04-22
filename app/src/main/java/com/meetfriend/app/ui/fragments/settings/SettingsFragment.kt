package com.meetfriend.app.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.fragments.PrivacyPolicyFragment
import com.meetfriend.app.ui.fragments.ResetPasswordFragment
import com.meetfriend.app.ui.fragments.TermConditionFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar.*

class SettingsFragment : BaseFragment() {
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showAndHideBottomNavigation(false)
        ivBackIcon.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        layoutAdsPayment.setOnClickListener {

        }
        layoutBlockList.setOnClickListener {

        }
        layoutLocation.setOnClickListener {

        }
        layoutNotification.setOnClickListener {

        }
        layoutAddBank.setOnClickListener {

        }
        layoutResetPassword.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, ResetPasswordFragment())
                ?.addToBackStack("ResetFrag")
                ?.commit()
        }
        layoutScrutyMngmnt.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, SecurityManagmentFragment())
                ?.addToBackStack("SecurityFrag")
                ?.commit()
        }
        layoutPrvcyPolicy.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, PrivacyPolicyFragment())
                ?.addToBackStack("PolicyFrag")
                ?.commit()
        }
        layoutTermCondition.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, TermConditionFragment())
                ?.addToBackStack("TermsFrag")
                ?.commit()
        }
    }
}