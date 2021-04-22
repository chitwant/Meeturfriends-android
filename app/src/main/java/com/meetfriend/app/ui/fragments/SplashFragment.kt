package com.meetfriend.app.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.meetfriend.app.R
import com.sanojpunchihewa.updatemanager.UpdateManager
import contractorssmart.app.utilsclasses.PreferenceHandler


class SplashFragment : Fragment() {
    private val SPLASH_DELAY: Long = 2000
    private var mDelayHandler: Handler? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(
            {
                if (PreferenceHandler.readString(
                        requireActivity(),
                        PreferenceHandler.AUTHORIZATION_TOKEN,
                        ""
                    ).equals("")
                ) {
                    findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
                } else {
                    if (PreferenceHandler.readString(
                            requireActivity(),
                            PreferenceHandler.SHOW_SUGGESTION,
                            ""
                        ).equals("Yes", true)
                    ) {
                        findNavController().navigate(R.id.action_splashFragment_to_editProfileInfoFragment)
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_homeActivity)
                        requireActivity().finish()
                    }
                }

            }, SPLASH_DELAY
        )
    }

}