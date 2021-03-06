package com.meetfriend.app.responseclasses.blocked

data class BlockedUsersResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: Result,
    val status: Boolean
)