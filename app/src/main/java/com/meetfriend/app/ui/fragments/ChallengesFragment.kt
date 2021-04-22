package com.meetfriend.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.ui.activities.CreateChallengeActivity
import com.meetfriend.app.ui.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_challenges.*

class ChallengesFragment : BaseFragment() {

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_challenges, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdminChallenge.setOnClickListener {
            //findNavController().navigate(R.id.action_mychallenge_to_adminchallengeFragment)
            val intent = Intent(activity, CreateChallengeActivity::class.java)
            startActivity(intent)
        }
        (activity as HomeActivity).ivSearchIcon!!.visibility = View.GONE

        createPublicChallenge.setOnClickListener {
            findNavController().navigate(R.id.action_mychallenge_to_publicchallengeFragment)
        }
        myChallenges.setOnClickListener {
            findNavController().navigate(R.id.action_mychallenge_to_mychallengeFragment)
        }
        challengesListingDone.setOnClickListener {
            findNavController().navigate(R.id.action_mychallenge_to_challengeListDoneFragment)
        }
        liveChallenges.setOnClickListener {
            findNavController().navigate(R.id.action_mychallenge_to_livechallengeFragment)
        }
        winnerChallenges.setOnClickListener {
            findNavController().navigate(R.id.action_mychallenge_to_winnerchallengeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}