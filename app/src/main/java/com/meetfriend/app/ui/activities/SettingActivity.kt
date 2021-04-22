package com.meetfriend.app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meetfriend.app.R
import com.meetfriend.app.ui.fragments.PrivacyPolicyFragment
import com.meetfriend.app.ui.fragments.ResetPasswordFragment
import com.meetfriend.app.ui.fragments.TermConditionFragment
import com.meetfriend.app.ui.fragments.settings.LocationFragment
import com.meetfriend.app.ui.fragments.settings.NotificationsFragment
import com.meetfriend.app.ui.fragments.settings.SecurityManagmentFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_settings)

        ivBackIcon.setOnClickListener {
            onBackPressed()
        }
        layoutAdsPayment.setOnClickListener {
        }
        layoutAddBank.setOnClickListener {
            val intent = Intent(this, AddBankDetailActivity::class.java)
            intent.putExtra("from", "update");
            startActivity(intent)
        }
        layoutBlockList.setOnClickListener {
//            supportFragmentManager?.beginTransaction()
//                ?.replace(R.id.settingContainer, BlockedUsersFragment(ff))
//                ?.addToBackStack("locFrag")
//                ?.commit()
        }
        layoutLocation.setOnClickListener {
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, LocationFragment())
                ?.addToBackStack("locFrag")
                ?.commit()
        }
        layoutNotification.setOnClickListener {
            supportFragmentManager?.beginTransaction()
                ?.add(R.id.settingContainer, NotificationsFragment())
                ?.addToBackStack("notiFrag")
                ?.commit()
        }
        layoutResetPassword.setOnClickListener {
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, ResetPasswordFragment())
                ?.addToBackStack("ResetFrag")
                ?.commit()
        }
        layoutScrutyMngmnt.setOnClickListener {
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, SecurityManagmentFragment())
                ?.addToBackStack("SecurityFrag")
                ?.commit()
        }
        layoutPrvcyPolicy.setOnClickListener {
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, PrivacyPolicyFragment())
                ?.addToBackStack("PolicyFrag")
                ?.commit()
        }
        layoutTermCondition.setOnClickListener {
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.settingContainer, TermConditionFragment())
                ?.addToBackStack("TermsFrag")
                ?.commit()
        }
    }
}