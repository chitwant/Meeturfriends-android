package com.meetfriend.app.ui.fragments.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment

class PublicChallengeFragment : BaseFragment() {
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_public_challenge, parent, false)
    }
}