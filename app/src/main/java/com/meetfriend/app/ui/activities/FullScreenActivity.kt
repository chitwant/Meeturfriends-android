package com.meetfriend.app.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.meetfriend.app.R
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)
        val url=intent.extras!!.getString("url","")
        CommonMethods.setImage(photo_view, url)
        closeActivity.setOnClickListener {
            onBackPressed()
        }
    }
}