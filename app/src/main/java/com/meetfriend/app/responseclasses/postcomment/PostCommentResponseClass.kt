package com.meetfriend.app.responseclasses.postcomment

import com.meetfriend.app.modals.friendsuggestion.homepost.Comments
import com.meetfriend.app.responseclasses.homeposts.Comment

data class PostCommentResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: Comment,
    val status: Boolean
)