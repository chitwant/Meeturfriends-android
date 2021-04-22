package com.meetfriend.app.responseclasses

data class UpdatePhotoResponse(
    val base_url: String,
    val media_url: String,
    val message: String,
    val profile_photo: String,
    val status: Boolean,
    val cover_photo:String
)