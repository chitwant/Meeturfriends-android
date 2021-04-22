package com.meetfriend.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.ui.activities.AddBankDetailActivity
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.activities.LoginActivity
import com.meetfriend.app.ui.activities.SearchUserListingActivity
import com.meetfriend.app.utilclasses.UtilsClass
import contractorssmart.app.utilsclasses.PreferenceHandler
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logoutLayout.setOnClickListener {
            logout()
        }
        resetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_more_to_resetPasswordFragment)
        }
        myProfileLayout.setOnClickListener {
            val bundle = bundleOf(
                "type" to "own"
            )
            findNavController().navigate(R.id.action_navigation_more_to_profileFragment, bundle)
        }
        requestsLayout.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_more_to_friendRequestFragment)
        }
        suggestionsLayout.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_more_to_friendSuggestionsFragment)
        }
        savePosts.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_more_to_savedPostsFragment)
        }
        if (PreferenceHandler.readInteger(requireContext(), "bankUpdated", 0) == 0) {
            bankDetails.visibility = View.GONE
        }
        bankDetails.setOnClickListener {
            val intent = Intent(requireContext(), AddBankDetailActivity::class.java)
            intent.putExtra("from", "update");
            startActivity(intent)
        }
        (activity as HomeActivity).ivSearchIcon!!.visibility = View.GONE

    }

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_more, parent, false)
    }

    private fun logout() {
        val builder =
            AlertDialog.Builder(requireActivity())
        builder.setCancelable(false)
        builder.setMessage("Are you sure, you want to logout?")
        builder.setPositiveButton(
            "Yes"
        ) { dialog, _ ->
            dialog.dismiss()
            UtilsClass.updateUserStatus(requireActivity(), false)
            PreferenceHandler.clearSharePreferences(requireContext())
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
}