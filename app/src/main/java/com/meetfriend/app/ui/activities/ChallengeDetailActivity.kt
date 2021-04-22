package com.meetfriend.app.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseActivity
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.ChallengeViewModal
import contractorssmart.app.utilsclasses.CommonMethods

class ChallengeDetailActivity : BaseActivity() {
    private lateinit var challengeViewModal: ChallengeViewModal

    private fun fetchChallengeDetails(status: String) {
        val mHashMap = HashMap<String, String>()
        mHashMap["challenge_id"] = status
        challengeViewModal?.fetchChalllengeDetails(mHashMap)
    }

    private fun initializeObservers() {
        challengeViewModal?.challengeDetailsReponse!!.observe(
            this@ChallengeDetailActivity,
            Observer {
                it.let {
                    if (it.status) {
                        CommonMethods.showToastMessageAtTop(
                            this@ChallengeDetailActivity,
                            it.message
                        )
                        //setAdapter(it.result, it.media_url)
                    } else {
                        CommonMethods.showToastMessageAtTop(
                            this@ChallengeDetailActivity,
                            it.message
                        )
                    }
                }
            })
        challengeViewModal!!.apiError.observe(this@ChallengeDetailActivity, Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(this@ChallengeDetailActivity, it)
            }
        })
        challengeViewModal?.isLoading?.observe(this@ChallengeDetailActivity, Observer {
            it?.let {
                showLoading(it)
            }
        })

        challengeViewModal?.onFailure?.observe(this@ChallengeDetailActivity, Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    this@ChallengeDetailActivity,
                    ApiFailureTypes().getFailureMessage(it, this@ChallengeDetailActivity)
                )
            }
        })
    }

    override fun initializeLayout(): Int {
        return R.layout.fragment_challenge_details
    }

    override fun inflateLayout() {
        Log.e("extra:", "" + intent.extras)

        challengeViewModal =
            ViewModelProvider(this).get(ChallengeViewModal::class.java)
        initializeObservers()
        intent.extras?.getString("id")?.let { fetchChallengeDetails(it) }
    }
}