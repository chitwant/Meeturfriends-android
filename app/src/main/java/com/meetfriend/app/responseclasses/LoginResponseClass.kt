package com.meetfriend.app.responseclasses

data class LoginResponseClass(
    val access_token: String,
    val message: String,
    val profile_updated_status: Boolean,
    val status: Boolean
)