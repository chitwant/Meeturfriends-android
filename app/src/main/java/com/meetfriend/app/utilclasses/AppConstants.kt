package com.meetfriend.app.utilclasses

object AppConstants {
    val APIS_BASE_URL = "https://www.meeturfriends.com/"
    var CHAT_ID = "chat_id_dgfxdvgyfc"

    //val APIS_BASE_URL="http://www.meetfreinds.com/api/"
    val POST_PIC_QUALITY=23
    val UPDATE_PIC_QUALITY=18
    val PROFILE_COVER_PIC_LOADING=3500
    val POST_PIC_LOADING=2500
    internal interface httpcodes {
        companion object {
            val STATUS_OK = 200
            val STATUS_BAD_REQUEST = 400
            val STATUS_SESSION_EXPIRED = 401
            val STATUS_PLAN_EXPIRED = 403
            val STATUS_VALIDATION_ERROR = 404
            val STATUS_SERVER_ERROR = 500
            val STATUS_UNKNOWN_ERROR = 503
            val STATUS_API_VALIDATION_ERROR = 401
            val SESSION_EXPIRED = 203
        }
    }

    const val DOB_FORMAT = "MM/dd/yyyy"
    const val CHALLENGE_DATE_FORMAT = "yyyy-MM-dd"
    const val CHALLENGE_TIME_FORMAT = "HH:mm"
    const val UPDATE_PROFILE_RECIEVER = "update_profile_receiver"

    const val SNACK_BAR_DURATION = 2500
    const val SOMETHING_WENT_WRONG = "Something went wrong please try again later!"
    const val SESSION_EXPIRED =
        "Sorry, looks like you are logged in another device with the same user."
}