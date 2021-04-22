package com.meetfriend.app.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meetfriend.app.R
import com.meetfriend.app.ui.fragments.challenge.CountryChooseFragment
import kotlinx.android.synthetic.main.toolbar.*

class CreateChallengeActivity : AppCompatActivity() {
     val TAG_1 = "fragCreateChallenge2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_challenge)

        ivBackIcon.setOnClickListener {
            onBackPressed()
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.container, CountryChooseFragment())
//            ?.addToBackStack(TAG_1)
            .commit()
    }

}