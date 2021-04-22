package com.meetfriend.app.ui.fragments.challenge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.meetfriend.app.ui.activities.VideoPlayActivity
import com.meetfriend.app.ui.adapters.MyChallengeAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.ChallengeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_my_challenge_common.*
import java.io.File

class LiveMyChallengeFragment : BaseFragment(), MyChallengeAdapter.AdapterCallback {
    private lateinit var challengeViewModal: ChallengeViewModal

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_challenge_common, parent, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        challengeViewModal =
            ViewModelProvider(this).get(ChallengeViewModal::class.java)
        initializeObservers()
    }

    override fun onResume() {
        super.onResume()
        fetchMyChallenges(1)

    }

    private fun fetchMyChallenges(status: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        mHashMap["is_my_challenge"] = 1
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
        Log.e("resi;t:", "" + result)
        if (result.data.isNullOrEmpty()) {
            tvNoPhotos?.visibility = View.VISIBLE
        } else {
            tvNoPhotos?.visibility = View.GONE
            recyclerViewMychllnge?.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewMychllnge?.addItemDecoration(
                DividerItemDecoration(
                    recyclerViewMychllnge.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            if (recyclerViewMychllnge != null) {
                val adapter = MyChallengeAdapter(result.data, this, requireContext(), baseUrl,"live")
                recyclerViewMychllnge?.adapter = adapter
            }
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
            startActivity(intent)
        }
    }

    override fun viewDetails(id: Int) {
        val intent = Intent(requireContext(), ChallangeDetailActivity::class.java)
        intent.putExtra("id", id);
        intent.putExtra("from", "my");

        startActivity(intent)
    }

    override fun openProfile(id: String) {
        val bundle = bundleOf(
            "from" to "live",
            "type" to "other",
            "userId" to id
        )
        findNavController().navigate(R.id.action_myChallengeFragment_to_profileFragment, bundle)

//        val fragment: Fragment = ProfileFragment()
//        fragment.arguments=bundle
//        childFragmentManager
//            .beginTransaction()
//            .replace(R.id.containers, fragment)
//            .commit()
    }
}