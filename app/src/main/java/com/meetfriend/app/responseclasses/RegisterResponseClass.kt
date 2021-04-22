package com.meetfriend.app.responseclasses

data class RegisterResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val profile_updated_status: Boolean,
    val result: Result,
    val status: Boolean,
    val access_token:String
)