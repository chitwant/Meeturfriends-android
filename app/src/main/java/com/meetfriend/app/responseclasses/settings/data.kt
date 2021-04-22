package com.meetfriend.app.responseclasses.settings

import com.meetfriend.app.responseclasses.user

data class data(
    val id: String,
    val user_id: String,
    val device_type: String,
    val device_model: String,
    val device_location: String,
    val created_at: String,
    val user: user
)