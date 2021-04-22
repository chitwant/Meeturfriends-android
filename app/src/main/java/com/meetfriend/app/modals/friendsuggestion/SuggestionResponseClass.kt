package com.meetfriend.app.modals.friendsuggestion

data class SuggestionResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: Result,
    val status: Boolean
)