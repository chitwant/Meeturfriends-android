package com.meetfriend.app.modals.friendsuggestion.homepost

data class Data(
    val content: String,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val post_media: ArrayList<PostMedia>,
    val privacy: Int,
    val status: String,
    val updated_at: String,
    val user: User,
    val user_id: Int,
    val uuid: String,
    val post_comments: ArrayList<Comments>,
    val is_shared: Int,
    val shared_user_id: Int,
    var post_likes_count: Int,
    var is_liked_count: Int,
    var post_shares_count: Int
)