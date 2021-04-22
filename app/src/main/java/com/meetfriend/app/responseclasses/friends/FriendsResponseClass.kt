package com.meetfriend.app.responseclasses.friends

data class FriendsResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: Result,
    val status: Boolean
)