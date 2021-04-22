package com.meetfriend.app.ui.fragments.challenge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.challenge.mychallenge.Result
import com.meetfriend.app.ui.activities.ChallangeDetailActivity
import com.meetfriend.app.ui.activities.FullScreenActivity
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.activities.VideoPlayActivity
import com.meetfriend.app.ui.adapters.LiveChallengeAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.ChallengeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_live_challenge.*
import kotlinx.android.synthetic.main.fragment_my_challenge_common.tvNoPhotos
import java.io.File

class LiveChallengeFragment : BaseFragment(), LiveChallengeAdapter.AdapterCallback,ChallengeAcceptPostFragment.responseCallback {
    private lateinit var challengeViewModal: ChallengeViewModal

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_live_challenge, parent, false)
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
        headerLoginBackButton?.setOnClickListener {
            findNavController().popBackStack()
        }
        challengeViewModal =
            ViewModelProvider(this).get(ChallengeViewModal::class.java)
        initializeObservers()
    }

    public  fun fetchLiveChallenges(status: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        mHashMap["is_my_challenge"] = 0
        mHashMap["status"] = status
        challengeViewModal?.fetchMyChallenge(mHashMap)
    }

    private fun initializeObservers() {
        challengeViewModal?.myChallengeResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
//                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    setAdapter(it.result, it.media_url)
                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        challengeViewModal?.challengeAcceptPostReponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    fetchLiveChallenges(1)

                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                } else {
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
        Log.e("resi;t:", "" + result)
        if (result.data.isNullOrEmpty()) {
            tvNoPhotos?.visibility = View.VISIBLE
        } else {
            tvNoPhotos?.visibility = View.GONE
            recyclerViewliveChllnge?.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewliveChllnge?.addItemDecoration(
                DividerItemDecoration(
                    recyclerViewliveChllnge.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            val adapter = LiveChallengeAdapter(result.data, this, requireContext(), baseUrl)
            recyclerViewliveChllnge?.adapter = adapter
        }
    }

    override fun playVideo(path: String) {
        if (File(path).extension == "mov" ||File(path).extension == "mp4" ||
            File(path).extension == "3gp" ||
            File(path).extension == "mpeg"
        ) {
            val intent = Intent(requireActivity(), VideoPlayActivity::class.java)
            intent.putExtra(
                "path",
                path
            )
            requireActivity().startActivity(intent)
        } else {
            val intent = Intent(requireActivity(), FullScreenActivity::class.java)
            intent.putExtra("url", path)
            requireActivity().startActivity(intent)
        }

    }

    override fun viewDetails(id: Int) {
        val intent = Intent(requireContext(), ChallangeDetailActivity::class.java)
        intent.putExtra("id", id);
        intent.putExtra("from", "live");
        startActivity(intent)
    }

    override fun acceptChallenge(challengeId: Int, title: String) {

        var bundle = Bundle()
        bundle.putInt("id", challengeId)
        bundle.putString("title", title)
        bundle.putString("from", "adapter")

        val fragment: Fragment = ChallengeAcceptPostFragment(this)
        fragment.arguments = bundle
        childFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack("Accept")
            .commit()
    }

    override fun rejectChallenge(challengeId: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["challenge_id"] = challengeId
        mHashMap["status"] = 2
        challengeViewModal?.challengeAcceptPost(mHashMap)
    }

    override fun openProfile(id: String) {
        val bundle = bundleOf(
            "from" to "live",
            "type" to "other",
            "userId" to id
        )
        findNavController().navigate(R.id.action_navigation_liveChallengeFragment_to_profileFragment, bundle)

    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).showAndHideBottomNavigation(false)
        (activity as HomeActivity).toolbar!!.visibility = View.GONE
        fetchLiveChallenges(1)

    }

    override fun getDetails() {
        fetchLiveChallenges(1)
    }
}