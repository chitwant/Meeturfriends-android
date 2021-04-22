package com.meetfriend.app.ui.activities

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseActivity
import com.meetfriend.app.ui.fragments.PostDetailsFragment
import com.meetfriend.app.ui.fragments.profile.ProfileFragment


class ProfileActivity : BaseActivity() {
    private var type = ""
    private var otherUserId = ""
    private var callType = ""
    private var post_id = -1
    override fun initializeLayout(): Int {
        return R.layout.activity_profile
    }

    override fun inflateLayout() {
        type=intent.extras!!.getString("type","")
        otherUserId=intent.extras!!.getString("userId","")
        callType=intent.extras!!.getString("call","")
        post_id=intent.extras!!.getInt("post_id",0)
        // Begin the transaction
        // Begin the transaction
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        // Replace the contents of the container with the new fragment
        // Replace the contents of the container with the new fragment
        if (callType.equals("post")){
            val bundle= Bundle()
            bundle.putInt("position",-1)
            bundle.putInt("post_id",post_id)
            bundle.putInt("ff",1)
            var fragment=PostDetailsFragment()
            fragment.arguments=bundle
            ft.replace(R.id.fragment_container, fragment)
            // or ft.add(R.id.your_placeholder, new ABCFragment());
            // Complete the changes added above
            // or ft.add(R.id.your_placeholder, new ABCFragment());
            // Complete the changes added above
            ft.commit()
        }
    else{
            val bundle= Bundle()
            bundle.putString("type",type)
            bundle.putString("userId",otherUserId)
            bundle.putString("from","activity")
            var fragment=ProfileFragment()
            fragment.arguments=bundle
            ft.replace(R.id.fragment_container, fragment)
            // or ft.add(R.id.your_placeholder, new ABCFragment());
            // Complete the changes added above
            // or ft.add(R.id.your_placeholder, new ABCFragment());
            // Complete the changes added above
            ft.commit()
        }

    }

}