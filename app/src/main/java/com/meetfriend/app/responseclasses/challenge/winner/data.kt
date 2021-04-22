package com.meetfriend.app.responseclasses.challenge.winner

import com.meetfriend.app.responseclasses.user

class data(
    val id: Int,
    val user_id: Int,
    val challenge_id: Int,
    val description: String,
    val file_path: String,
    val created_at: String,
    val no_of_likes_count: Int,
    val no_of_views_count: Int,
    val user: user,
    val challenge: challenge

)