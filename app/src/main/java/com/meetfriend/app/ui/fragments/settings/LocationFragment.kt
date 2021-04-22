package com.meetfriend.app.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.ui.fragments.ResetPasswordFragment

public class LocationFragment : BaseFragment() {
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_notification_temp, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, MyLocationFragmet())
//            ?.addToBackStack("ResetFrag")
            ?.commit()
    }
}