package com.meetfriend.app.responseclasses.posts

data class Data(
    val content: String,
    val created_at: String,
    val id: Int,
    val post_media: List<PostMedia>,
    val user: User,
    val user_id: Int
)