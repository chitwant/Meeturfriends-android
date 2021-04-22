package com.meetfriend.app.responseclasses.photos

data class UserPhotosResponse(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: Result,
    val status: Boolean
)