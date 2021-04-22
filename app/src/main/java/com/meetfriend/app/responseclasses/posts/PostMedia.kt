package com.meetfriend.app.responseclasses.posts

data class PostMedia(
    val created_at: String,
    val extension: String,
    val file_name: String,
    val file_path: String,
    val id: Int,
    val posts_id: Int,
    val size: String
)