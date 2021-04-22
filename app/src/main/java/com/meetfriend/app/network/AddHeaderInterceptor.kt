package com.meetfriend.app.network

import android.util.Log
import com.meetfriend.app.ApplicationClass
import contractorssmart.app.utilsclasses.PreferenceHandler
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 */

class AddHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var token = PreferenceHandler.readString(
            ApplicationClass.getContext(),
            PreferenceHandler.AUTHORIZATION_TOKEN,
            ""
        )
        val builder = chain.request().newBuilder()
        Log.v("---TOKEN---", token)
        builder.addHeader("Authorization", "Bearer " + token)
        builder.addHeader("Accept", "application/json")
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded")
        return chain.proceed(builder.build())
    }

    /**
     ******** get current timezone string *****************
     */
    private fun getTimeZone(): String {
        val tz = TimeZone.getDefault()
        val offset = tz.rawOffset
        return String.format(
            "%s%02d:%02d",
            if (offset >= 0) "+" else "-",
            offset / 3600000,
            offset / 60000 % 60
        )
    }

}