package com.meetfriend.app.modals.friendsuggestion.homepost

data class HomePostResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: Result,
    val status: Boolean
)