package com.meetfriend.app.responseclasses.profile

data class ViewProfileResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: Result,
    val status: Boolean
)