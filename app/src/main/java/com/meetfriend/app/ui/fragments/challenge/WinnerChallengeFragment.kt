package com.meetfriend.app.ui.fragments.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.challenge.winner.Result
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.adapters.WinnerChallengeAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.ChallengeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_winner_challenge.*
import java.util.*
import kotlin.collections.HashMap

class WinnerChallengeFragment : BaseFragment(),
    WinnerChallengeAdapter.WinnerChallengeAdapterCallback {
    private lateinit var challengeViewModal: ChallengeViewModal
    private var backButton: ImageView? = null
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_winner_challenge, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showAndHideBottomNavigation(false)
        (activity as HomeActivity).toolbar!!.visibility = View.GONE

        headerLoginBackButton?.setOnClickListener {
            findNavController().popBackStack()
        }
        //(activity as HomeActivity).ivBackIcon!!.visibility = View.VISIBLE
        challengeViewModal =
            ViewModelProvider(this).get(ChallengeViewModal::class.java)
        initializeObservers()
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        challengeViewModal?.fetchWinner(mHashMap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as HomeActivity).showAndHideBottomNavigation(true)
        (activity as HomeActivity).toolbar!!.visibility = View.VISIBLE
    }

    private fun initializeObservers() {
        challengeViewModal?.winnerChallengeResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    //CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    setAdapter(it.result, it.media_url)
                } else {
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                     }*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        challengeViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        challengeViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        challengeViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
            }
        })
    }

    private fun setAdapter(result: Result, baseUrl: String) {
        if (result.data.isNullOrEmpty()) {
            tvNoPhotos.visibility = View.VISIBLE
        } else {
            tvNoPhotos.visibility = View.GONE
            recyclerViewWinner.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewWinner.addItemDecoration(
                DividerItemDecoration(
                    recyclerViewWinner.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            Collections.reverse(result.data)
            val adapter = WinnerChallengeAdapter(result.data, this, requireContext(), baseUrl)
            recyclerViewWinner.adapter = adapter
        }
    }

    override fun viewDetails(id: Int) {

    }

    override fun openProfile(id: String) {
        val bundle = bundleOf(
            "from" to "winner",
            "type" to "other",
            "userId" to id
        )
        findNavController().navigate(
            R.id.action_navigation_WinnerChallengeFragment_to_profileFragment,
            bundle
        )
    }
}