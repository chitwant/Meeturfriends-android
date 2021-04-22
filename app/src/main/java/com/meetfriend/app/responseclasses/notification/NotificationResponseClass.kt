package com.meetfriend.app.responseclasses.notification


data class NotificationResponseClass(
    val status: Boolean,
    val message: String,
    val media_url:String,
    val result: Result
)