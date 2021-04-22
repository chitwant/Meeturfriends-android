package com.meetfriend.app.modals.friendsuggestion.homepost

data class Comments(
    val id: Int,
    val post_id: Int,
    val user_id: Int,
    val content: String,
    val created_at: String,
    val user:UserComment
)