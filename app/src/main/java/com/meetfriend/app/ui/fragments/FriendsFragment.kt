package com.meetfriend.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.fragments.profile.MyfriendsFragment
import kotlinx.android.synthetic.main.fragment_friends.*

class FriendsFragment : BaseFragment() {
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var froms: String? = ""

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_friends, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        froms = getArguments()?.getString("from")
        if (froms != null) {
            viewPagerAdapter = froms?.let {
                ViewPagerAdapter(
                    childFragmentManager,
                    it,
                    containerss
                )
            }
        } else {
            viewPagerAdapter =
                ViewPagerAdapter(
                    childFragmentManager,
                    "",
                    containerss
                )
            (activity as HomeActivity).ivSearchIcon!!.visibility = View.GONE

        }
        if (froms != null) {
            lytHeader.visibility = View.VISIBLE
            headerLoginBackButton.setOnClickListener(View.OnClickListener {
                requireActivity().onBackPressed()
            })
        }
        viewPagerFriends.setAdapter(viewPagerAdapter)
        tabLayout.setupWithViewPager(viewPagerFriends)

    }

    class ViewPagerAdapter(
        fm: FragmentManager,
        froms: String,
        containerss: RelativeLayout
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        var ff = froms;
        var container = containerss;
        override fun getItem(position: Int): Fragment {

            var fragment: Fragment? = null
            if (position == 0) fragment = MyfriendsFragment(ff,R.id.containerss)
            else if (position == 1) fragment =
                FriendRequestFragment(ff)
            else if (position == 2) fragment =
                FriendSuggestionsFragment(ff, R.id.containerss)
            else if (position == 3) fragment =
                BlockedUsersFragment(ff)
            return fragment!!
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            var title: String? = null
            if (position == 0) title = "Friends" else if (position == 1) title =
                "Requests" else if (position == 2) title =
                "Suggestions" else if (position == 3) title = "Blocked"
            return title
        }
    }

    fun newInstance(address: String?): FriendsFragment? {
        val myFragment = FriendsFragment()
        val args = Bundle()
        args.putString("from", address)
        myFragment.setArguments(args)
        return myFragment
    }
}