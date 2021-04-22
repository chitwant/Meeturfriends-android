package com.meetfriend.app.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meetfriend.app.R
import com.meetfriend.app.utilclasses.UtilsClass
import com.sanojpunchihewa.updatemanager.UpdateManager
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant

class LoginActivity : AppCompatActivity() {
    var mUpdateManager: UpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.IMMEDIATE);
        mUpdateManager?.start()

    }

    override fun onResume() {
        super.onResume()
        UtilsClass.updateUserStatus(this, true)
    }

    override fun onPause() {
        super.onPause()
        UtilsClass.updateUserStatus(this, false)
    }
}