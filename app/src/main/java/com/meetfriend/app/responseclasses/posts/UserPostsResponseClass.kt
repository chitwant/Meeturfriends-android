package com.meetfriend.app.responseclasses.posts

data class UserPostsResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: Result,
    val status: Boolean
)