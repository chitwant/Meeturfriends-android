package com.meetfriend.app.responseclasses.friends

data class Data(
    val accepted_user: AcceptedUser,
    val block_status: String,
    val friend_id: Int,
    val is_blocked_by_me: Int,
    val friend_status: String,
    val id: Int,
    val request_status: String,
    val user_id: Int,
    val isadded: Int
)