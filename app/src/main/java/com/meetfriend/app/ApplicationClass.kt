package com.meetfriend.app


import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.multidex.MultiDex
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.meetfriend.app.acra.YourOwnSenderfactory
import com.meetfriend.app.utilclasses.MyFcmListenerService
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import java.util.*

@ReportsCrashes(
    reportSenderFactoryClasses = [YourOwnSenderfactory::class],
    mode = ReportingInteractionMode.DIALOG,
    resDialogText = R.string.crash_dialog_text,
    resDialogTitle = R.string.crash_dialog_title
)
class ApplicationClass : Application() {

    var mContext: Context? = null

    companion object AppContext {
        lateinit var instance: Application
        fun getContext(): Context {
            return instance
        }
    }

    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.places_api_key), Locale.US);
        }
        FirebaseApp.initializeApp(this)
        ACRA.init(this)
        ACRA.isInitialised()
        mContext = applicationContext
        MultiDex.install(this);

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

}