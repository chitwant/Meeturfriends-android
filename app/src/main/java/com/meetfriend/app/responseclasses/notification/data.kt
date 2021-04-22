package com.meetfriend.app.responseclasses.notification

data class data(
    val id: String,
    val user_id: String,
    val device_type: String,
    val device_model: String,
    val device_location: String,
    val created_at: String,
    val message:String,
    val type:String,
    val type_id:Int,
    val user: user,
    val from_user: fromUser
)