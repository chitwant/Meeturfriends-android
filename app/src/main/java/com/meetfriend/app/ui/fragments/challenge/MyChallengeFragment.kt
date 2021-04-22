package com.meetfriend.app.ui.fragments.challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.adapters.MyChallengeAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.ChallengeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import com.meetfriend.app.responseclasses.challenge.mychallenge.Result
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_my_challenge.*
import kotlinx.android.synthetic.main.fragment_my_challenge.headerLoginBackButton
import kotlinx.android.synthetic.main.fragment_winner_challenge.*


class MyChallengeFragment : BaseFragment() {
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_challenge, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).showAndHideBottomNavigation(false)
        (activity as HomeActivity).toolbar!!.visibility = View.GONE
        headerLoginBackButton?.setOnClickListener {
            findNavController().popBackStack()
        }
        setTabLayout()
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab?.position == 2) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.colorAccent));
                    view.text = "Upcoming"
                    val fragment: Fragment = PendingMyChallengeFragment()
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.viewpager, fragment)
                        .commit()
                }
                if (tab?.position == 0) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.colorAccent));
                    view.text = "Live"
                    val fragment: Fragment = LiveMyChallengeFragment()
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.viewpager, fragment)
                        .commit()
                }
                if (tab?.position == 1) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.colorAccent));
                    view.text = "Done"
                    val fragment: Fragment = DoneMychallengeFragment()
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.viewpager, fragment)
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab?.position == 2) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.grey));
                    view.text = "Upcoming"
                }
                if (tab?.position == 0) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.grey));
                    view.text = "Live"
                }
                if (tab?.position == 1) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.grey));
                    view.text = "Done"
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 2) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.colorAccent));
                    view.text = "Upcoming"
                    val fragment: Fragment = PendingMyChallengeFragment()
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.viewpager, fragment)
                        .commit()
                }
                if (tab?.position == 0) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.colorAccent));
                    view.text = "Live"
                    val fragment: Fragment = LiveMyChallengeFragment()
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.viewpager, fragment)
                        .commit()
                }
                if (tab?.position == 1) {
                    var view: TextView = tab.customView as TextView
                    view.setTextColor(requireActivity().getColor(R.color.colorAccent));
                    view.text = "Done"
                    val fragment: Fragment = DoneMychallengeFragment()
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.viewpager, fragment)
                        .commit()
                }
            }
        })
        defaultTab();

    }

    /*  private fun fetchMyChallenges(status: Int) {
          val mHashMap = HashMap<String, Any>()
          mHashMap["page"] = 1
          mHashMap["per_page"] = 15
          mHashMap["is_my_challenge"] = 15
          mHashMap["status"] = status
          challengeViewModal?.fetchMyChallenge(mHashMap)
      }*/

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as HomeActivity).showAndHideBottomNavigation(true)
        (activity as HomeActivity).toolbar!!.visibility = View.VISIBLE
    }



    /*private fun initializeObservers() {
        challengeViewModal?.myChallengeResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
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
            tvNoPhotos.visibility = View.VISIBLE
        } else {
            tvNoPhotos.visibility = View.GONE
            recyclerViewMychllnge.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewMychllnge.addItemDecoration(
                DividerItemDecoration(
                    recyclerViewMychllnge.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            val adapter = MyChallengeAdapter(result.data, this, requireContext(), baseUrl)
            recyclerViewMychllnge.adapter = adapter
        }
    }
    */


    private fun defaultTab() {
        tabs.getTabAt(0)?.select()
    }

    private fun setTabLayout() {
        tabs.addTab(tabs.newTab().setCustomView(R.layout.itemview_live_item))
        tabs.addTab(tabs.newTab().setCustomView(R.layout.itemview_done_item))
        tabs.addTab(tabs.newTab().setCustomView(R.layout.itemview_pending_item))

    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).showAndHideBottomNavigation(false)
        (activity as HomeActivity).toolbar!!.visibility = View.GONE
    }
}